package bluemonster.simplerandomstuff.tab;

import bluemonster.simplerandomstuff.reference.ModInfo;
import bluemonster.simplerandomstuff.core.FRCore;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public class CreativeTabST
        extends CreativeTabs {
    public CreativeTabST() {
        super(ModInfo.MOD_ID);
    }

    @Override
    public ItemStack getTabIconItem() {
        return new ItemStack(FRCore.wrench);
    }
}
