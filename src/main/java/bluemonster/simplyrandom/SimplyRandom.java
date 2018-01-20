package bluemonster.simplyrandom;

import bluemonster.simplyrandom.proxy.IProxy;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonWriter;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.*;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.oredict.OreDictionary;
import scala.util.parsing.json.JSONObject;

import java.io.FileWriter;

import static bluemonster.simplyrandom.ModInfo.*;
import static bluemonster.simplyrandom.RegistryHandler.Objects.*;

@Mod(modid = MOD_ID, name = MOD_NAME, version = VERSION)
public class SimplyRandom {

    @SidedProxy(clientSide = CLIENT_PROXY_CLASS, serverSide = SERVER_PROXY_CLASS)
    public static IProxy proxy = null;

    @Instance(MOD_ID)
    public static SimplyRandom INSTANCE = null;
}
