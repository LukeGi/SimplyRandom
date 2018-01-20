package bluemonster.simplyrandom;

import bluemonster.simplyrandom.proxy.IProxy;
import bluemonster.simplyrandom.util.RecipeConverter;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;

import java.io.File;

import static bluemonster.simplyrandom.ModInfo.*;

@Mod(modid = MOD_ID, name = MOD_NAME, version = VERSION)
public class SimplyRandom {

    @SidedProxy(clientSide = CLIENT_PROXY_CLASS, serverSide = SERVER_PROXY_CLASS)
    public static IProxy proxy = null;

    @Instance(MOD_ID)
    public static SimplyRandom INSTANCE = null;
    public static Logger logger;

    @EventHandler
    public void onPreInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();
    }

    @EventHandler
    public void onInit(FMLInitializationEvent event) {
        RegistryHandler.onRegisterOreDict();
    }
}
