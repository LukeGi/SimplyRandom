package bluemonster.simplyrandom.metals;

import net.minecraft.item.ItemBlock;

import java.util.Objects;

public class MetalBlockItem extends ItemBlock {

    public MetalBlockItem(MetalBlock block) {
        super(block);
        setRegistryName(Objects.requireNonNull(block.getRegistryName()));
        setCreativeTab(MetalsCT.INSTANCE);
    }
}
