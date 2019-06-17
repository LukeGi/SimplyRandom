package lhg.forgemods.simplyrandom.cobblemaker;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;

/**
 * Cobblestone Maker Inventory, this exists so as much cobble can be extracted as anyone would like.
 */
public class CobblestoneMakerInventory implements IItemHandler
{
    private CobblestoneMakerTileEntity parent;

    public CobblestoneMakerInventory(CobblestoneMakerTileEntity parent)
    {
        this.parent = parent;
    }

    @Override
    public int getSlots()
    {
        return 1;
    }

    @Nonnull
    @Override
    public ItemStack getStackInSlot(int slot)
    {
        return new ItemStack(Items.COBBLESTONE, parent.getCobbleCount());
    }

    @Nonnull
    @Override
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate)
    {
        return stack;
    }

    @Nonnull
    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate)
    {
        if (slot == 0)
        {
            int extracted = Math.min(amount, parent.getCobbleCount());
            if (!simulate)
            {
                parent.consume(extracted);
            }
            return new ItemStack(Items.COBBLESTONE, extracted);
        }
        return ItemStack.EMPTY;
    }

    @Override
    public int getSlotLimit(int slot)
    {
        return 0;
    }

    @Override
    public boolean isItemValid(int slot, @Nonnull ItemStack stack)
    {
        return false;
    }
}
