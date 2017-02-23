package bluemonster122.simplethings.tileentity;

import bluemonster122.simplethings.handler.RegistryHandler;
import bluemonster122.simplethings.tileentity.core.TileEntityST;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ITickable;

public class TileLightningRod extends TileEntityST implements ITickable
{
	private boolean hasGold = false, hasWood = false, hasPiston = false, hasWater = false;
	private int workLeft = 30;

	@Override
	public void update()
	{
		if (getWorld().isRemote) return;


		if (getWorld().getBlockState(getPos().down()).getBlock().equals(Blocks.IRON_BLOCK) && getWorld().getTotalWorldTime() % 50 == 0 && hasWater && hasGold && hasPiston && hasWood)
		{
			getWorld().addWeatherEffect(new EntityLightningBolt(getWorld(), getPos().getX(), getPos().getY() + 1, getPos().getZ(), true));
			workLeft--;
		}
		if (workLeft == 0)
		{
			getWorld().setBlockState(getPos().down(), RegistryHandler.machine_block.getDefaultState());
			for (int i = 0; i < 10; i++)
			{
				getWorld().addWeatherEffect(new EntityLightningBolt(getWorld(), getPos().getX(), getPos().getY(), getPos().getZ(), true));
			}
			getWorld().destroyBlock(pos, false);
		}
	}

	public boolean addWater()
	{
		if (hasWater)
		{
			return false;
		} else
		{
			hasWater = true;
			return true;
		}
	}

	public boolean addGold()
	{
		if (hasGold)
		{
			return false;
		} else
		{
			hasGold = true;
			return true;
		}
	}

	public boolean addWood()
	{
		if (hasWood)
		{
			return false;
		} else
		{
			hasWood = true;
			return true;
		}
	}

	public boolean addPiston()
	{
		if (hasPiston)
		{
			return false;
		} else
		{
			hasPiston = true;
			return true;
		}
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound)
	{
		compound.setInteger("workLeft", workLeft);
		compound.setBoolean("hasWater", hasWater);
		compound.setBoolean("hasGold", hasGold);
		compound.setBoolean("hasPiston", hasPiston);
		compound.setBoolean("hasWood", hasWood);
		return super.writeToNBT(compound);
	}

	@Override
	public void readFromNBT(NBTTagCompound compound)
	{
		workLeft = compound.getInteger("workLeft");
		hasWater = compound.getBoolean("hasWater");
		hasWood = compound.getBoolean("hasWood");
		hasPiston = compound.getBoolean("hasPiston");
		hasGold = compound.getBoolean("hasGold");
		super.readFromNBT(compound);
	}

	// TODO: 11/19/2016 Make this functional
	// TODO: 11/19/2016 Make this be used to make machine blocks
	// TODO: 11/19/2016 add recipe that includes quartz and lapis
	// TODO: 11/19/2016 make the rod emit lots of end rod effects that change colour
}
