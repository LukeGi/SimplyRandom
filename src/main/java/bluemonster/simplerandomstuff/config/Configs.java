package bluemonster.simplerandomstuff.config;

import bluemonster.simplerandomstuff.SimpleRandomStuff;
import bluemonster.simplerandomstuff.core.CoreProps;
import bluemonster.simplerandomstuff.treefarm.TreeFarmProps;
import net.minecraftforge.common.config.Config;

@Config(modid = SimpleRandomStuff.MOD_ID, name = SimpleRandomStuff.MOD_NAME, category = "")
public class Configs {
    @Config.Name(CoreProps.NAME)
    public static final CoreProps.CoreConfigCategory CORE = new CoreProps.CoreConfigCategory();
    @Config.Name(TreeFarmProps.NAME)
    public static final TreeFarmProps.TreeFarmConfigCategory TREE_FARM = new TreeFarmProps.TreeFarmConfigCategory();

    private Configs() {

    }
}
