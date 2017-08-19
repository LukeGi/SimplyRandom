package bluemonster.simplerandomstuff.block;

import bluemonster.simplerandomstuff.SimpleRandomStuff;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;

public class BlockSRSBase extends Block {
    public BlockSRSBase(String name, Material blockMaterialIn, MapColor blockMapColorIn) {
        super(blockMaterialIn, blockMapColorIn);
        this.setRegistryName(SimpleRandomStuff.MOD_ID, name);
        this.setUnlocalizedName(this.getRegistryName().toString());
        this.setCreativeTab(SimpleRandomStuff.Tab);
    }

    public BlockSRSBase(String name, Material materialIn) {
        this(name, materialIn, materialIn.getMaterialMapColor());
    }

    public BlockSRSBase(String name) {
        this(name, Material.CLOTH);
    }
}
