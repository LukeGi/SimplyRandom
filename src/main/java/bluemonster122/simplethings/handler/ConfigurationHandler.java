package bluemonster122.simplethings.handler;

import bluemonster122.simplethings.SimpleThings;
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
	public static int tree_farm_place_energy = 10;
	public static int tree_farm_break_energy = 50;
	// COBBLESTONE GENERATOR
	public static int cobblestone_generator_req_power = 0;
	// POWER GENERATORS
	public static int energy_from_sugar = 300;
	public static int sugar_burn_time = 60;
	public static int max_blocks_broken = 5;
	
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
		tree_farm_break_energy = configuration.getInt(
		  "Energy Consumed On Block Break", "tree_farm", 50, 0, 1000, "Set to 0 to make the farm cost no power.");
		tree_farm_place_energy = configuration.getInt(
		  "Energy Consumed On Block Place", "tree_farm", 10, 0, 500, "Set to 0 to make the farm cost no power.");
		//        TileTreeFarm.usePower = !(tree_farm_break_energy == 0 && tree_farm_place_energy == 0);
		// COBBLESTONE GENERATOR
		cobblestone_generator_req_power = configuration.getInt(
		  "Cost Per Cobble", "cobblestone_generator", 0, 0, 1000, "If set to 0, the cobblestone is free.");
		// POWER GENERATORS
		energy_from_sugar = configuration.getInt(
		  "Energy per sugar", "power_generators", 300, 1, Integer.MAX_VALUE, "Set to any number larger than 0.");
		sugar_burn_time = configuration.getInt(
		  "Sugar burn time", "power_generators", 60, 1, Integer.MAX_VALUE, "Set to any number larger than 0.");
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
