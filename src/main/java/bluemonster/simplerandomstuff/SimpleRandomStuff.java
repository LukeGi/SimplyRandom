package bluemonster.simplerandomstuff;

import bluemonster.simplerandomstuff.client.GuiHandler;
import bluemonster.simplerandomstuff.proxy.IProxy;
import cofh.redstoneflux.RedstoneFlux;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.oredict.OreDictionary;
import org.apache.logging.log4j.Logger;

@Mod.EventBusSubscriber
@Mod(modid = SimpleRandomStuff.MOD_ID, name = SimpleRandomStuff.MOD_NAME, version = SimpleRandomStuff.VERSION)
public class SimpleRandomStuff {
    public static final String MOD_ID = "simplerandomstuff";
    public static final String MOD_NAME = "Simple Random Stuff";
    public static final String VERSION = "@VERSION@";
    public static final String DEPS = RedstoneFlux.VERSION_GROUP;
    public static final boolean isDev = Boolean.parseBoolean(System.getProperty("indev"));

    @Mod.Instance(SimpleRandomStuff.MOD_ID)
    public static SimpleRandomStuff instance;

    @SidedProxy(clientSide = "bluemonster.simplerandomstuff.proxy.ClientProxy", serverSide = "bluemonster.simplerandomstuff.proxy.ServerProxy")
    public static IProxy proxy;

    public static Logger logger;

    public static CreativeTabs Tab;

    static {
        Tab = new CreativeTabs(MOD_ID) {
            @Override
            public ItemStack getTabIconItem() {
                return new ItemStack(Blocks.BONE_BLOCK);
            }
        };

    }

    @SubscribeEvent
    public static void configsChanged(ConfigChangedEvent event) {
        if (event.getModID().equals(SimpleRandomStuff.MOD_ID))
            ConfigManager.sync(SimpleRandomStuff.MOD_ID, Config.Type.INSTANCE);
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();
        ConfigManager.sync(SimpleRandomStuff.MOD_ID, Config.Type.INSTANCE);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        NetworkRegistry.INSTANCE.registerGuiHandler(instance, new GuiHandler());
        OreDictionary.registerOre("sapling", new ItemStack(Blocks.SAPLING, 1, OreDictionary.WILDCARD_VALUE));
    }

}
