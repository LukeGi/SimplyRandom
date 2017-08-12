package bluemonster.simplerandomstuff.generators;

import bluemonster.simplerandomstuff.core.energy.BatteryST;
import bluemonster.simplerandomstuff.core.energy.IEnergyProviderST;
import bluemonster.simplerandomstuff.core.block.TileST;
import com.google.common.collect.ImmutableMap;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

import java.util.Map;

public abstract class TileGenerator
        extends TileST
        implements ITickable, IEnergyProviderST {

    public BatteryST battery = createBattery();

    @Override
    public void update() {
        if (!getWorld().isRemote) {
            for (EnumFacing dir : EnumFacing.VALUES) {
                BlockPos pos = getPos().offset(dir);
                TileEntity tile = getWorld().getTileEntity(pos);
                if (tile != null && tile.hasCapability(CapabilityEnergy.ENERGY, dir.getOpposite())) {
                    IEnergyStorage otherBattery = tile.getCapability(CapabilityEnergy.ENERGY, dir.getOpposite());
                    if (otherBattery != null && otherBattery.canReceive()) {
                        battery.extractEnergy(otherBattery.receiveEnergy(battery.getEnergyStored(), false), false);
                    }
                }
            }
        }
    }

    /**
     * Gets the Tile's current battery.
     *
     * @return The Tile's current battery.
     */
    @Override
    public BatteryST getBattery() {
        return battery;
    }

    /**
     * Sets the given BatteryST to be the Tile's Battery.
     *
     * @param battery new Battery.
     */
    @Override
    public void setBattery(BatteryST battery) {
        this.battery = battery;
    }

    @Override
    public Map<Capability, Capability> getCaps() {
        return ImmutableMap.of(CapabilityEnergy.ENERGY, CapabilityEnergy.ENERGY.cast((IEnergyStorage) battery));
    }
}
