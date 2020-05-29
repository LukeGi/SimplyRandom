package luhegi.mods.simplyrandom.basis.objects;

import net.minecraft.item.BlockItem;

public class BasisBlockItem extends BlockItem {
    protected static final Properties DEFAULT = new Properties();

    public BasisBlockItem(BasisBlock block, Properties props) {
        super(block, props);
    }
}
