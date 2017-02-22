package bluemonster122.simplethings;

import bluemonster122.simplethings.handler.ConfigurationHandler;
import bluemonster122.simplethings.handler.GuiHandler;
import bluemonster122.simplethings.handler.RegistryHandler;
import bluemonster122.simplethings.util.IInitialize;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
@Mod(modid = SimpleThings.MOD_ID, name = SimpleThings.MOD_NAME, version = SimpleThings.VERSION, guiFactory = SimpleThings.GUI_FACTORY_CLASS, updateJSON = SimpleThings.UPDATE_JSON)
public class SimpleThings implements IInitialize
{
	public static final String MOD_ID = "simplethings";
	public static final String MOD_NAME = "Simple Things";
	public static final String VERSION = "1.0.0";
	public static final String GUI_FACTORY_CLASS = "bluemonster122.simplethings.client.gui.GuiFactory";
	public static final String UPDATE_JSON = "https://github.com/bluemonster122/SimpleThings/blob/master/update.json";
	@Instance(MOD_ID)
	public static SimpleThings INSTANCE;
	public static CreativeTabs theTab = new CreativeTabs(MOD_ID)
	{
		@Override
		public ItemStack getTabIconItem()
		{
			return ItemStack.field_190927_a;
		}
	};
	
	@Override
	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		ConfigurationHandler.init(event.getSuggestedConfigurationFile());
		RegistryHandler.registerEvents();
	}
	
	@Override
	@EventHandler
	public void init(FMLInitializationEvent event)
	{
		NetworkRegistry.INSTANCE.registerGuiHandler(SimpleThings.INSTANCE, new GuiHandler());
		RegistryHandler.registerTileEntities();
		RegistryHandler.registerRecipes();
	}
	
	@Override
	@EventHandler
	public void postInit(FMLPostInitializationEvent event)
	{
	}
}
