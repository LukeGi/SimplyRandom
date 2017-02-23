package bluemonster122.simplethings.tab;

import bluemonster122.simplethings.SimpleThings;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public class CreativeTabST extends CreativeTabs
{
	public CreativeTabST()
	{
		super(SimpleThings.MOD_ID);
	}

	@Override
	public ItemStack getTabIconItem()
	{
		return ItemStack.EMPTY;
	}

	public interface ISTObj
	{
	}
}
