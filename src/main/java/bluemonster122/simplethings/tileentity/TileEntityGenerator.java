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
			energy += energyToGenerate;
		}

		@Override
		public boolean canReceive()
		{
			return false;
		}
	}
}
