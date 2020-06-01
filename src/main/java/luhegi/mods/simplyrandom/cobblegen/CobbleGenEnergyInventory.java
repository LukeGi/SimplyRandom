package luhegi.mods.simplyrandom.cobblegen;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.Tags;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.IItemHandlerModifiable;

import javax.annotation.Nonnull;
import java.util.function.IntConsumer;

public class CobbleGenEnergyInventory implements IItemHandlerModifiable, IEnergyStorage {
    private static final ItemStack COBBLE_STACK = new ItemStack(Items.COBBLESTONE, 1);
    private final IntConsumer changeCallback;
    private int energy;

    public CobbleGenEnergyInventory(IntConsumer changeCallback) {
        this.changeCallback = changeCallback;
    }

    @Override
    public int getSlots() {
        return 8;
    }

    @Nonnull
    @Override
    public ItemStack getStackInSlot(int slot) {
        int cobbleLeft = getCobbleLeft();
        if (cobbleLeft == -1) {
            cobbleLeft = COBBLE_STACK.getMaxStackSize();
        }
        if (cobbleLeft > 0) {
            ItemStack stack = COBBLE_STACK.copy();
            stack.setCount(cobbleLeft);
            return stack;
        }
        return ItemStack.EMPTY;
    }

    @Nonnull
    @Override
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
        if (Tags.Items.COBBLESTONE.contains(stack.getItem())) {
            if (!simulate)
                generateCobble(-stack.getCount());
            return ItemStack.EMPTY;
        }
        return stack;
    }

    @Nonnull
    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        ItemStack stack = getStackInSlot(slot);
        stack.setCount(Math.min(amount, stack.getCount()));
        if (!simulate)
            generateCobble(stack.getCount());
        return stack;
    }

    private void generateCobble(int amount) {
        if (CobblestoneGenerator.getUseEnergy()) {
            energy -= CobblestoneGenerator.getEnergyPerCobble();
        }
        changeCallback.accept(amount);
    }

    @Override
    public int getSlotLimit(int slot) {
        return COBBLE_STACK.getMaxStackSize();
    }

    @Override
    public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
        return Tags.Items.COBBLESTONE.contains(stack.getItem());
    }

    @Override
    public void setStackInSlot(int slot, @Nonnull ItemStack stack) {
        // NOOP
    }

    public int getCobbleLeft() {
        return CobblestoneGenerator.getUseEnergy() ?
                (getEnergyStored() / CobblestoneGenerator.getEnergyPerCobble()) :
                -1;
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        int spaceLeft = getMaxEnergyStored() - getEnergyStored();
        int accepted = Math.min(maxReceive, spaceLeft);
        if (!simulate) {
            energy += accepted;
        }
        return accepted;
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        return 0;
    }

    @Override
    public int getEnergyStored() {
        return energy;
    }

    @Override
    public int getMaxEnergyStored() {
        return CobblestoneGenerator.getMaxEnergyStorage();
    }

    @Override
    public boolean canExtract() {
        return false;
    }

    @Override
    public boolean canReceive() {
        return true;
    }

    public void write(CompoundNBT compound) {
        compound.putInt("CGEIenergy", energy);
    }

    public void read(CompoundNBT compound) {
        energy = compound.getInt("CGEIenergy");
    }
}
