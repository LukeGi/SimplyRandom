package luhegi.mods.simplyrandom.basis.objects;

import luhegi.mods.simplyrandom.SimplyRandom;
import net.minecraft.item.BlockItem;

public class BasisBlockItem extends BlockItem {
    protected static final Properties DEFAULT = new Properties().group(SimplyRandom.INSTANCE.group);

    public BasisBlockItem(BasisBlock block, Properties props) {
        super(block, props);
    }
}
