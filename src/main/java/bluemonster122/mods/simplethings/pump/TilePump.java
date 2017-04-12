package bluemonster122.mods.simplethings.pump;

import bluemonster122.mods.simplethings.core.block.IHaveTank;
import bluemonster122.mods.simplethings.core.block.TileST;
import bluemonster122.mods.simplethings.core.energy.BatteryST;
import bluemonster122.mods.simplethings.core.energy.IEnergyRecieverST;
import com.google.common.collect.ImmutableMap;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

import java.util.Map;
import java.util.function.Supplier;

public class TilePump extends TileST implements IEnergyRecieverST, IHaveTank {
    public FluidTank tank = createTank();
    public BatteryST battery = createBattery();

    @Override
    public Map<Capability, Supplier<Capability>> getCaps() {
        return ImmutableMap.of(
                CapabilityEnergy.ENERGY, () -> CapabilityEnergy.ENERGY.cast((IEnergyStorage) tank),
                CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, () -> CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast((IFluidHandler) tank)
        );
    }

    @Override
    public NBTTagCompound writeChild(NBTTagCompound tag) {
        return tag;
    }

    @Override
    public NBTTagCompound readChild(NBTTagCompound tag) {
        return tag;
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
     * Creates a new Battery for the Tile.
     *
     * @return a new Battery for the Tile.
     */
    @Override
    public BatteryST createBattery() {
        return new BatteryST(100);
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

    /**
     * Gets the Tile's current Tank.
     *
     * @return The Tile's current Tank.
     */
    @Override
    public FluidTank getTank() {
        return tank;
    }

    /**
     * Creates a new Tank for the Tile.
     *
     * @return a new Tank for the Tile.
     */
    @Override
    public FluidTank createTank() {
        return new FluidTank(1000);
    }

    /**
     * Sets the given ItemStackHandler to be the Tile's Tank.
     *
     * @param tank new Inventory.
     */
    @Override
    public void setTank(FluidTank tank) {
        this.tank = tank;
    }
}
