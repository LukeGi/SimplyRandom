package bluemonster122.mods.simplethings.core;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;

public class ItemBlockST extends ItemBlock {
    public ItemBlockST(Block block) {
        super(block);
        setRegistryName(block.getRegistryName());
    }
}
