package bluemonster.simplerandomstuff.registry;

import bluemonster.simplerandomstuff.FeatureRegistry;
import bluemonster.simplerandomstuff.block.BlockSRSBase;
import bluemonster.simplerandomstuff.config.Configs;

public class FeatureRegistryCore extends FeatureRegistry {
    public static final FeatureRegistry INSTANCE = new FeatureRegistryCore();

    @Override
    public boolean shouldLoad() {
        return Configs.coreConfig.load;
    }

    @Override
    public BlockSRSBase[] getBlocks() {
        return new BlockSRSBase[0];
    }

    private FeatureRegistryCore() {
    }
}
