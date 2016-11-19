package bluemonster122.simplethings.tileentity;

import bluemonster122.simplethings.util.EnergyContainerGenerator;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public abstract class TileEnergyGenerator extends TileEntity implements IEnergyStorage
{
    private EnergyContainerGenerator battery;

    public TileEnergyGenerator()
    {
        this.battery = makeNewBattery();
    }

    public abstract EnergyContainerGenerator makeNewBattery();

    public EnergyContainerGenerator getBattery()
    {
        return battery;
    }

    protected void checkForTakers()
    {
        List<BlockPos> recievers = new ArrayList<>();
        List<BlockPos> visited = new ArrayList<>();
        Stack<BlockPos> toVisit = new Stack<>();

        toVisit.add(getPos());
        while (!toVisit.isEmpty())
        {
            BlockPos current = toVisit.pop();
            visited.add(current);
            TileEntity tileEntity = worldObj.getTileEntity(current);
            if (tileEntity != null && (tileEntity.hasCapability(CapabilityEnergy.ENERGY, null) || tileEntity instanceof TilePowerCable))
            {
                for (EnumFacing f : EnumFacing.VALUES)
                {
                    BlockPos element = current.offset(f);
                    if (!worldObj.isAirBlock(element) && !toVisit.contains(element) && !visited.contains(element))
                    {
                        toVisit.add(element);
                    }
                }
                if (current != getPos() && !(tileEntity instanceof TilePowerCable))
                    recievers.add(current);
            }
        }
        for (BlockPos pos : recievers)
        {
            IEnergyStorage taker = worldObj.getTileEntity(pos).getCapability(CapabilityEnergy.ENERGY, null);
            if (taker.canReceive())
            {
                this.receiveEnergy(taker.receiveEnergy(this.extractEnergy(this.getEnergyStored(), false), false), false);
            } else
            {
                continue;
            }
            if (this.getEnergyStored() <= 0)
            {
                return;
            }
        }
    }

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
        if (capability.equals(CapabilityEnergy.ENERGY))
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
        if (capability.equals(CapabilityEnergy.ENERGY))
        {
            return CapabilityEnergy.ENERGY.cast(battery);
        } else
        {
            return super.getCapability(capability, facing);
        }
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
