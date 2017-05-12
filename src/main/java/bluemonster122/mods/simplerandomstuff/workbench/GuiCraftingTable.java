package bluemonster122.mods.simplerandomstuff.workbench;

import bluemonster122.mods.simplerandomstuff.reference.ModInfo;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class GuiCraftingTable extends GuiContainer {

    private static final ResourceLocation CRAFTING_TABLE_GUI_TEXTURES = new ResourceLocation(ModInfo.MOD_ID, "textures/gui/crafting_table.png");

    private TileCraftingTable tile;

    public GuiCraftingTable(InventoryPlayer playerInv, World worldIn, BlockPos blockPosition, TileCraftingTable tile) {
        super(new ContainerCrafting(playerInv, tile));
        this.tile = tile;
    }

    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        String string = tile.getName();
        this.fontRendererObj.drawString(I18n.format(string), 28, 6, 4210752);
        this.fontRendererObj.drawString(I18n.format("container.inventory"), 8, this.ySize - 96 + 2, 4210752);
    }

    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(CRAFTING_TABLE_GUI_TEXTURES);
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(i, j, 0, 0, this.xSize, this.ySize);
    }

    public static class GuiCraftingTableAuto extends GuiCraftingTable {
        TileCraftingTable.TileCraftingTableAuto tile;

        public GuiCraftingTableAuto(InventoryPlayer playerInv, World worldIn, BlockPos blockPosition, TileCraftingTable.TileCraftingTableAuto tile) {
            super(playerInv, worldIn, blockPosition, tile);
            this.tile = tile;
        }

        @Override
        protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
            super.drawGuiContainerForegroundLayer(mouseX, mouseY);
            ItemStack stack = tile.inventory.getStackInSlot(9);
            if (stack != ItemStack.EMPTY) {
                String msg = "Stored: " + stack.getCount();
                int msgWidth = this.fontRendererObj.getStringWidth(msg);
                this.fontRendererObj.drawString(msg, 132 - msgWidth / 2, 65, 4210752);
            }
        }

        @Override
        protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            this.mc.getTextureManager().bindTexture(CRAFTING_TABLE_GUI_TEXTURES);
            int i = (this.width - this.xSize) / 2;
            int j = (this.height - this.ySize) / 2;
            this.drawTexturedModalRect(i, j, 0, 0, this.xSize, this.ySize);
            if (tile.workTime != -1) {
                int l = 24 - (int) (24 * (tile.workTime / 100f));
                this.drawTexturedModalRect(i + 89, j + 34, 176, 0, l, 17);
            }
        }
    }
}
