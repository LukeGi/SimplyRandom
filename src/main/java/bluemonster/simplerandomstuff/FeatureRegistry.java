package bluemonster.simplerandomstuff;

import bluemonster.simplerandomstuff.block.BlockSRSBase;

public abstract class FeatureRegistry {
    public abstract boolean shouldLoad();

    public abstract BlockSRSBase[] getBlocks();
}
