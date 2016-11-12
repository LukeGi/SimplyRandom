package bluemonster122.simplethings.feature.firegenerator;

import bluemonster122.simplethings.energy.EnergyContainer;
import bluemonster122.simplethings.energy.TileEntityEnergy;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

import java.util.ArrayList;
import java.util.List;

public class TileEntityFireGenerator extends TileEntityEnergy implements ITickable
{
    @Override
    public EnergyContainer makeNewBattery()
    {
        return new EnergyContainer(1000, 1, 100);
    }

    @Override
    public void update()
    {
        if (worldObj.getBlockState(pos.up(2)).getBlock().equals(Blocks.FIRE))
        {
            receiveEnergy(1, false);
        }
        if (this.getEnergyStored() > 0)
        {
            checkForTakers();
        }
    }

    private void checkForTakers()
    {
        List<IEnergyStorage> takers = new ArrayList<>();
        for (EnumFacing value : EnumFacing.VALUES)
        {
            BlockPos pos = this.pos.offset(value);
            TileEntity tileEntity = worldObj.getTileEntity(pos);
            if (tileEntity != null)
            {
                for (int i = 0; i < EnumFacing.VALUES.length; i++)
                {
                    EnumFacing check = EnumFacing.VALUES[i];
                    if (check == value.getOpposite()) continue;
                    if (tileEntity.hasCapability(CapabilityEnergy.ENERGY, check))
                    {
                        takers.add(tileEntity.getCapability(CapabilityEnergy.ENERGY, check));
                        break;
                    }
                }
            }
        }
        for (IEnergyStorage taker : takers)
        {
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
}
