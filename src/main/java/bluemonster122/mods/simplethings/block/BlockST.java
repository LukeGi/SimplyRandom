package bluemonster122.mods.simplethings.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemBlock;

public class BlockST extends Block {
    public BlockST(String name, Material blockMaterialIn, MapColor blockMapColorIn) {
        super(blockMaterialIn, blockMapColorIn);
        setup(name);
    }

    private void setup(String name) {
        setRegistryName(name);
        setUnlocalizedName(getRegistryName().getResourceDomain() + "." + name);
    }

    public BlockST(String name, Material materialIn) {
        super(materialIn);
        setup(name);
    }

    public ItemBlock createItemBlock() {
        return (ItemBlock) new ItemBlock(this).setRegistryName(getRegistryName());
    }
}
