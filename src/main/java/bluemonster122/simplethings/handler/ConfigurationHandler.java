package bluemonster122.simplethings.handler;

import bluemonster122.simplethings.SimpleThings;
import bluemonster122.simplethings.tileentity.TileTreeFarm;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.io.File;

@Mod.EventBusSubscriber
public class ConfigurationHandler
{
    public static Configuration configuration;
    // TREE FARM
    public static boolean load_tree_farm = true;
    public static int tree_farm_place_energy = 10;
    public static int tree_farm_break_energy = 50;

    // COBBLESTONE GENERATOR
    public static boolean load_cobblestone_generator = true;
    public static int cobblestone_generator_req_power = 0;

    // POWER GENERATORS
    public static boolean load_power_generator_fire = true;

    public static void init(File configFile)
    {
        if (configuration == null)
        {
            configuration = new Configuration(configFile);
            loadConfiguration();
        }
    }

    private static void loadConfiguration()
    {
        // TREE FARM
        tree_farm_break_energy = configuration.getInt("Energy Consumed On Block Break", "tree_farm", 50, 0, 1000, "Set to 0 to make the farm cost no power.");
        tree_farm_place_energy = configuration.getInt("Energy Consumed On Block Place", "tree_farm", 10, 0, 500, "Set to 0 to make the farm cost no power.");
        TileTreeFarm.usePower = !(tree_farm_break_energy == 0 && tree_farm_place_energy == 0);

        // COBBLESTONE GENERATOR
        cobblestone_generator_req_power = configuration.getInt("Cost Per Cobble", "cobblestone_generator", 0, 0, 1000, "If set to 0, the cobblestone is free.");

        // POWER GENERATORS

        if (configuration.hasChanged())
        {
            configuration.save();
        }
    }

    @SubscribeEvent
    public static void onConfigurationChangedEvent(ConfigChangedEvent.OnConfigChangedEvent event)
    {
        if (event.getModID().equalsIgnoreCase(SimpleThings.MOD_ID))
        {
            loadConfiguration();
        }
    }
}
