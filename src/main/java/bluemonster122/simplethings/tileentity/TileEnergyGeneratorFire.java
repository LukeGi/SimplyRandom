package bluemonster122.simplethings.tileentity;

import net.minecraft.init.Blocks;

public class TileEnergyGeneratorFire extends TileEntityGenerator
{
	private EnergyStorageGenerator battery = new EnergyStorageGenerator(1000);

	@Override
	protected void generatePower()
	{
		getBatteryGenerator().generate(1);
	}

	@Override
	protected boolean generateCondition()
	{
		return getWorld().getBlockState(getPos().up(2)).getBlock().equals(Blocks.FIRE);
	}

	@Override
	protected int getTickTimer()
	{
		return 1;
	}

	@Override
	protected EnergyStorageGenerator getBatteryGenerator()
	{
		return battery;
	}
}
