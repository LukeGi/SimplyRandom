package bluemonster122.mods.simplerandomstuff.core.block;

import bluemonster122.mods.simplerandomstuff.reference.ModInfo;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public abstract class BlockEnum extends BlockST {
    private final IEnumMeta[] VARIANTS;

    public BlockEnum(String name, IEnumMeta[] variants) {
        this(name, Material.ROCK, variants);
    }

    public BlockEnum(String name, Material material, IEnumMeta[] variants) {
        super(name, material);
        if (variants.length > 0) {
            VARIANTS = variants;
        } else {
            VARIANTS = new IEnumMeta[0];
        }
    }

    public String getUnlocalizedName(ItemStack itemStack) {
        if (itemStack != ItemStack.EMPTY && Block.getBlockFromItem(itemStack.getItem()) instanceof BlockST && VARIANTS.length > 0) {
            return String.format("tile.%s:%s", ModInfo.MOD_ID, VARIANTS[Math.abs(itemStack.getMetadata() % VARIANTS.length)].getName());
        }
        return super.getUnlocalizedName();
    }

    @Override
    public ItemBlock createItemBlock( ) {
        return new ItemBlockEnum(this);
    }
}
