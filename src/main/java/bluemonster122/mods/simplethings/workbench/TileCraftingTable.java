package bluemonster122.mods.simplethings.workbench;

import bluemonster122.mods.simplethings.core.IHaveGui;
import bluemonster122.mods.simplethings.core.block.IHaveInventory;
import bluemonster122.mods.simplethings.core.block.TileST;
import com.google.common.collect.ImmutableMap;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import java.util.List;
import java.util.Map;

public class TileCraftingTable extends TileST implements IHaveInventory, IHaveGui {
    public ItemStackHandler inventory = createInventory();

    @Override
    public Map<Capability, Capability> getCaps( ) {
        return ImmutableMap.of(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(inventory));
    }

    @Override
    public NBTTagCompound writeChild(NBTTagCompound tag) {
        return tag;
    }

    @Override
    public NBTTagCompound readChild(NBTTagCompound tag) {
        return tag;
    }

    @Override
    public ItemStackHandler getInventory( ) {
        return inventory;
    }

    @Override
    public ItemStackHandler createInventory( ) {
        return new ItemHandlerCrafting(10) {
            @Override
            protected void onContentsChanged(int slot) {
                markDirty();
            }
        };
    }

    @Override
    public void setInventory(ItemStackHandler inventory) {
        this.inventory = inventory;
    }

    @Override
    public String getName( ) {
        return "simplethings:crafting_table";
    }

    @SideOnly(Side.CLIENT)
    @Override
    public Gui createGui(InventoryPlayer player, World world, BlockPos pos) {
        return new GuiCraftingTable(player, world, pos, this);
    }

    @Override
    public Container createContainer(InventoryPlayer player, World world, BlockPos pos) {
        return new ContainerCrafting(player, this);
    }

    public static class TileCraftingTableAuto extends TileCraftingTable implements ITickable {

        public int workTime;
        private IRecipe recipe;

        @Override
        public NBTTagCompound writeChild(NBTTagCompound tag) {
            tag.setInteger("work", workTime);
            return super.writeChild(tag);
        }

        @Override
        public NBTTagCompound readChild(NBTTagCompound tag) {
            workTime = tag.getInteger("work");
            return super.readChild(tag);
        }

        @SideOnly(Side.CLIENT)
        @Override
        public Gui createGui(InventoryPlayer player, World world, BlockPos pos) {
            return new GuiCraftingTable.GuiCraftingTableAuto(player, world, pos, this);
        }

        @Override
        public void update( ) {
            if (!getWorld().isRemote) {
                if (hasMoreThan1()) {
                    InventoryCrafting ic = createIC();
                    IRecipe newRecipe = getRecipe(ic);
                    if (newRecipe != null && canMerge(newRecipe.getRecipeOutput())) {
                        if (newRecipe != recipe) {
                            recipe = newRecipe;
                            workTime = 100;
                            return;
                        }
                        if (workTime >= 0) {
                            workTime--;
                        } else {
                            // Handle output
                            ItemStack result = recipe.getCraftingResult(ic);
                            if (canMerge(recipe.getRecipeOutput())) result.grow(inventory.getStackInSlot(9).getCount());
                            inventory.setStackInSlot(9, result);

                            // Handle inputs
                            for (int i = 0; i < 9; i++) {
                                ItemStack stack = inventory.getStackInSlot(i);
                                if (stack.isEmpty()) continue;
                                if (stack.getItem().hasContainerItem(stack)) {
                                    ItemStack container = stack.getItem().getContainerItem(stack);
                                    inventory.setStackInSlot(i, container);
                                } else {
                                    stack.shrink(1);
                                    inventory.setStackInSlot(i, stack);
                                }
                            }
                            recipe = null;
                        }
                    }
                } else {
                    recipe = null;
                    workTime = -1;
                }
                balanceSlots();
            }
            sendUpdate();
        }

        private void balanceSlots( ) {
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    if (i != j) {
                        ItemStack o = getStackInSlot(i);
                        ItemStack t = getStackInSlot(j);
                        if (ItemStack.areItemsEqual(o, t) && o.getCount() > t.getCount() && o.getCount() - t.getCount() > 1) {
                            int toMove = Math.floorDiv(o.getCount() - t.getCount(), 2);
                            o.shrink(toMove);
                            t.grow(toMove);
                        }
                    }
                }
            }
        }

        private boolean canMerge(ItemStack stack) {
            ItemStack stackInSlot = inventory.getStackInSlot(9);
            if (stackInSlot.equals(ItemStack.EMPTY)) return true;
            return stack.getItem() == stackInSlot.getItem() && stack.getCount() + stackInSlot.getCount() <= inventory.getSlotLimit(9);
        }

        private boolean hasMoreThan1( ) {
            boolean flag = true;
            for (int i = 0; i < inventory.getSlots() - 1; i++) {
                ItemStack stackInSlot = inventory.getStackInSlot(i);
                flag &= stackInSlot.getCount() > 1 || stackInSlot.equals(ItemStack.EMPTY);
            }
            return flag;
        }

        public IRecipe getRecipe(InventoryCrafting ic) {
            List<IRecipe> recipes = CraftingManager.getInstance().getRecipeList();
            for (IRecipe recipe : recipes) {
                if (recipe.matches(ic, getWorld())) {
                    return recipe;
                }
            }
            return null;
        }

        private InventoryCrafting createIC( ) {
            InventoryCrafting inventoryCrafting = new InventoryCrafting(new Container() {
                @Override
                public boolean canInteractWith(EntityPlayer playerIn) {
                    return false;
                }
            }, 3, 3);
            for (int i = 0; i < inventory.getSlots() - 1; i++) {
                inventoryCrafting.setInventorySlotContents(i, inventory.getStackInSlot(i));
            }
            return inventoryCrafting;
        }
    }
}
