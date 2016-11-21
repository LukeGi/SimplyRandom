package bluemonster122.simplethings.tileentity;

import bluemonster122.simplethings.util.EnergyHelper;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;

public class TileEnergyGeneratorFire extends TileEntity implements ITickable, IEnergyStorage
{
    private EnergyStorage energy = new EnergyStorage(1000);

    @Override
    public void update()
    {
        if (getWorld().getBlockState(getPos().up(2)).getBlock().equals(Blocks.FIRE))
        {
            getEnergy().receiveEnergy(1, false);
        }
        if (this.getEnergyStored() > 0)
        {
            EnergyHelper.checkForTakers(getEnergy(), getWorld(), getPos());
        }
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate)
    {
        return getEnergy().receiveEnergy(maxReceive, simulate);
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate)
    {
        return getEnergy().extractEnergy(maxExtract, simulate);
    }

    @Override
    public int getEnergyStored()
    {
        return getEnergy().getEnergyStored();
    }

    @Override
    public int getMaxEnergyStored()
    {
        return getEnergy().getMaxEnergyStored();
    }

    @Override
    public boolean canExtract()
    {
        return true;
    }

    @Override
    public boolean canReceive()
    {
        return false;
    }

    public EnergyStorage getEnergy()
    {
        return energy;
    }
}
