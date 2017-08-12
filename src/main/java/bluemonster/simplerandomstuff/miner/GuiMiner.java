package bluemonster.simplerandomstuff.miner;

import bluemonster.simplerandomstuff.reference.ModInfo;
import com.google.common.collect.ImmutableList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.IOException;

@SideOnly(Side.CLIENT)
public class GuiMiner
        extends GuiContainer {
    private ResourceLocation MINER_GUI = new ResourceLocation(ModInfo.MOD_ID, "textures/gui/miner.png");

    private TileMiner tile;

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        super.drawGuiContainerForegroundLayer(mouseX, mouseY);
        if (isPointInRegion(9, 72, 162, 7, mouseX, mouseY)) {
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
                .bindTexture(MINER_GUI);
        drawTexturedModalRect(getGuiLeft(), getGuiTop(), 0, 0, xSize, ySize);
        IEnergyStorage battery = tile.getCapability(CapabilityEnergy.ENERGY, null);
        int energyPercent = (int) ((float) battery.getEnergyStored() / (float) battery.getMaxEnergyStored() * 160);
        System.out.println(energyPercent);
        drawTexturedModalRect(getGuiLeft() + 10, getGuiTop() + 73, 10, 171, energyPercent, 5);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        super.actionPerformed(button);
    }

    public GuiMiner(InventoryPlayer playerInventory, TileMiner tileEntity) {
        super(new ContainerMiner(playerInventory, tileEntity));
        this.tile = tileEntity;
        this.xSize = 181;
        this.ySize = 171;
    }
}
