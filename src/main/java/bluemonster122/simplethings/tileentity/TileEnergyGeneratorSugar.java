package bluemonster122.simplethings.tileentity;

import bluemonster122.simplethings.util.CapabilityHelper;
import bluemonster122.simplethings.util.EnergyHelper;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;

import static bluemonster122.simplethings.handler.ConfigurationHandler.energy_from_sugar;
import static bluemonster122.simplethings.handler.ConfigurationHandler.sugar_burn_time;
public class TileEnergyGeneratorSugar extends TileEntity implements ITickable, IEnergyStorage, IItemHandler
{
	private ItemStackHandler inventory = new ItemStackHandler(1);
	private EnergyStorage energy = new EnergyStorage(10000);
	private int burntime;
	
	@Override
	public void update()
	{
		// If we have nothing left to burn
		if (burntime == 0)
		{
			// If we have enough spare room in the generator's buffer to burn a sugar without waste
			if (getEnergyStored() + energy_from_sugar <= getMaxEnergyStored())
			{
				// If the inventory contains a stack of sugar.
				if (getStackInSlot(0).getItem().equals(Items.SUGAR))
				{
					// We take a single sugar from the stack.
					extractItem(0, 1, false);
					// Adds to the burntime
					burntime = sugar_burn_time;
				}
			}
		} // or if we are still burning sugar
		else if (burntime > 0)
		{
			// burn some sugar
			burntime--;
			// add the things created to the battery
			receiveEnergy(energy_from_sugar / sugar_burn_time, false);
		} // or if for some reason the burnime is negative
		else
		{
			// set the burntime to be 0, for the sugar checks.
			burntime = 0;
		}
		if (getEnergyStored() > 0)
		{
			EnergyHelper.checkForTakers(this, getWorld(), getPos());
		}
		// TODO: 11/19/2016 make it require an air block on 3 sides, and 15 air blocks in a 2 block radius.
		// TODO: 11/19/2016 make it emit a lot of smoke particles if not enough air
		// TODO: 11/19/2016 make it emit flames, ender and smoke particles
	}
	
	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing)
	{
		return CapabilityHelper.hasCapability(this, capability);
	}
	
	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing)
	{
		return CapabilityHelper.getCapability(this, capability);
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound)
	{
		compound.setInteger("[burntime]", burntime);
		compound.setInteger("[things]", energy.getEnergyStored());
		compound.setTag("[inventory]", inventory.serializeNBT());
		return compound;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound)
	{
		burntime = compound.getInteger("[burntime]");
		inventory.deserializeNBT((NBTTagCompound) compound.getTag("[inventory]"));
		energy = EnergyHelper.makeNewAndFill(compound.getInteger("[things]"), 10000, 10000, 10000);
	}
	
	@Override
	public int receiveEnergy(int maxReceive, boolean simulate)
	{
		return getEnergy().receiveEnergy(maxReceive, simulate);
	}
	
	@Override
	public int extractEnergy(int maxExtract, boolean simulate)
	{
		return 0;
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
	
	@Override
	public int getSlots()
	{
		return getInventory().getSlots();
	}
	
	@Nonnull
	@Override
	public ItemStack getStackInSlot(int slot)
	{
		return getInventory().getStackInSlot(slot);
	}
	
	@Nonnull
	@Override
	public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate)
	{
		return getInventory().insertItem(slot, stack, simulate);
	}
	
	@Nonnull
	@Override
	public ItemStack extractItem(int slot, int amount, boolean simulate)
	{
		return getInventory().extractItem(slot, amount, simulate);
	}
	
	public ItemStackHandler getInventory()
	{
		return inventory;
	}
	
	public EnergyStorage getEnergy()
	{
		return energy;
	}
}
