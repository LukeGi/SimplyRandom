package bluemonster122.simplethings.client.gui;

import bluemonster122.simplethings.SimpleThings;
import bluemonster122.simplethings.container.ContainerTreeFarm;
import bluemonster122.simplethings.tileentity.TileTreeFarm;
import com.google.common.collect.ImmutableList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.IOException;

@SideOnly(Side.CLIENT)
public class GuiTreeFarm extends GuiContainer
{

    ResourceLocation TREE_FARM_GUI = new ResourceLocation(SimpleThings.MOD_ID, "textures/gui/treefarm.png");
    EntityPlayer player;
    TileTreeFarm tile;

    public GuiTreeFarm(EntityPlayer player, TileTreeFarm treefarm)
    {
        super(new ContainerTreeFarm(player, treefarm));
        this.xSize = 234;
        this.ySize = 214;
        this.player = player;
        this.tile = treefarm;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
    {
        Minecraft.getMinecraft().getTextureManager().bindTexture(TREE_FARM_GUI);
        int left = (width - xSize) / 2;
        int top = (height - ySize) / 2;
        drawTexturedModalRect(left, top, 0, 0, xSize, ySize);
        IEnergyStorage battery = tile.getCapability(CapabilityEnergy.ENERGY, EnumFacing.DOWN);

        int energyPercent = (int) ((float) battery.getEnergyStored() / (float) battery.getMaxEnergyStored() * 106);
        drawTexturedModalRect(left + 226 - energyPercent, top + 5, 136 - energyPercent, 214, energyPercent, 10);

    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
    {
        super.drawGuiContainerForegroundLayer(mouseX, mouseY);
        if (isPointInRegion(121, 6, 106, 10, mouseX, mouseY))
        {
            IEnergyStorage battery = tile.getCapability(CapabilityEnergy.ENERGY, EnumFacing.DOWN);
            drawHoveringText(ImmutableList.of(battery.getEnergyStored() + " / " + battery.getMaxEnergyStored() + " " + "Forge Units"), 0, 0);
        }
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException
    {
        super.actionPerformed(button);
    }
}
