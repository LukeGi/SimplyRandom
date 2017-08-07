package bluemonster122.mods.simplerandomstuff.core.block;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemStackHandler;

public interface IHaveInventory
        extends IInventory {

    /**
     * Gets the Tile's current Inventory.
     *
     * @return The Tile's current Inventory.
     */
    ItemStackHandler getInventory();

    /**
     * Sets the given ItemStackHandler to be the Tile's Inventory.
     *
     * @param inventory new Inventory.
     */
    void setInventory(ItemStackHandler inventory);

    /**
     * Creates a new Inventory for the Tile.
     *
     * @return a new Inventory for the Tile.
     */
    ItemStackHandler createInventory();

    /**
     * Gets the name of the TileEntity
     *
     * @return the name of the tile in a String.
     */
    String getName();

    @Override
    default boolean hasCustomName() {
        return false;
    }

    @Override
    default ITextComponent getDisplayName() {
        return new TextComponentString(getName());
    }

    @Override
    default int getSizeInventory() {
        return getInventory().getSlots();
    }

    @Override
    default boolean isEmpty() {
        boolean flag = true;
        for (int i = 0; i < getSizeInventory() && flag; i++) {
            flag = getStackInSlot(i) == ItemStack.EMPTY;
        }
        return flag;
    }

    @Override
    default ItemStack getStackInSlot(int index) {
        return getInventory().getStackInSlot(index);
    }

    @Override
    default ItemStack decrStackSize(int index, int count) {
        ItemStack stackInSlot = getStackInSlot(index);
        if (stackInSlot.getCount() > count) {
            stackInSlot.splitStack(count);
            return stackInSlot;
        } else {
            return stackInSlot;
        }
    }

    @Override
    default ItemStack removeStackFromSlot(int index) {
        ItemStack stack = getStackInSlot(index);
        getInventory().setStackInSlot(index, ItemStack.EMPTY);
        return stack;
    }

    @Override
    default void setInventorySlotContents(int index, ItemStack stack) {
        getInventory().setStackInSlot(index, stack);
    }

    @Override
    default int getInventoryStackLimit() {
        return 64;
    }

    @Override
    void markDirty();

    @Override
    default boolean isUsableByPlayer(EntityPlayer player) {
        return true;
    }

    @Override
    default void openInventory(EntityPlayer player) {
    }

    @Override
    default void closeInventory(EntityPlayer player) {
    }

    @Override
    default boolean isItemValidForSlot(int index, ItemStack stack) {
        return getInventory().insertItem(index, stack, true) != stack;
    }

    @Override
    default int getField(int id) {
        return 0;
    }

    @Override
    default void setField(int id, int value) {
    }

    @Override
    default int getFieldCount() {
        return 0;
    }

    @Override
    default void clear() {
        for (int i = 0; i < getSizeInventory(); i++) {
            removeStackFromSlot(i);
        }
    }

    /**
     * This method will drop the machines contents on the ground.
     *
     * @param world World in which to drop the contents.
     * @param pos   Position at which to drop the contents.
     */
    default void dropContents(World world, BlockPos pos) {
        double x = pos.getX() + 0.5;
        double y = pos.getY() + 0.5;
        double z = pos.getZ() + 0.5;
        for (int i = 0; i < getSizeInventory(); i++) {
            world.spawnEntity(new EntityItem(world, x, y, z, removeStackFromSlot(i)));
        }
    }
}
