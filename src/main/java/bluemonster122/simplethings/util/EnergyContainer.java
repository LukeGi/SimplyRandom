package bluemonster122.simplethings.util;

import cofh.api.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;

public class EnergyContainer extends EnergyStorage implements IEnergyStorage
{
    public EnergyContainer(int capacity, int maxReceive, int maxExtract)
    {
        super(capacity, maxReceive, maxExtract);
    }

    @Override
    public boolean canExtract()
    {
        return getMaxExtract() > 0;
    }

    @Override
    public boolean canReceive()
    {
        return getMaxReceive() > 0;
    }
}
