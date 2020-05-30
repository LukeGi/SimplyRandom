package luhegi.mods.simplyrandom.cobblegen;

import luhegi.mods.simplyrandom.basis.objects.BasisBlockItem;

public class CobbleGenItem extends BasisBlockItem {
    public CobbleGenItem(CobbleGenBlock block)
    {
        super(block, DEFAULT.maxStackSize(1));
    }
}
