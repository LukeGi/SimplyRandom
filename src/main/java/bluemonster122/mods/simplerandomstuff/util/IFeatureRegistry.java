package bluemonster122.mods.simplerandomstuff.util;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;

public interface IFeatureRegistry
{
  void registerBlocks(IForgeRegistry<Block> registry);
  
  void registerItems(IForgeRegistry<Item> registry);
  
  void registerRecipes(IForgeRegistry<IRecipe> registry);
  
  void registerTileEntities();
  
  void loadConfigs(Configuration configuration);
  
  void registerEvents();
  
  void registerOreDict();
  
  @SideOnly(Side.CLIENT)
  void registerRenders();
  
  @SideOnly(Side.CLIENT)
  void registerClientEvents();
  
  String getName();
}
