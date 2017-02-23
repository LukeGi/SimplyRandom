package bluemonster122.simplethings.block;

import bluemonster122.simplethings.SimpleThings;
import bluemonster122.simplethings.handler.RegistryHandler;
import bluemonster122.simplethings.item.SimpleItemBlockBase;
import bluemonster122.simplethings.tab.CreativeTabST;
import bluemonster122.simplethings.util.IInitModelVarients;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;
public class SimpleBlockBase extends Block implements CreativeTabST.ISTObj, IInitModelVarients
{
	public SimpleBlockBase(Material materialIn, String name)
	{
		super(materialIn);
		setHardness(5);
		setResistance(5);
		setHarvestLevel("pickaxe", 1);
		setRegistryName(SimpleThings.MOD_ID, name);
		setUnlocalizedName(getRegistryName().toString());
		setCreativeTab(SimpleThings.theTab);
		RegistryHandler.BLOCKS.add(this);
		RegistryHandler.ITEMS.add(getItemBlock());
	}
	
	public ItemBlock getItemBlock()
	{
		return new SimpleItemBlockBase(this);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void initModelsAndVariants() {
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation(getRegistryName().toString()));
	}

	@SideOnly(Side.CLIENT)
	public boolean addExtraInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced)
	{
		return false;
	}
}
