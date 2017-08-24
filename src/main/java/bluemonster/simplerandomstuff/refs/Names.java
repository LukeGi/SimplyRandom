package bluemonster.simplerandomstuff.refs;

import bluemonster.simplerandomstuff.SimpleRandomStuff;
import bluemonster.simplerandomstuff.treefarm.TreeFarmProps;

public class Names {
    public static class Configs {
        // ===== Shared =====
        public static final String SHOULD_LOAD = "Should Load";

        // ===== Core =====
        public static final String SHOULD_LOAD_CORE = "If enabled, the mod will load, otherwise it will not.";
        public static final String ONLY_RF = "Only Use RF";
        public static final String ONLY_RF_COMMENT = "Should " + SimpleRandomStuff.MOD_NAME + " use only RF?";

        // ===== Tree Farm =====
        public static final String SHOULD_LOAD_TREE_FARM = "If enabled, " + TreeFarmProps.NAME + " will load";
        public static final String TREE_FARM_RF_BREAK = "RF Used On Break";
        public static final String TREE_FARM_RF_BREAK_COMMENT = "This is the amount of RF that the tree farm will use to break a block.";
        public static final String TREE_FARM_RF_LEAVES = "RF Used When Breaking Leaves";
        public static final String TREE_FARM_RF_LEAVES_COMMENT = "If set to true, it will use 0.1x as much power to break a leaf block as a wood block takes. If set to false, it will use no power to break leaves";
    }
}
