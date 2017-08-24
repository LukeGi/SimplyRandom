package bluemonster.simplerandomstuff.util;

import net.minecraft.client.gui.Gui;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public interface IGuiProvider {

    Container createContainer(InventoryPlayer inventory);

    @SideOnly(Side.CLIENT)
    Gui createGui(InventoryPlayer inventory);
}
