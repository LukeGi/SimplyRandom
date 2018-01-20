package bluemonster.simplyrandom;

import net.minecraftforge.common.config.Config;

import static bluemonster.simplyrandom.ModInfo.MOD_ID;

@Config(modid = MOD_ID, category = "core", name = "Simply Random Configs")
public class Configs {

    @Config.Comment("So long as this is 'True' and not 'False', Simply Random will spawn ores.")
    public static boolean loadMetals = true;
}
