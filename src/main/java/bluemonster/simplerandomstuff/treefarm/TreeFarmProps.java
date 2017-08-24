package bluemonster.simplerandomstuff.treefarm;

import bluemonster.simplerandomstuff.refs.Names;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.Comment;
import net.minecraftforge.common.config.Config.Name;

public class TreeFarmProps {
    public static final String NAME = "Tree Farm";
    public static final String ID = "treefarm";

    private TreeFarmProps() {
    }

    public static class TreeFarmConfigCategory {
        @Name(Names.Configs.SHOULD_LOAD)
        @Comment(Names.Configs.SHOULD_LOAD_TREE_FARM)
        public boolean shouldLoad = true;

        @Name(Names.Configs.TREE_FARM_RF_BREAK)
        @Comment(Names.Configs.TREE_FARM_RF_BREAK_COMMENT)
        @Config.RangeInt(min = 0, max = 1000)
        public int blockBreakRF = 100;

        public TreeFarmConfigCategory() {
        }
    }
}
