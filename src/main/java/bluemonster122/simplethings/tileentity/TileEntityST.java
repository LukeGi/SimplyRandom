package bluemonster122.simplethings.tileentity;

import bluemonster122.simplethings.tileentity.things.IHaveBattery;
import bluemonster122.simplethings.tileentity.things.IHaveInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TileEntityST extends TileEntity
{
	public void dropInventory()
	{
		if (getWorld().isRemote)
		{
			return;
		}
		if (this instanceof IHaveInventory)
		{
			for (int i = 0; i < ((IHaveInventory) this).getInventory().getSlots(); ++i)
			{
				ItemStack itemstack = ((IHaveInventory) this).getInventory().getStackInSlot(i);
				if (itemstack != ItemStack.EMPTY)
				{
					InventoryHelper.spawnItemStack(
					  getWorld(), getPos().getX(), getPos().getY(), getPos().getZ(), itemstack);
				}
			}
		}
	}

	protected void sendUpdate()
	{
		getWorld().notifyBlockUpdate(
		  getPos(), getWorld().getBlockState(getPos()), getWorld().getBlockState(getPos()), 3);
	}

	@Override
	public SPacketUpdateTileEntity getUpdatePacket()
	{
		NBTTagCompound nbt = new NBTTagCompound();
		this.writeToNBT(nbt);
		return new SPacketUpdateTileEntity(getPos(), 0, nbt);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt)
	{
		this.readFromNBT(pkt.getNbtCompound());
	}

	@Override
	@Nonnull
	public NBTTagCompound getUpdateTag()
	{
		NBTTagCompound nbt = super.getUpdateTag();
		writeToNBT(nbt);
		return nbt;
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound)
	{
		if (this instanceof IHaveBattery)
		{
			compound.setInteger("powerStored", ((IHaveBattery) this).getBattery().getEnergyStored());
		}
		if (this instanceof IHaveInventory)
		{
			compound.setTag("inventory", ((IHaveInventory) this).getInventory().serializeNBT());
		}
		return super.writeToNBT(compound);
	}

	@Override
	public void readFromNBT(NBTTagCompound compound)
	{
		if (this instanceof IHaveBattery)
		{
			EnergyStorage battery = ((IHaveBattery) this).getBattery();
			battery.extractEnergy(battery.getEnergyStored(), false);
			battery.receiveEnergy(compound.getInteger("powerStored"), false);
		}
		if (this instanceof IHaveInventory)
		{
			((IHaveInventory) this).getInventory().deserializeNBT(compound.getCompoundTag("inventory"));
		}
		super.readFromNBT(compound);
	}

	@Override
	public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing)
	{
		if (capability.equals(CapabilityEnergy.ENERGY))
		{
			return this instanceof IHaveBattery;
		}
		if (capability.equals(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY))
		{
			return this instanceof IHaveInventory;
		}
		return super.hasCapability(capability, facing);
	}

	@Nullable
	@Override
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing)
	{
		if (capability.equals(CapabilityEnergy.ENERGY))
		{
			return this instanceof IHaveBattery ? CapabilityEnergy.ENERGY.cast(
			  ((IHaveBattery) this).getBattery()) : null;
		}
		if (capability.equals(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY))
		{
			return this instanceof IHaveBattery ? CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(
			  ((IHaveInventory) this).getInventory()) : null;
		}
		return super.getCapability(capability, facing);
	}
}
