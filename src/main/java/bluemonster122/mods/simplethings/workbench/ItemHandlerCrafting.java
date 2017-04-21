package bluemonster122.mods.simplethings.workbench;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;

public class ItemHandlerCrafting extends ItemStackHandler {
    public ItemHandlerCrafting(int size) {
        super(size);
    }

    @Nonnull
    @Override
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
        if (slot == getSlots() - 1) return stack;
        if (getStackInSlot(slot) == ItemStack.EMPTY) return stack;
        return super.insertItem(slot, stack, simulate);
    }

    @Nonnull
    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        if (slot != getSlots() - 1) return ItemStack.EMPTY;
        return super.extractItem(slot, amount, simulate);
    }
}
