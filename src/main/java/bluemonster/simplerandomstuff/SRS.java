package bluemonster.simplerandomstuff;

import bluemonster.simplerandomstuff.core.network.MessageCraftingSync;
import bluemonster.simplerandomstuff.generators.FRGenerators;
import bluemonster.simplerandomstuff.handler.GuiHandler;
import bluemonster.simplerandomstuff.overlayoverhaul.FROverlays;
import bluemonster.simplerandomstuff.pump.FRPump;
import bluemonster.simplerandomstuff.treefarm.FRTreeFarm;
import bluemonster.simplerandomstuff.cobblegen.FRCobbleGen;
import bluemonster.simplerandomstuff.core.FRCore;
import bluemonster.simplerandomstuff.grinder.FRGrinder;
import bluemonster.simplerandomstuff.handler.ConfigurationHandler;
import bluemonster.simplerandomstuff.miner.FRMiner;
import bluemonster.simplerandomstuff.proxy.IProxy;
import bluemonster.simplerandomstuff.reference.ModInfo;
import bluemonster.simplerandomstuff.tab.CreativeTabST;
import bluemonster.simplerandomstuff.tanks.FRTank;
import bluemonster.simplerandomstuff.util.IFeatureRegistry;
import bluemonster.simplerandomstuff.workbench.FRCrafters;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.Logger;

@Mod(modid = ModInfo.MOD_ID,
        version = ModInfo.VERSION,
        guiFactory = ModInfo.GUI_FACTORY_CLASS,
        updateJSON = ModInfo.UPDATE_JSON)
public class SRS {

    public static final CreativeTabs theTab;

    public static final IFeatureRegistry[] featureRegistries;

    public static final boolean isDev;
    public static Logger logger;
    @Instance(value = ModInfo.MOD_ID)
    public static SRS INSTANCE = new SRS();
    @SidedProxy(clientSide = ModInfo.CLIENT_PROXY_CLASS, serverSide = ModInfo.SERVER_PROXY_CLASS)
    public static IProxy proxy;

    static {
        isDev = Boolean.parseBoolean(System.getProperty("indev"));
        theTab = new CreativeTabST();
        featureRegistries = new IFeatureRegistry[]{
                FRCore.INSTANCE,
                FRTank.INSTANCE,
                FRPump.INSTANCE,
                FRTreeFarm.INSTANCE,
                FRCobbleGen.INSTANCE,
                FRGenerators.INSTANCE,
                FRCrafters.INSTANCE,
                FROverlays.INSTANCE,
                FRMiner.INSTANCE,
                FRGrinder.INSTNACE
        };
    }

    public SimpleNetworkWrapper channel;

    public static boolean shouldLoad(IFeatureRegistry registry) {
        if (!ConfigurationHandler.FeatureLoad.containsKey(registry)) {
            INSTANCE.logger.warn("attempted to load a registry before it's load logic was figured out");
            return false;
        } else {
            return ConfigurationHandler.FeatureLoad.get(registry);
        }
    }

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();
        ConfigurationHandler.INSTANCE.init(event.getSuggestedConfigurationFile());
        for (IFeatureRegistry registry : featureRegistries) {
            if (shouldLoad(registry)) {
                registry.registerEvents();
            }
        }
        INSTANCE.setupNetwork();
        proxy.preInit();
    }

    private void setupNetwork() {
        logger.info(">>> Registering network channel...");

        channel = NetworkRegistry.INSTANCE.newSimpleChannel(ModInfo.CHANNEL);

        channel.registerMessage(MessageCraftingSync.class, MessageCraftingSync.class, 0, Side.SERVER);
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init();
        NetworkRegistry.INSTANCE.registerGuiHandler(SRS.INSTANCE, new GuiHandler());
        for (IFeatureRegistry fr : featureRegistries) {
            if (shouldLoad(fr)) {
                fr.registerOreDict();
                fr.registerTileEntities();
            }
        }
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
    }

    @EventBusSubscriber
    public static class RegistrationHandler {
        @SubscribeEvent
        public static void registerBlocks(Register<Block> event) {
            for (IFeatureRegistry fr : SRS.featureRegistries) {
                if (SRS.shouldLoad(fr)) fr.registerBlocks(event.getRegistry());
            }
        }

        @SubscribeEvent
        public static void registerItems(Register<Item> event) {
            for (IFeatureRegistry fr : SRS.featureRegistries) {
                if (SRS.shouldLoad(fr)) fr.registerItems(event.getRegistry());
            }
        }

        @SubscribeEvent
        public static void registerRecipes(Register<IRecipe> event) {

        }

        @SideOnly(Side.CLIENT)
        @SubscribeEvent
        public static void registerRenders(ModelRegistryEvent event) {
            for (IFeatureRegistry fr : SRS.featureRegistries) {
                if (SRS.shouldLoad(fr)) fr.registerRenders();
            }
        }
    }
}
