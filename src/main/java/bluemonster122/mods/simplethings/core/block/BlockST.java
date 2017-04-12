package bluemonster122.mods.simplethings.core.block;

import bluemonster122.mods.simplethings.reference.ModInfo;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemBlock;

public class BlockST extends Block {
    public BlockST(String name, Material blockMaterialIn, MapColor blockMapColorIn) {
        super(blockMaterialIn, blockMapColorIn);
        setup(name);
    }

    public BlockST(String name, Material materialIn) {
        super(materialIn);
        setup(name);
    }

    private void setup(String name) {
        setRegistryName(ModInfo.MOD_ID, name);
        setUnlocalizedName(getRegistryName().toString());
    }

    public ItemBlock createItemBlock() {
        return (ItemBlock) new ItemBlock(this).setRegistryName(getRegistryName());
    }
}
