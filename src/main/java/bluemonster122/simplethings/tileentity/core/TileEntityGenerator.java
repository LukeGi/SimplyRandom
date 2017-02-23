package bluemonster122.simplethings.tileentity.core;

import net.minecraft.util.ITickable;
import net.minecraftforge.energy.EnergyStorage;

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
