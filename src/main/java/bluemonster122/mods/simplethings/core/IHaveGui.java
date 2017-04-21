package bluemonster122.mods.simplethings.core;

import net.minecraft.client.gui.Gui;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IHaveGui {
    Gui createGui(InventoryPlayer player, World world, BlockPos pos);

    Container createContainer(InventoryPlayer player, World world, BlockPos pos);
}
