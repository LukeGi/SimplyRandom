package luhegi.mods.simplyrandom.solargen;

import luhegi.mods.simplyrandom.basis.objects.BasisTile;
import luhegi.mods.simplyrandom.basis.tile.BasisEnergyStorage;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.Direction;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

import java.util.EnumMap;
import java.util.Optional;

public class SolarGeneratorTile extends BasisTile<SolarGeneratorTile> implements ITickableTileEntity {
    private final EnumMap<Direction, LazyOptional<IEnergyStorage>> sideCache = new EnumMap<>(Direction.class);
    private BasisEnergyStorage battery = new BasisEnergyStorage(SolarGenerator.getMaxStorage());

    public SolarGeneratorTile() {
        super(SolarGenerator.INSTANCE.tileType);
        addCap(battery);
    }



    @Override
    public void tick() {
        if (!(world instanceof ServerWorld))
            return;
        if (canGenerate()) {
            makeSomeEnergy();
            world.setBlockState(getPos(), getBlockState().with(SolarGeneratorBlock.POWERED, true), 3);
        } else {
            world.setBlockState(getPos(), getBlockState().with(SolarGeneratorBlock.POWERED, false), 3);
        }
        handlePowerPush();
    }

    private boolean canGenerate() {
        return sunIsUp() && canSeeSky() && !isRaining();
    }

    private boolean isRaining() {
        return world.isRaining();
    }

    private boolean canSeeSky() {
        return world.canSeeSky(getPos().up());
    }

    private boolean sunIsUp() {
        assert world != null;
        return world.isDaytime();
    }

    private void makeSomeEnergy() {
        if (battery.getEnergyStored() + SolarGenerator.getEnergyPerTick() > battery.getMaxEnergyStored()) {
            return;
        }
        battery.add((int) (SolarGenerator.getEnergyPerTick() * (isWaterlogged() ? SolarGenerator.getWaterloggedMultiplier() : 1)));
    }

    private boolean isWaterlogged() {
        return getBlockState().get(SolarGeneratorBlock.WATERLOGGED);
    }

    private void handlePowerPush() {
        int energyToTake = 0;
        for (Direction value : Direction.values()) {
            LazyOptional<IEnergyStorage> sideBattery = sideCache.compute(value, ((direction, cache) -> {
                if (cache != null && cache.isPresent())
                    return cache;
                else
                    return Optional.ofNullable(world.getTileEntity(pos.offset(direction)))
                            .map(tileEntity -> tileEntity.getCapability(CapabilityEnergy.ENERGY, direction.getOpposite()))
                            .orElse(LazyOptional.empty());
            }));
            final int max = battery.getEnergyStored() - energyToTake;
            final int accepted = sideBattery.map(bat -> bat.receiveEnergy(max, true)).orElse(0);
            energyToTake += sideBattery.map(bat -> bat.receiveEnergy(accepted, false)).orElse(0);
            if (energyToTake == battery.getEnergyStored()) break;
        }
        battery.extractEnergy(energyToTake, false);
    }
}
