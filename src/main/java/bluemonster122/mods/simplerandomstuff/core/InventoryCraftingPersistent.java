package bluemonster122.mods.simplerandomstuff.core;

import bluemonster122.mods.simplerandomstuff.SimpleRandomStuff;
import bluemonster122.mods.simplerandomstuff.core.network.MessageCraftingSync;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;

public class InventoryCraftingPersistent extends InventoryCrafting {

    private final int length;
    private final Container eventHandler;
    private final IInventory parent;

    public InventoryCraftingPersistent(Container eventHandler, IInventory parent, int width, int height) {
        super(eventHandler, width, height);
        int k = width * height;

        assert (k == parent.getSizeInventory());

        this.parent = parent;
        this.length = k;
        this.eventHandler = eventHandler;
    }

    @Override
    public int getSizeInventory( ) {
        return this.length;
    }

    @Override
    public ItemStack getStackInSlot(int index) {
        return index >= this.getSizeInventory() ? null : this.parent.getStackInSlot(index);
    }

    @Override
    public boolean hasCustomName( ) {
        return false;
    }

    @Override
    public ItemStack decrStackSize(int index, int count) {
        if (this.getStackInSlot(index) != ItemStack.EMPTY) {
            ItemStack itemstack;

            if (this.getStackInSlot(index).getCount() <= count) {
                itemstack = this.getStackInSlot(index);
                this.setInventorySlotContents(index, ItemStack.EMPTY);
                this.eventHandler.onCraftMatrixChanged(this);
                return itemstack;
            } else {
                itemstack = this.getStackInSlot(index).splitStack(count);

                if (this.getStackInSlot(index).getCount() == 0) {
                    this.setInventorySlotContents(index, ItemStack.EMPTY);
                }

                this.eventHandler.onCraftMatrixChanged(this);
                return itemstack;
            }
        } else {
            return ItemStack.EMPTY;
        }
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        this.parent.setInventorySlotContents(index, stack);
        this.eventHandler.onCraftMatrixChanged(this);
    }

    @Override
    public void markDirty( ) {
        this.parent.markDirty();
        this.eventHandler.onCraftMatrixChanged(this);

        SimpleRandomStuff.INSTANCE.channel.sendToServer(new MessageCraftingSync());
    }

    @Override
    public void clear( ) {
        /* NO OPERATION */
    }
}
