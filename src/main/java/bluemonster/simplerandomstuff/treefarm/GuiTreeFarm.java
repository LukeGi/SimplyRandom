package bluemonster.simplerandomstuff.treefarm;

import bluemonster.simplerandomstuff.reference.ModInfo;
import com.google.common.collect.ImmutableList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.IOException;

@SideOnly(Side.CLIENT)
public class GuiTreeFarm
        extends GuiContainer {
    ResourceLocation TREE_FARM_GUI = new ResourceLocation(ModInfo.MOD_ID, "textures/gui/treefarm.png");

    InventoryPlayer playerInventory;

    TileTreeFarm tile;

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        super.drawGuiContainerForegroundLayer(mouseX, mouseY);
        if (isPointInRegion(121, 6, 106, 10, mouseX, mouseY)) {
            IEnergyStorage battery = tile.getCapability(CapabilityEnergy.ENERGY, null);
            drawHoveringText(
                    ImmutableList.of(battery.getEnergyStored() + " / " + battery.getMaxEnergyStored() + " " + "RF"),
                    0,
                    0
            );
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        Minecraft.getMinecraft()
                .getTextureManager()
                .bindTexture(TREE_FARM_GUI);
        drawTexturedModalRect(getGuiLeft(), getGuiTop(), 0, 0, xSize, ySize);
        IEnergyStorage battery = tile.getCapability(CapabilityEnergy.ENERGY, EnumFacing.DOWN);
        int energyPercent = (int) ((float) battery.getEnergyStored() / (float) battery.getMaxEnergyStored() * 106);
        drawTexturedModalRect(getGuiLeft() + 107, getGuiTop() + 5, 17, 214, energyPercent, 10);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        super.actionPerformed(button);
    }

    public GuiTreeFarm(InventoryPlayer playerInventory, TileTreeFarm treefarm) {
        super(new ContainerTreeFarm(playerInventory, treefarm));
        this.xSize = 234;
        this.ySize = 214;
        this.playerInventory = playerInventory;
        this.tile = treefarm;
    }
}
