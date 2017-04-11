package bluemonster122.mods.simplethings.handler;

import bluemonster122.mods.simplethings.treefarm.ContainerTreeFarm;
import bluemonster122.mods.simplethings.treefarm.GuiTreeFarm;
import bluemonster122.mods.simplethings.treefarm.TileTreeFarm;
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
        switch (ID) {
            case tree_farm_gui_id:
                TileEntity tile = world.getTileEntity(new BlockPos(x, y, z));
                if (tile instanceof TileTreeFarm) {
                    return new ContainerTreeFarm(player, (TileTreeFarm) tile);
                }
                throw new IllegalStateException(
                        "Cannot open tree farm gui, if tree farm is not at location of tree farm block.");
            default:
                return null;
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        switch (ID) {
            case tree_farm_gui_id:
                TileEntity tile = world.getTileEntity(new BlockPos(x, y, z));
                if (tile instanceof TileTreeFarm) {
                    return new GuiTreeFarm(player, (TileTreeFarm) tile);
                }
                throw new IllegalStateException(
                        "Cannot open tree farm gui, if tree farm is not at location of tree farm block.");
            default:
                return null;
        }
    }
}
