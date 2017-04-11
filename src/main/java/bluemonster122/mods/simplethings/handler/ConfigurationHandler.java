package bluemonster122.mods.simplethings.handler;

import bluemonster122.mods.simplethings.SimpleThings;
import bluemonster122.mods.simplethings.reference.ModInfo;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.io.File;

@Mod.EventBusSubscriber
public class ConfigurationHandler {
    public static Configuration configuration;

    public static void init(File configFile) {
        if (configuration == null) {
            configuration = new Configuration(configFile);
            loadConfiguration();
        }
    }

    private static void loadConfiguration() {
        SimpleThings.featureRegistries.forEach((registry) -> registry.loadConfigs(configuration));
//        // TREE FARM
//        tree_farm_place_energy = configuration.getInt(
//                "Energy Consumed On Block Place", "tree_farm", 10, 0, 500, "Set to 0 to make the farm cost no power.");
//        //        TileTreeFarm.usePower = !(tree_farm_break_energy == 0 && tree_farm_place_energy == 0);
//        // COBBLESTONE GENERATOR
//        cobblestone_generator_req_power = configuration.getInt(
//                "Cost Per Cobble", "cobblestone_generator", 0, 0, 1000, "If set to 0, the cobblestone is free.");
//        // POWER GENERATORS
//        sugar_burn_time = configuration.getInt(
//                "Sugar burn time", "power_generators", 60, 1, Integer.MAX_VALUE, "Set to any number larger than 0.");
        if (configuration.hasChanged()) {
            configuration.save();
        }
    }

    @SubscribeEvent
    public static void onConfigurationChangedEvent(ConfigChangedEvent.OnConfigChangedEvent event) {
        if (event.getModID().equalsIgnoreCase(ModInfo.MOD_ID)) {
            loadConfiguration();
        }
    }
}
