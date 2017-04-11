package bluemonster122.mods.simplethings.tileentity;

import bluemonster122.mods.simplethings.tileentity.core.IAcceptPower;
import bluemonster122.mods.simplethings.tileentity.core.IProvidePower;
import bluemonster122.mods.simplethings.tileentity.core.TileEntityST;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.EnergyStorage;

import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class TilePowerStorage extends TileEntityST implements ITickable, IAcceptPower, IProvidePower {
    private EnergyStorage battery = new EnergyStorage(1000000);

    @Override
    public void update() {
        this.attemptDischarge();
    }

    @Override
    public EnergyStorage getBattery() {
        return battery;
    }

    @Override
    public Set<Consumer<NBTTagCompound>> getAllWrites() {
        return null;
    }

    @Override
    public Set<Consumer<NBTTagCompound>> getAllReads() {
        return null;
    }

    @Override
    public Map<Capability, Supplier<Capability>> getCaps() {
        return null;
    }

    @Override
    public Set<Consumer<NBTTagCompound>> getMinWrites() {
        return null;
    }

    @Override
    public Set<Consumer<NBTTagCompound>> getMinReads() {
        return null;
    }
}
