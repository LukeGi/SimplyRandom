package bluemonster.simplerandomstuff.config;

import bluemonster.simplerandomstuff.SimpleRandomStuff;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.Name;

@Config(modid = SimpleRandomStuff.MOD_ID, name = SimpleRandomStuff.MOD_NAME, category = "")
public class Configs {
    @Name("CoreConfigs")
    public static CoreConfigs coreConfig = new CoreConfigs();
}
