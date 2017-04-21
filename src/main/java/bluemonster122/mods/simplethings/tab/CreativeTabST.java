package bluemonster122.mods.simplethings.tab;

import bluemonster122.mods.simplethings.reference.ModInfo;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public class CreativeTabST extends CreativeTabs {
    public CreativeTabST( ) {
        super(ModInfo.MOD_ID);
    }

    @Override
    public ItemStack getTabIconItem( ) {
        return ItemStack.EMPTY;
    }
}
