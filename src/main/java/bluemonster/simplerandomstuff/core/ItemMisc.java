package bluemonster.simplerandomstuff.core;

import bluemonster.simplerandomstuff.core.block.IEnumMeta;
import bluemonster.simplerandomstuff.reference.ModInfo;
import bluemonster.simplerandomstuff.reference.Names;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public class ItemMisc
        extends ItemST {
    @Override
    public String getUnlocalizedName(ItemStack itemStack) {
        return String.format(
                "item.%s:%s.%s",
                ModInfo.MOD_ID,
                Names.Items.MISC,
                Types.byMeta(itemStack.getMetadata())
                        .getName()
        );
    }

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> itemList) {
        if (isInCreativeTab(tab)) {
            for (Types type : Types.VARIANTS) {
                itemList.add(new ItemStack(this, 1, type.getMeta()));
            }
        }
    }

    public ItemMisc() {
        super(Names.Items.MISC, true);
    }

    public enum Types
            implements IEnumMeta, Comparable<Types> {
        IRON_ROD, WOODEN_GEAR, STONE_GEAR, MACHINE_BASE, COMPLEX_MACHINE_BASE;

        protected static final Types[] VARIANTS = values();

        private int meta;

        public static Types byMeta(int meta) {
            return VARIANTS[Math.abs(meta) % VARIANTS.length];
        }

        Types() {
            meta = ordinal();
        }

        @Override
        public int getMeta() {
            return meta;
        }
    }
}
