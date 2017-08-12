package bluemonster.simplerandomstuff.reference;

import net.minecraftforge.common.config.ConfigCategory;

public class Names {
    private Names() {
    }

    public static class Blocks {
        public static final String TREE_FARM = "tree_farm";

        public static final String COBBLESTONE_GENERATOR = "cobblestone_generator";

        public static final String GENERATOR = "generator";

        public static final String FLOOD_GATE = "flood_gate";

        public static final String MINER = "miner";

        public static final String GRINDER = "grinder";

        private Blocks() {
        }
    }

    public static class OreDict {
        public static final String SAPLING = "sapling";

        public static final String IRON_BLOCK = "blockIron";

        public static final String OBSIDIAN = "obsidian";

        public static final String IRON_INGOT = "ingotIron";

        public static final String GLASS_PANES = "paneGlass";

        public static final String GLASS_BLOCK = "blockGlass";

        public static final String GOLD_INGOT = "ingotGold";

        public static final String DIAMOND = "gemDiamond";

        public static final String REDSTONE = "dustRedstone";

        public static final String STRING = "string";

        public static final String STICK = "stickWood";

        public static final String IRON_STICK = "stickIron";

        public static final String IRON_BARS = "barsIron";

        public static final String SUGAR = "sugar";

        private OreDict() {
        }
    }

    public static class Items {
        public static final String MISC = "misc";

        public static final String WRENCH = "wrench";

        private Items() {
        }
    }

    public static class Features {
        public static final String MINER = "Miner";

        public static final String TREE_FARM = "Tree Farm";

        public static final String TANK = "Tanks";

        public static final String PUMP = "Pump";

        public static final String OVERLAYS = "Overlay/HUD";

        public static final String GRINDER = "Grinder";

        public static final String GENERATORS = "Generators";

        public static final String CRAFTERS = "Crafters";

        public static final String CORE = "Core";

        public static final String COBBLESTONE_GENERATOR = "Cobblestone Generator";

        public static final ConfigCategory GENERATORS_CC = new ConfigCategory("Generators");


        public static class Configs {
            public static final String TREE_FARM_BREAK_ENERGY = "Energy Consumed On Block Break";

            public static final String PUMP_ENERGY = "Energy Consumer Per Pump Operation";

            public static final String OVERLAYS_USE_CUSTOM_OVERLAYS = "Use Custom in game UI";

            public static final String OVERLAYS_SHOW_LVLUPMSG = "Show Level up message";

            public static final String MINER_SCAN_POWER = "Energy Consumed On Scanning A Block";

            public static final String MINER_BREAK_POWER = "Energy Consumed On Breaking A Block";

            public static final String COBBLE_GEN_RF_COST = "Energy Consumed On Creating A Single Cobblestone";

            public static final String GENERATORS_SUGAR_RFPERT = "SUGAR: Energy Created Per Tick";

            public static final String GENERATORS_SUGAR_BURNTIME = "SUGAR: Sugar Burn Time (ticks)";

            public static final String GENERATORS_FIRE_RFPERT = "FIRE: Energy Created Per Tick";
        }
    }
}