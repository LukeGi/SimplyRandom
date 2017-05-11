package bluemonster122.mods.simplethings.core;

import bluemonster122.mods.simplethings.core.block.IEnumMeta;
import bluemonster122.mods.simplethings.reference.ModInfo;
import bluemonster122.mods.simplethings.reference.Names.Items;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public class ItemMisc extends ItemST {
    public ItemMisc( ) {
        super(Items.MISC, true);
    }

    @Override
    public String getUnlocalizedName(ItemStack itemStack) {
        return String.format("item.%s:%s.%s", ModInfo.MOD_ID, Items.MISC, Types.byMeta(itemStack.getMetadata()).getName());
    }

    @Override
    public void getSubItems(Item item, CreativeTabs tab, NonNullList<ItemStack> itemList) {
        for (Types type : Types.VARIANTS) {
            itemList.add(new ItemStack(item, 1, type.getMeta()));
        }
    }

    public enum Types implements IEnumMeta, Comparable<Types> {
        IRON_ROD, WOODEN_GEAR, STONE_GEAR, MACHINE_BASE;

        protected static final Types[] VARIANTS = values();
        private int meta;

        public static Types byMeta(int meta) {
            return VARIANTS[Math.abs(meta) % VARIANTS.length];
        }

        @Override
        public int getMeta( ) {
            return meta;
        }

        Types( ) {
            meta = ordinal();
        }
    }
}
