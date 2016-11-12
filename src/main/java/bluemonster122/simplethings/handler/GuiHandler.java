package bluemonster122.simplethings.handler;

import net.minecraft.client.gui.Gui;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

public class GuiHandler implements IGuiHandler
{
    private static int ID = 0;
    public static final int FARM = 0;
    private static List<GuiInfo> guis = new ArrayList<>();

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
    {
        for (GuiInfo gui : guis)
        {
            if (gui.isModule_loaded() && ID == gui.getGui_id())
            {
                return gui.getNewContainer(player, world.getTileEntity(new BlockPos(x, y, z)));
            }
        }
        return null;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
    {
        for (GuiInfo gui : guis)
        {
            if (gui.isModule_loaded() && ID == gui.getGui_id())
            {
                return gui.getNewGui(player, world.getTileEntity(new BlockPos(x, y, z)));
            }
        }
        return null;
    }

    public static GuiInfo addGui(String name, BiFunction<EntityPlayer, TileEntity, Container> container, BiFunction<EntityPlayer, TileEntity, Gui> gui)
    {
        GuiInfo newGui = new GuiHandler.GuiInfo(name, GuiHandler.getNextID(), true, container, gui);
        addNewGui(newGui);
        return newGui;
    }

    private static boolean addNewGui(GuiInfo gui)
    {
        return guis.add(gui);
    }

    private static int getNextID()
    {
        return ID++;
    }

    public static class GuiInfo
    {
        private String gui_name;
        private int gui_id;
        private boolean module_loaded;
        private BiFunction<EntityPlayer, TileEntity, Container> container;
        private BiFunction<EntityPlayer, TileEntity, Gui> gui;

        public GuiInfo(String gui_name, int gui_id, boolean module_loaded, BiFunction<EntityPlayer, TileEntity, Container> container, BiFunction<EntityPlayer, TileEntity, Gui> gui)
        {
            this.gui_name = gui_name;
            this.gui_id = gui_id;
            this.module_loaded = module_loaded;
            this.container = container;
            this.gui = gui;
        }

        public String getGui_name()
        {
            return gui_name;
        }

        public void setGui_name(String gui_name)
        {
            this.gui_name = gui_name;
        }

        public int getGui_id()
        {
            return gui_id;
        }

        public void setGui_id(int gui_id)
        {
            this.gui_id = gui_id;
        }

        public boolean isModule_loaded()
        {
            return module_loaded;
        }

        public void setModule_loaded(boolean module_loaded)
        {
            this.module_loaded = module_loaded;
        }

        public Container getNewContainer(EntityPlayer player, TileEntity tileEntity)
        {
            return container.apply(player, tileEntity);
        }

        public Gui getNewGui(EntityPlayer player, TileEntity tileEntity)
        {
            return gui.apply(player, tileEntity);
        }
    }
}
