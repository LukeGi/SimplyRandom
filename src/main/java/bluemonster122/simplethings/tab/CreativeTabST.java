package bluemonster122.simplethings.tab;

import bluemonster122.simplethings.SimpleThings;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Map;
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
	
	@SideOnly(Side.CLIENT)
	@Override
	public void displayAllRelevantItems(NonNullList<ItemStack> list)
	{
		ForgeRegistries.ITEMS.getEntries().stream().filter(
		  e -> e.getValue() instanceof ISTObj || (e.getValue() instanceof ItemBlock && ((ItemBlock) e.getValue()).getBlock() instanceof ISTObj)).map(
		  Map.Entry::getValue).forEach(item -> item.getSubItems(item, this, list));
	}
	
	public interface ISTObj
	{}
}
