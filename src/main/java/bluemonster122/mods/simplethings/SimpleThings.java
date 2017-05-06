package bluemonster122.mods.simplethings;

import bluemonster122.mods.simplethings.cobblegen.FRCobbleGen;
import bluemonster122.mods.simplethings.core.FRCore;
import bluemonster122.mods.simplethings.core.network.MessageCraftingSync;
import bluemonster122.mods.simplethings.generators.FRGenerators;
import bluemonster122.mods.simplethings.handler.ConfigurationHandler;
import bluemonster122.mods.simplethings.handler.GuiHandler;
import bluemonster122.mods.simplethings.overlayoverhaul.FROverlays;
import bluemonster122.mods.simplethings.proxy.IProxy;
import bluemonster122.mods.simplethings.pump.FRPump;
import bluemonster122.mods.simplethings.reference.ModInfo;
import bluemonster122.mods.simplethings.tab.CreativeTabST;
import bluemonster122.mods.simplethings.tanks.FRTank;
import bluemonster122.mods.simplethings.treefarm.FRTreeFarm;
import bluemonster122.mods.simplethings.util.IFeatureRegistry;
import bluemonster122.mods.simplethings.workbench.FRCrafters;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

@Mod(modid = ModInfo.MOD_ID, version = ModInfo.VERSION, guiFactory = ModInfo.GUI_FACTORY_CLASS, updateJSON = ModInfo.UPDATE_JSON)
public class SimpleThings {
    public static final List<IFeatureRegistry> featureRegistries = new ArrayList<>();

    static {
        featureRegistries.add(FRCore.INSTANCE);
        featureRegistries.add(FRTank.INSTANCE);
        featureRegistries.add(FRPump.INSTANCE);
        featureRegistries.add(FRTreeFarm.INSTANCE);
        featureRegistries.add(FRCobbleGen.INSTANCE);
        featureRegistries.add(FRGenerators.INSTANCE);
        featureRegistries.add(FRCrafters.INSTANCE);
        featureRegistries.add(FROverlays.INSTANCE);
    }

    public Logger logger;
    public SimpleNetworkWrapper channel;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();
        ConfigurationHandler.INSTANCE.init(event.getSuggestedConfigurationFile());
        for (IFeatureRegistry registry : featureRegistries) {
            registry.registerBlocks();
            registry.registerItems();
            registry.registerEvents();
            registry.registerOreDict();
        }
        INSTANCE.setupNetwork();
        proxy.preInit();
    }

    private void setupNetwork( ) {
        logger.info(">>> Registering network channel...");

        channel = NetworkRegistry.INSTANCE.newSimpleChannel(ModInfo.CHANNEL);

        channel.registerMessage(MessageCraftingSync.class, MessageCraftingSync.class, 0, Side.SERVER);
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init();
        NetworkRegistry.INSTANCE.registerGuiHandler(SimpleThings.INSTANCE, new GuiHandler());
        featureRegistries.forEach(IFeatureRegistry::registerTileEntities);
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        featureRegistries.forEach(IFeatureRegistry::registerRecipes);
    }
    public static CreativeTabs theTab = new CreativeTabST();
    @Instance(value = ModInfo.MOD_ID)
    public static SimpleThings INSTANCE = new SimpleThings();
    @SidedProxy(clientSide = ModInfo.CLIENT_PROXY_CLASS, serverSide = ModInfo.SERVER_PROXY_CLASS)
    public static IProxy proxy;
}
