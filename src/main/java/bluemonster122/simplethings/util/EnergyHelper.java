package bluemonster122.simplethings.util;

import bluemonster122.simplethings.tileentity.TilePowerCable;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
public class EnergyHelper
{
	public static void checkForTakers(IEnergyStorage energyStorageIn, World worldIn, BlockPos posIn)
	{
		List<BlockPos> recievers = new ArrayList<>();
		List<BlockPos> visited = new ArrayList<>();
		Stack<BlockPos> toVisit = new Stack<>();
		toVisit.add(posIn);
		while (!toVisit.isEmpty())
		{
			BlockPos current = toVisit.pop();
			visited.add(current);
			TileEntity tileEntity = worldIn.getTileEntity(current);
			if (tileEntity != null && (tileEntity.hasCapability(CapabilityEnergy.ENERGY, null) || tileEntity instanceof TilePowerCable))
			{
				for (EnumFacing f : EnumFacing.VALUES)
				{
					BlockPos element = current.offset(f);
					if (!worldIn.isAirBlock(element) && !toVisit.contains(element) && !visited.contains(element))
					{
						toVisit.add(element);
					}
				}
				if (current != posIn && !(tileEntity instanceof TilePowerCable))
					recievers.add(current);
			}
		}
		for (BlockPos pos : recievers)
		{
			IEnergyStorage taker = worldIn.getTileEntity(pos).getCapability(CapabilityEnergy.ENERGY, null);
			if (taker.canReceive())
			{
				energyStorageIn.receiveEnergy(taker.receiveEnergy(energyStorageIn.extractEnergy(energyStorageIn.getEnergyStored(), false), false), false);
			} else
			{
				continue;
			}
			if (energyStorageIn.getEnergyStored() <= 0)
			{
				return;
			}
		}
	}
	
	public static EnergyStorage makeNewAndFill(int energy, int max, int maxIn, int maxOut)
	{
		EnergyStorage battery = new EnergyStorage(max, maxIn, maxOut);
		battery.receiveEnergy(energy, false);
		return battery;
	}
}
