package bluemonster122.simplethings.item;

import bluemonster122.simplethings.handler.RegistryHandler;
import bluemonster122.simplethings.tab.CreativeTabST;
import bluemonster122.simplethings.util.IInitModelVarients;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class SimpleItemBase extends Item implements CreativeTabST.ISTObj, IInitModelVarients
{
	public SimpleItemBase()
	{
		RegistryHandler.ITEMS.add(this);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void initModelsAndVariants()
	{
		ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName().toString()));
	}
}
