package bluemonster122.mods.simplethings.core;

import bluemonster122.mods.simplethings.core.block.IEnumMeta;
import bluemonster122.mods.simplethings.item.ItemST;
import bluemonster122.mods.simplethings.reference.ModInfo;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public class ItemMisc extends ItemST {
    public ItemMisc() {
        super("misc", true);
    }

    @Override
    public String getUnlocalizedName(ItemStack itemStack) {
        return String.format("item.%s:%s", ModInfo.MOD_ID, "tank_upgrade" + Types.byMeta(itemStack.getMetadata()).getName());
    }

    @Override
    public void getSubItems(Item item, CreativeTabs tab, NonNullList<ItemStack> itemList) {
        for (Types type : Types.VARIANTS) {
            itemList.add(new ItemStack(item, 1, type.getMeta()));
        }
    }

    @Override
    public int getItemStackLimit(ItemStack stack) {
        int limit;
        switch (Types.byMeta(stack.getMetadata())) {
            case WRENCH:
                limit = 1;
                break;
            default:
                limit = 64;
                break;
        }
        return limit;
    }

    public enum Types implements IEnumMeta, Comparable<Types> {
        WRENCH;

        protected static final Types[] VARIANTS = values();
        private int meta;

        public static Types byMeta(int meta) {
            return VARIANTS[Math.abs(meta) % VARIANTS.length];
        }

        @Override
        public int getMeta() {
            return meta;
        }

        Types() {
            meta = ordinal();
        }
    }
}
