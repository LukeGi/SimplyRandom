package luhegi.mods.simplyrandom.cobblegen;

import luhegi.mods.simplyrandom.basis.objects.BasisBlockItem;
import net.minecraft.item.ItemGroup;

public class CobbleGenItem extends BasisBlockItem {
    public CobbleGenItem(CobbleGenBlock block, ItemGroup group) {
        super(block, DEFAULT.maxStackSize(1).group(group));
    }
}
