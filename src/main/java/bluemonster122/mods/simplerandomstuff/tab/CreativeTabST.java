package bluemonster122.mods.simplerandomstuff.tab;

import bluemonster122.mods.simplerandomstuff.core.FRCore;
import bluemonster122.mods.simplerandomstuff.reference.ModInfo;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public class CreativeTabST
  extends CreativeTabs
{
  public CreativeTabST()
  {
    super(ModInfo.MOD_ID);
  }
  
  @Override
  public ItemStack getTabIconItem()
  {
    return new ItemStack(FRCore.wrench);
  }
}
