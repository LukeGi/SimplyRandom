package bluemonster122.mods.simplethings.handler;

import bluemonster122.mods.simplethings.core.IHaveGui;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GuiHandler implements IGuiHandler {
    public static final int tree_farm_gui_id = 0;

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        TileEntity tile = world.getTileEntity(new BlockPos(x, y, z));
        if (tile instanceof IHaveGui) {
            return ((IHaveGui) tile).createContainer(player.inventory, world, new BlockPos(x, y, z));
        }
        return null;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        TileEntity tile = world.getTileEntity(new BlockPos(x, y, z));
        if (tile instanceof IHaveGui) {
            return ((IHaveGui) tile).createGui(player.inventory, world, new BlockPos(x, y, z));
        }
        return null;
    }
}
