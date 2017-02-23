package bluemonster122.simplethings.tileentity;

import bluemonster122.simplethings.tileentity.things.IProvidePower;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;

public abstract class TileEntityGenerator extends TileEntityST implements IProvidePower, ITickable
{

    @Override
    public void update()
    {
        if (getWorld().isRemote) return;
        if (getWorld().getTotalWorldTime() % getTickTimer() == 0 && generateCondition() && getBattery().getMaxEnergyStored() > getBattery().getEnergyStored())
        {
            generatePower();
        }
        if (getEnergyStored() > 0)
        {
            attemptDischarge();
        }
    }

    private void attemptDischarge()
    {
        List<BlockPos> network = scanNetwork();
        List<IEnergyStorage> batteries = network.stream().map(getWorld()::getTileEntity).map(tileEntity -> tileEntity.getCapability(CapabilityEnergy.ENERGY, null)).filter(b -> b.getEnergyStored() < b.getMaxEnergyStored()).collect(Collectors.toList());
        for (IEnergyStorage battery : batteries)
        {
            extractPower(battery.receiveEnergy(getEnergyStored(), false), false);
            if (getEnergyStored() <= 0)
            {
                break;
            }
        }
    }

    private List<BlockPos> scanNetwork()
    {
        List<BlockPos> recievers = new ArrayList<>();
        List<BlockPos> visited = new ArrayList<>();
        Stack<BlockPos> toVisit = new Stack<>();
        toVisit.add(getPos());

        while (!toVisit.isEmpty())
        {
            // get next value
            // check if this value accepts power
            // if so add it to the list
            // then check if the blocks around are in the recievers, visited or tovisit lists, and that they have an energy capability
            // if they match, add them to the tovisit list
            // add them to the visited list.
            BlockPos blockPos = toVisit.pop();
            TileEntity tileEntity = getWorld().getTileEntity(blockPos);
            if (tileEntity != null && (tileEntity.hasCapability(CapabilityEnergy.ENERGY, null) || tileEntity instanceof TilePowerCable))
            {
                IEnergyStorage battery = tileEntity.getCapability(CapabilityEnergy.ENERGY, null);
                if (battery != null && battery.canReceive())
                {
                    recievers.add(blockPos);
                }
                for (EnumFacing value : EnumFacing.VALUES)
                {
                    BlockPos element = blockPos.offset(value);
                    if (!getWorld().isAirBlock(element) && !toVisit.contains(element) && !visited.contains(element))
                    {
                        toVisit.add(element);
                    }
                    visited.add(element);
                }
            }
        }
        return recievers;
    }

    protected abstract void generatePower();

    protected abstract boolean generateCondition();

    protected abstract int getTickTimer();

    @Override
    public EnergyStorage getBattery()
    {
        return getBatteryGenerator();
    }

    protected abstract EnergyStorageGenerator getBatteryGenerator();

    protected class EnergyStorageGenerator extends EnergyStorage
    {
        public EnergyStorageGenerator(int capacity)
        {
            super(capacity);
        }

        public void generate(int energyToGenerate)
        {
            capacity += energyToGenerate;
        }

        @Override
        public boolean canReceive()
        {
            return false;
        }
    }
}
