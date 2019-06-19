package lhg.forgemods.simplyrandom.data;

import lhg.forgemods.simplyrandom.SimplyRandom;
import lhg.forgemods.simplyrandom.core.RLProvider;
import net.minecraft.block.Block;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.Tag;
import net.minecraft.util.ResourceLocation;

public class Tags
{
    public static class Blocks {
        public static final Tag<Block> COBBLESTONE_MAKERS = tag("cobblestone_maker");
        public static final Tag<Block> TREE_FARMS = tag("tree_farm");
        public static final Tag<Block> MINERS = tag("miner");

        private static Tag<Block> tag(String name)
        {
            return new BlockTags.Wrapper(RLProvider.get(name));
        }
    }
}
