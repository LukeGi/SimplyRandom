package bluemonster122.mods.simplerandomstuff;

import bluemonster122.mods.simplerandomstuff.util.IFeatureRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@EventBusSubscriber
public class RegistrationHandler
{
  @SubscribeEvent
  public static void registerBlocks(Register<Block> event)
  {
    for (IFeatureRegistry fr : SRS.featureRegistries)
    {
      fr.registerBlocks(event.getRegistry());
    }
  }
  
  @SubscribeEvent
  public static void registerItems(Register<Item> event)
  {
    for (IFeatureRegistry fr : SRS.featureRegistries)
    {
      fr.registerItems(event.getRegistry());
    }
  }
  
  @SubscribeEvent
  public static void registerRecipes(Register<IRecipe> event)
  {
    for (IFeatureRegistry fr : SRS.featureRegistries)
    {
      fr.registerRecipes(event.getRegistry());
    }
  }
  
  @SideOnly(Side.CLIENT)
  @SubscribeEvent
  public static void registerRenders(ModelRegistryEvent event)
  {
    for (IFeatureRegistry fr: SRS.featureRegistries)
    {
      fr.registerRenders();
    }
  }
}
