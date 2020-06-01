package luhegi.mods.simplyrandom.coalgen;

import luhegi.mods.simplyrandom.SimplyRandom;
import luhegi.mods.simplyrandom.basis.objects.BasisTile;
import luhegi.mods.simplyrandom.basis.tile.BasisEnergyStorage;
import luhegi.mods.simplyrandom.basis.tile.BasisItemHandler;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.AbstractFurnaceTileEntity;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.Direction;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

import java.util.EnumMap;
import java.util.Optional;

public class CoalGenTile extends BasisTile<CoalGenTile> implements ITickableTileEntity {
    private static final String NBT_KEY_FUEL_LEFT = SimplyRandom.ID + ":fuel_left";
    private final BasisEnergyStorage battery = new BasisEnergyStorage(CoalGen.getMaxStorage());
    private final BasisItemHandler coalSlot = new BasisItemHandler(1).withSlotCheck(new BasisItemHandler.SlotFilter(0, AbstractFurnaceTileEntity::isFuel));
    private final EnumMap<Direction, LazyOptional<IEnergyStorage>> sideCache = new EnumMap<>(Direction.class);
    private int fuelLeft;

    public CoalGenTile() {
        super(CoalGen.INSTANCE.tileType);
        addCap(battery);
        addCap(coalSlot);
    }

    @Override
    public void tick() {
        if (!(world instanceof ServerWorld)) return;
        handlerPowerGen();
        handlePowerPush();
    }

    private void handlerPowerGen() {
        if (fuelLeft == 0) {
            ItemStack singleCoal = coalSlot.extractItem(0, 1, false);
            if (singleCoal.isEmpty()) return;
            fuelLeft += ForgeHooks.getBurnTime(singleCoal);
            return;
        }
        if (battery.getEnergyStored() + CoalGen.getEnergyPerTick() > battery.getMaxEnergyStored()) {
            return;
        }
        fuelLeft--;
        battery.add(CoalGen.getEnergyPerTick());
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

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        compound.putInt(NBT_KEY_FUEL_LEFT, fuelLeft);
        return super.write(compound);
    }

    @Override
    public void read(CompoundNBT compound) {
        fuelLeft = compound.getInt(NBT_KEY_FUEL_LEFT);
        super.read(compound);
    }
}
