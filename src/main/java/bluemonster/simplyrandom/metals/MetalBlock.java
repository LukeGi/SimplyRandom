package bluemonster.simplyrandom.metals;

import bluemonster.simplyrandom.core.BlockBase;
import net.minecraft.block.material.Material;

public class MetalBlock extends BlockBase {

    public MetalBlock(String type) {
        super(Material.IRON, type + "_block");
        setCreativeTab(MetalsCT.INSTANCE);
    }
}
