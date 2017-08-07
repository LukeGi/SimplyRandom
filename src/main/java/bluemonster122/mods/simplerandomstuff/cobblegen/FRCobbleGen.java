package bluemonster122.mods.simplerandomstuff.cobblegen;

import bluemonster122.mods.simplerandomstuff.core.block.BlockSRS;
import bluemonster122.mods.simplerandomstuff.reference.ModInfo;
import bluemonster122.mods.simplerandomstuff.reference.Names;
import bluemonster122.mods.simplerandomstuff.util.IFeatureRegistry;
import static bluemonster122.mods.simplerandomstuff.util.ModelHelpers.registerBlockModelAsItem;
import net.minecraft.block.Block;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.registries.IForgeRegistry;

public class FRCobbleGen
  implements IFeatureRegistry
{
  public static final FRCobbleGen INSTANCE = new FRCobbleGen();
  
  private FRCobbleGen()
  {
  }
  
  @Override
  public void registerBlocks(IForgeRegistry<Block> registry)
  {
    registry.registerAll(cobblestone_generator);
  }
  
  @Override
  public void registerItems(IForgeRegistry<Item> registry)
  {
    registry.registerAll(cobblestone_generator.createItemBlock());
  }
  
  @Override
  public void registerRecipes(IForgeRegistry<IRecipe> registry)
  {
    //@formatter:off
    
    registry.registerAll(new ShapedOreRecipe(new ResourceLocation(ModInfo.MOD_ID, "blah"),
                                             new ItemStack(cobblestone_generator),
                                             "PPP",
                                             "WCL",
                                             "PPP",
                                             'W',
                                             new ItemStack(Items.WATER_BUCKET, 1),
                                             'C',
                                             "cobblestone",
                                             'L',
                                             new ItemStack(Items.LAVA_BUCKET, 1),
                                             'P',
                                             new ItemStack(Items.IRON_PICKAXE, 1)
    ).setRegistryName("cobblegen"));
    
    //@formatter:on
  }
  
  @Override
  public void registerTileEntities()
  {
    GameRegistry.registerTileEntity(TileCobblestoneGenerator.class, "simplerandomstuff:cobblestone_generator");
  }
  
  @Override
  public void loadConfigs(Configuration configuration)
  {
    Cobble_RF = configuration.getInt(
      Names.Features.Configs.COBBLE_GEN_RF_COST,
      Names.Features.COBBLESTONE_GENERATOR,
      0,
      0,
      1000,
      "If set to 0, the cobblestone is free."
    );
  }
  
  @Override
  public void registerEvents()
  {
  
  }
  
  @Override
  public void registerOreDict()
  {
  
  }
  
  @SideOnly(Side.CLIENT)
  @Override
  public void registerRenders()
  {
    registerBlockModelAsItem(cobblestone_generator);
  }
  
  @SideOnly(Side.CLIENT)
  @Override
  public void registerClientEvents()
  {
  
  }
  
  @Override
  public String getName()
  {
    return Names.Features.COBBLESTONE_GENERATOR;
  }
  
  public static int      Cobble_RF             = 0;
  
  public static BlockSRS cobblestone_generator = new BlockCobblestoneGenerator();
}
