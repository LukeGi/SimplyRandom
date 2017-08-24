package bluemonster.simplerandomstuff.core;

import bluemonster.simplerandomstuff.refs.Names;
import net.minecraftforge.common.config.Config;

public class CoreProps {

    public static final String NAME = "Core";

    private CoreProps() {
    }

    public static class CoreConfigCategory {
        @Config.Name(Names.Configs.SHOULD_LOAD)
        @Config.Comment(Names.Configs.SHOULD_LOAD_CORE)
        public boolean shouldLoad = true;

        @Config.Name(Names.Configs.ONLY_RF)
        @Config.Comment(Names.Configs.ONLY_RF_COMMENT)
        public boolean onlyRF = true;

        public CoreConfigCategory() {
        }
    }
}
