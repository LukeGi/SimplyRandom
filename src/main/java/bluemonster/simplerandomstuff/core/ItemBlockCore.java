package bluemonster.simplerandomstuff.core;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;

public class ItemBlockCore extends ItemBlock{
    public ItemBlockCore(Block block) {
        super(block);
        setRegistryName(block.getRegistryName());
    }
}
