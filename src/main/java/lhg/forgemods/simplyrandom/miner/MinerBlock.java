package lhg.forgemods.simplyrandom.miner;

import lhg.forgemods.simplyrandom.core.SRBlock;
import lhg.forgemods.simplyrandom.core.Tooltip;
import lhg.forgemods.simplyrandom.core.Tooltip.Builder;

public class MinerBlock extends SRBlock
{
    public MinerBlock(Properties props)
    {
        super(props);
    }

    @Override
    protected Tooltip getTooltip()
    {
        return new Builder().createTooltip();
    }
}
