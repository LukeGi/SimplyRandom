package bluemonster.simplerandomstuff.core;

import bluemonster.simplerandomstuff.config.Configs;
import bluemonster.simplerandomstuff.util.IGuiProvider;
import cofh.redstoneflux.api.IEnergyReceiver;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class TileCoreMachine extends TileCoreTicking implements IEnergyReceiver, IItemHandler, IGuiProvider {
    public Battery energyStorage = createBattery();
    public ItemStackHandler inventory = createInventory();

    protected abstract Battery createBattery();

    protected abstract ItemStackHandler createInventory();

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        writeToNBTLight(compound);
        return super.writeToNBT(compound);
    }

    public NBTTagCompound writeToNBTLight(NBTTagCompound compound) {
        energyStorage.writeToNBT(compound);
        compound.setTag("[SimpleRandomStuff]inv", inventory.serializeNBT());
        return compound;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        readFromNBTLight(compound);
    }

    public void readFromNBTLight(NBTTagCompound compound) {
        energyStorage.readFromNBT(compound);
        if (compound.hasKey("[SimpleRandomStuff]inv"))
            inventory.deserializeNBT(compound.getCompoundTag("[SimpleRandomStuff]inv"));
    }

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
        if ((!Configs.CORE.onlyRF && capability.equals(CapabilityEnergy.ENERGY)) ||
                capability.equals(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY))
            return true;
        return super.hasCapability(capability, facing);
    }

    @Nullable
    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
        if (!Configs.CORE.onlyRF && capability.equals(CapabilityEnergy.ENERGY))
            return CapabilityEnergy.ENERGY.cast(energyStorage);
        else if (capability.equals(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY))
            return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(inventory);
        return super.getCapability(capability, facing);
    }

    @Override
    public int receiveEnergy(EnumFacing from, int maxReceive, boolean simulate) {
        return energyStorage.receiveEnergy(maxReceive, simulate);
    }

    @Override
    public int getEnergyStored(EnumFacing from) {
        return energyStorage.getEnergyStored();
    }

    @Override
    public int getMaxEnergyStored(EnumFacing from) {
        return energyStorage.getMaxEnergyStored();
    }

    @Override
    public boolean canConnectEnergy(EnumFacing from) {
        return true;
    }

    @Override
    public int getSlots() {
        return inventory.getSlots();
    }

    @Nonnull
    @Override
    public ItemStack getStackInSlot(int slot) {
        return inventory.getStackInSlot(slot);
    }

    @Nonnull
    @Override
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
        return inventory.insertItem(slot, stack, simulate);
    }

    @Nonnull
    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        return inventory.extractItem(slot, amount, simulate);
    }

    @Override
    public int getSlotLimit(int slot) {
        return inventory.getSlotLimit(slot);
    }
}
