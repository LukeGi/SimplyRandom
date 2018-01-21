package bluemonster.simplyrandom.core;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;

import java.util.Objects;

public class ItemBlockBase extends ItemBlock {


    public ItemBlockBase(Block block) {
        super(block);
        setRegistryName(Objects.requireNonNull(block.getRegistryName()));
    }
}
