package luhegi.mods.simplyrandom.coalgen;

import luhegi.mods.simplyrandom.basis.objects.BasisBlock;
import luhegi.mods.simplyrandom.basis.objects.BasisBlockItem;

public class CoalGenItem extends BasisBlockItem {
    public CoalGenItem(BasisBlock block) {
        super(block, DEFAULT.maxStackSize(1));
    }
}
