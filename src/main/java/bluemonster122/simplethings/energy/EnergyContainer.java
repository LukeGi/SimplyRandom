package bluemonster122.simplethings.energy;

import cofh.api.energy.EnergyStorage;
import net.darkhax.tesla.api.ITeslaConsumer;
import net.darkhax.tesla.api.ITeslaHolder;
import net.darkhax.tesla.api.ITeslaProducer;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fml.common.Optional;

@Optional.InterfaceList({
        @Optional.Interface(iface = "ITeslaHolder", modid = "Tesla"),
        @Optional.Interface(iface = "ITeslaConsumer", modid = "Tesla"),
        @Optional.Interface(iface = "ITeslaProducer", modid = "Tesla")
})
public class EnergyContainer extends EnergyStorage implements ITeslaHolder, ITeslaConsumer, ITeslaProducer, IEnergyStorage
{
    public EnergyContainer(int capacity)
    {
        super(capacity);
    }

    public EnergyContainer(int capacity, int maxTransfer)
    {
        super(capacity, maxTransfer);
    }

    public EnergyContainer(int capacity, int maxReceive, int maxExtract)
    {
        super(capacity, maxReceive, maxExtract);
    }

    @Optional.Method(modid = "Tesla")
    @Override
    public long getStoredPower()
    {
        return getEnergyStored();
    }

    @Optional.Method(modid = "Tesla")
    @Override
    public long getCapacity()
    {
        return getMaxEnergyStored();
    }

    @Optional.Method(modid = "Tesla")
    @Override
    public long givePower(long power, boolean simulated)
    {
        if (canReceive())
        {
            return receiveEnergy(power > Integer.MAX_VALUE ? Integer.MAX_VALUE : (int) power, simulated);
        } else
        {
            return 0;
        }
    }

    @Optional.Method(modid = "Tesla")
    @Override
    public long takePower(long power, boolean simulated)
    {
        if (canExtract())
        {
            return extractEnergy(power > Integer.MAX_VALUE ? Integer.MAX_VALUE : (int) power, simulated);
        } else
        {
            return 0;
        }
    }

    @Override
    public boolean canExtract()
    {
        return maxExtract > 0;
    }

    @Override
    public boolean canReceive()
    {
        return maxReceive > 0;
    }
}
