package bluemonster.simplyrandom.metals;

import bluemonster.simplyrandom.core.ItemBlockBase;
import net.minecraft.block.Block;

public class MetalItemBlock extends ItemBlockBase {

    public MetalItemBlock(Block block) {
        super(block);
        setCreativeTab(MetalsCT.INSTANCE);
    }
}
