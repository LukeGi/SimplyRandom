package bluemonster122.simplethings.tileentity;

import bluemonster122.simplethings.handler.ConfigurationHandler;
import bluemonster122.simplethings.util.CapabilityHelper;
import cofh.api.energy.EnergyStorage;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;

public class TileCobblestoneGenerator extends TileEntity implements ITickable, IItemHandler, IEnergyStorage
{
    private ItemStackHandler inventory = new ItemStackHandler(1);
    private EnergyStorage energy = new EnergyStorage(1000);

    @Override
    public void update()
    {
        ItemStack stackInSlot = getStackInSlot(0);
        int stackSize = stackInSlot.func_190916_E();
        if (stackSize < 64)
        {
            int spaceLeft = 64 - stackSize;
            while (ConfigurationHandler.cobblestone_generator_req_power * spaceLeft > getEnergyStored())
            {
                spaceLeft--;
            }
            ItemHandlerHelper.insertItem(this, new ItemStack(Blocks.COBBLESTONE, spaceLeft), false);
            extractEnergy(spaceLeft, false);
        }
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
    public int receiveEnergy(int maxReceive, boolean simulate)
    {
        return getEnergy().receiveEnergy(maxReceive, simulate);
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate)
    {
        return getEnergy().extractEnergy(maxExtract, simulate);
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
        return false;
    }

    @Override
    public boolean canReceive()
    {
        return true;
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
