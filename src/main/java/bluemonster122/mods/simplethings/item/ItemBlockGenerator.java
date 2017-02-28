package bluemonster122.mods.simplethings.item;

import bluemonster122.mods.simplethings.block.BlockGenerator;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemBlockGenerator extends ItemBlock {
    public ItemBlockGenerator(Block block) {
        super(block);
        setMaxDamage(0);
        setHasSubtypes(true);
        setMaxStackSize(1);
    }

    @Override
    public int getMetadata(int meta) {
        return meta;
    }

    @Override
    public String getUnlocalizedName(ItemStack itemStack) {
        return super.getUnlocalizedName() + "." + BlockGenerator.GeneratorType.byMetadata(itemStack.getMetadata()).getName();
    }
}
