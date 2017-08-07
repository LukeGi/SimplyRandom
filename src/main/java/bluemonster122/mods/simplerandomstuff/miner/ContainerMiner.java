package bluemonster122.mods.simplerandomstuff.miner;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerMiner
  extends Container
{
  private TileMiner tile;
  
  public ContainerMiner(InventoryPlayer playerInventory, TileMiner tileEntity)
  {
    tile = tileEntity;
    int i, j;
    
    // TILE
    IItemHandler inventory = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.DOWN);
    for (j = 0; j < 3; j++)
    {
      for (i = 0; i < 9; i++)
      {
        this.addSlotToContainer(new SlotItemHandler(inventory, j * 9 + i, i * 18 + 10, j * 18 + 16));
      }
    }
    
    // VANILLA
    for (i = 0; i < 3; ++i)
    {
      for (j = 0; j < 9; ++j)
      {
        this.addSlotToContainer(new Slot(playerInventory, j + i * 9 + 9, 10 + j * 18, 92 + i * 18));
      }
    }
    for (i = 0; i < 9; ++i)
    {
      this.addSlotToContainer(new Slot(playerInventory, i, 10 + i * 18, 149));
    }
  }
  
  @Override
  public boolean canInteractWith(EntityPlayer playerIn)
  {
    return true;
  }
}
