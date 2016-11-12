package bluemonster122.simplethings.energy;

import net.darkhax.tesla.api.ITeslaConsumer;
import net.darkhax.tesla.api.ITeslaHolder;
import net.darkhax.tesla.api.ITeslaProducer;
import net.darkhax.tesla.capability.TeslaCapabilities;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Optional;

import java.util.HashSet;
import java.util.Set;

@Optional.InterfaceList({
        @Optional.Interface(iface = "ITeslaHolder", modid = "Tesla"),
        @Optional.Interface(iface = "ITeslaConsumer", modid = "Tesla"),
        @Optional.Interface(iface = "ITeslaProducer", modid = "Tesla")
})
public abstract class TileEntityEnergy extends TileEntity implements ITeslaHolder, ITeslaConsumer, ITeslaProducer, IEnergyStorage
{
    protected Set<Capability> capabilities = new HashSet<>();

    {
        capabilities.add(CapabilityEnergy.ENERGY);
        if (Loader.isModLoaded("Tesla"))
        {
            capabilities.add(TeslaCapabilities.CAPABILITY_CONSUMER);
            capabilities.add(TeslaCapabilities.CAPABILITY_HOLDER);
            capabilities.add(TeslaCapabilities.CAPABILITY_PRODUCER);
            capabilities.add(TeslaCapabilities.TESLA_CONSUMER);
        }
    }

    private EnergyContainer battery;

    public TileEntityEnergy()
    {
        battery = makeNewBattery();
    }

    public abstract EnergyContainer makeNewBattery();

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);
        battery.readFromNBT(compound);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        return battery.writeToNBT(super.writeToNBT(compound));
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing)
    {
        if (capabilities.contains(capability))
        {
            return true;
        } else
        {
            return super.hasCapability(capability, facing);
        }
    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing)
    {
        if (capabilities.contains(capability))
        {
            return (T) battery;
        } else
        {
            return super.getCapability(capability, facing);
        }
    }

    @Optional.Method(modid = "Tesla")
    @Override
    public long givePower(long power, boolean simulated)
    {
        return battery.givePower(power, simulated);
    }

    @Optional.Method(modid = "Tesla")
    @Override
    public long getStoredPower()
    {
        return battery.getStoredPower();
    }

    @Optional.Method(modid = "Tesla")
    @Override
    public long getCapacity()
    {
        return battery.getCapacity();
    }

    @Optional.Method(modid = "Tesla")
    @Override
    public long takePower(long power, boolean simulated)
    {
        return battery.takePower(power, simulated);
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate)
    {
        return battery.receiveEnergy(maxReceive, simulate);
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate)
    {
        return battery.extractEnergy(maxExtract, simulate);
    }

    @Override
    public int getEnergyStored()
    {
        return battery.getEnergyStored();
    }

    @Override
    public int getMaxEnergyStored()
    {
        return battery.getMaxEnergyStored();
    }

    @Override
    public boolean canExtract()
    {
        return battery.canExtract();
    }

    @Override
    public boolean canReceive()
    {
        return battery.canReceive();
    }
}
