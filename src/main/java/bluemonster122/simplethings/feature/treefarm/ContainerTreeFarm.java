package bluemonster122.simplethings.feature.treefarm;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerTreeFarm extends Container {

    private EntityPlayer player;
    private TileEntityTreeFarm tileEntity;

    public ContainerTreeFarm(EntityPlayer player, TileEntityTreeFarm tileEntity) {
        this.player = player;
        this.tileEntity = tileEntity;
        int i;
        int j;

        // ME

        IItemHandler inventory = tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.DOWN);
        for (j = 0; j < 6; j++) {
            for (i = 0; i < 12; i++) {
                this.addSlotToContainer(new SlotItemHandler(inventory, j * 12 + i, i * 18 + 10, j * 18 + 18));
            }
        }

        // VANILLA

        for (i = 0; i < 3; ++i) {
            for (j = 0; j < 9; ++j) {
                this.addSlotToContainer(new Slot(player.inventory, j + i * 9 + 9, 37 + j * 18, 132 + i * 18));
            }
        }

        for (i = 0; i < 9; ++i) {
            this.addSlotToContainer(new Slot(player.inventory, i, 37 + i * 18, 190));
        }
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int fromSlot) {
        ItemStack previous = null;
        Slot slot = this.inventorySlots.get(fromSlot);

        if (slot != null && slot.getHasStack()) {
            ItemStack current = slot.getStack();
            previous = current.copy();

            if (fromSlot < 72) {
                // From TE Inventory to Player Inventory
                if (!this.mergeItemStack(current, 72, 108, true))
                    return null;
            } else {
                // From Player Inventory to TE Inventory
                if (!this.mergeItemStack(current, 0, 72, false))
                    return null;
            }

            if (current.stackSize == 0)
                slot.putStack(null);
            else
                slot.onSlotChanged();

            if (current.stackSize == previous.stackSize)
                return null;
            slot.onPickupFromSlot(playerIn, current);
        }
        return previous;
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return player.getPosition().distanceSq(tileEntity.getPos()) < 100;
    }


}
