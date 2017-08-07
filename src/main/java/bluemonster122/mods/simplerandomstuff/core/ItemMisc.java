package bluemonster122.mods.simplerandomstuff.core;

import bluemonster122.mods.simplerandomstuff.core.block.IEnumMeta;
import bluemonster122.mods.simplerandomstuff.reference.ModInfo;
import bluemonster122.mods.simplerandomstuff.reference.Names.Items;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public class ItemMisc
  extends ItemST
{
  public ItemMisc()
  {
    super(Items.MISC, true);
  }
  
  @Override
  public String getUnlocalizedName(ItemStack itemStack)
  {
    return String.format(
      "item.%s:%s.%s",
      ModInfo.MOD_ID,
      Items.MISC,
      Types.byMeta(itemStack.getMetadata())
           .getName()
    );
  }
  
  @Override
  public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> itemList)
  {
    if (isInCreativeTab(tab))
    {
      for (Types type : Types.VARIANTS)
      {
        itemList.add(new ItemStack(this, 1, type.getMeta()));
      }
    }
  }
  
  public enum Types
    implements IEnumMeta, Comparable<Types>
  {
    IRON_ROD, WOODEN_GEAR, STONE_GEAR, MACHINE_BASE, COMPLEX_MACHINE_BASE;
    
    protected static final Types[] VARIANTS = values();
    
    private int meta;
    
    Types()
    {
      meta = ordinal();
    }
    
    public static Types byMeta(int meta)
    {
      return VARIANTS[Math.abs(meta) % VARIANTS.length];
    }
    
    @Override
    public int getMeta()
    {
      return meta;
    }
  }
}
