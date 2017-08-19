package bluemonster.simplerandomstuff;

import bluemonster.simplerandomstuff.proxy.CommonProxy;
import bluemonster.simplerandomstuff.registry.FeatureRegistryCore;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.logging.log4j.Logger;

import java.util.List;

@Mod.EventBusSubscriber
@Mod(modid = SimpleRandomStuff.MOD_ID, name = SimpleRandomStuff.MOD_NAME, version = SimpleRandomStuff.VERSION)
public class SimpleRandomStuff {
    public static final String MOD_ID = "simplerandomstuff";
    public static final String MOD_NAME = "Simple Random Stuff";
    public static final String VERSION = "@VERSION@";

    @Mod.Instance(SimpleRandomStuff.MOD_ID)
    public static SimpleRandomStuff instance;

    @SidedProxy(clientSide = "bluemonster.simplerandomstuff.proxy.ClientProxy", serverSide = "bluemonster.simplerandomstuff.proxy.ServerProxy")
    public static CommonProxy proxy;

    public static Logger logger;

    public static List<FeatureRegistry> featureRegistries;
    public static CreativeTabs Tab;

    static {
        Tab = new CreativeTabs() {
            @Override
            public ItemStack getTabIconItem() {
                return new ItemStack(Blocks.BONE_BLOCK);
            }
        }
        featureRegistries.add(FeatureRegistryCore.INSTANCE);
    }

    @SubscribeEvent
    public static void configsChanged(ConfigChangedEvent event) {
        if (event.getModID() == SimpleRandomStuff.MOD_ID)
            ConfigManager.sync(SimpleRandomStuff.MOD_ID, Config.Type.INSTANCE);
    }

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        featureRegistries.stream().filter(FeatureRegistry::shouldLoad).forEach(s -> event.getRegistry().registerAll(s.getBlocks()));
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        ConfigManager.sync(SimpleRandomStuff.MOD_ID, Config.Type.INSTANCE);
    }
}
