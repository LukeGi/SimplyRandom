package lhg.forgemods.simplyrandom.data;

import lhg.forgemods.simplyrandom.cobblemaker.CobblestoneMaker;
import lhg.forgemods.simplyrandom.miner.Miner;
import lhg.forgemods.simplyrandom.treefarm.TreeFarm;
import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;

import static lhg.forgemods.simplyrandom.data.Tags.Blocks.COBBLESTONE_MAKERS;
import static lhg.forgemods.simplyrandom.data.Tags.Blocks.MINERS;
import static lhg.forgemods.simplyrandom.data.Tags.Blocks.TREE_FARMS;

public class SRBlockTagProvider extends BlockTagsProvider
{
    public SRBlockTagProvider(DataGenerator gen)
    {
        super(gen);
    }

    @Override
    protected void registerTags()
    {
        getBuilder(COBBLESTONE_MAKERS).add(CobblestoneMaker.BLOCK);
        getBuilder(TREE_FARMS).add(TreeFarm.BLOCK);
        getBuilder(MINERS).add(Miner.BLOCK);
    }

    @Override
    public String getName()
    {
        return "Simply Random Block Tags";
    }
}
