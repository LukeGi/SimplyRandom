package bluemonster122.simplethings;

import bluemonster122.simplethings.feature.cobblegen.FeatureCobblestoneGenerator;
import bluemonster122.simplethings.feature.firegenerator.FeatureFireGenerator;
import bluemonster122.simplethings.feature.treefarm.FeatureTreeFarm;
import bluemonster122.simplethings.handler.ConfigurationHandler;
import bluemonster122.simplethings.handler.FeatureHandler;
import bluemonster122.simplethings.handler.GuiHandler;
import bluemonster122.simplethings.util.IInitialize;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;

@Mod(
        modid = SimpleThings.MOD_ID,
        name = SimpleThings.MOD_NAME,
        version = SimpleThings.VERSION,
        guiFactory = SimpleThings.GUI_FACTORY_CLASS
)
public class SimpleThings implements IInitialize
{
    public static final String MOD_ID = "simplethings";
    public static final String MOD_NAME = "Simple Things";
    public static final String VERSION = "1.0";
    public static final String GUI_FACTORY_CLASS = "bluemonster122.simplethings.client.gui.GuiFactory";

    @Instance(MOD_ID)
    public static SimpleThings INSTANCE;

    public static CreativeTabs theTab = new CreativeTabs(MOD_ID)
    {
        @Override
        public Item getTabIconItem()
        {
            return Item.getItemFromBlock(Blocks.FURNACE);
        }
    };

    static
    {
        FeatureHandler.addFeature(new FeatureTreeFarm());
        FeatureHandler.addFeature(new FeatureCobblestoneGenerator());
        FeatureHandler.addFeature(new FeatureFireGenerator());
    }

    @Override
    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        ConfigurationHandler.init(event.getSuggestedConfigurationFile());
        FeatureHandler.preInit(event);
    }

    @Override
    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        NetworkRegistry.INSTANCE.registerGuiHandler(SimpleThings.INSTANCE, new GuiHandler());
        FeatureHandler.init(event);
    }

    @Override
    @EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
        FeatureHandler.postInit(event);
    }
}
