package bluemonster122.mods.simplerandomstuff.generators;

import bluemonster122.mods.simplerandomstuff.core.FRCore;
import bluemonster122.mods.simplerandomstuff.core.ItemMisc;
import bluemonster122.mods.simplerandomstuff.core.block.BlockSRS;
import bluemonster122.mods.simplerandomstuff.generators.BlockGenerator.Types;
import bluemonster122.mods.simplerandomstuff.reference.ModInfo;
import bluemonster122.mods.simplerandomstuff.reference.Names;
import bluemonster122.mods.simplerandomstuff.util.IFeatureRegistry;
import static bluemonster122.mods.simplerandomstuff.util.ModelHelpers.registerIEnumMeta;
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
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.registries.IForgeRegistry;

public class FRGenerators
  implements IFeatureRegistry
{
  public static final FRGenerators INSTANCE = new FRGenerators();
  
  private FRGenerators()
  {
  }
  
  @Override
  public void registerBlocks(IForgeRegistry<Block> registry)
  {
    registry.registerAll(generators);
  }
  
  @Override
  public void registerItems(IForgeRegistry<Item> registry)
  {
    registry.registerAll(generators.createItemBlock());
  }
  
  @Override
  public void registerRecipes(IForgeRegistry<IRecipe> registry)
  {
    //@formatter:off
    
    ResourceLocation blah = new ResourceLocation(ModInfo.MOD_ID, "blah");
    
    registry.registerAll(new ShapedOreRecipe(
      blah,
      new ItemStack(generators, 1, Types.SUGAR.getMeta()),
      "WSW",
      "SMS",
      "WSW",
      'W',
      new ItemStack(FRCore.misc, 1, ItemMisc.Types.WOODEN_GEAR.getMeta()),
      'M',
      new ItemStack(FRCore.misc, 1, ItemMisc.Types.MACHINE_BASE.getMeta()),
      'S',
      Names.OreDict.SUGAR
    ).setRegistryName("sugar_generator"), new ShapedOreRecipe(
      blah,
      new ItemStack(generators, 1, Types.FIRE.getMeta()),
      "ISI",
      "FMF",
      "ISI",
      'I',
      Names.OreDict.IRON_STICK,
      'S',
      new ItemStack(FRCore.misc, 1, ItemMisc.Types.STONE_GEAR.getMeta()),
      'F',
      new ItemStack(Items.FLINT_AND_STEEL),
      'M',
      new ItemStack(FRCore.misc, 1, ItemMisc.Types.MACHINE_BASE.getMeta())
    ).setRegistryName("fire_generator"));
    
    //@formatter:on
  }
  
  @Override
  public void registerTileEntities()
  {
    GameRegistry.registerTileEntity(TileGeneratorSugar.class, "simplerandomstuff:sugar_generator");
    GameRegistry.registerTileEntity(TileGeneratorFire.class, "simplerandomstuff:fire_generator");
  }
  
  @Override
  public void loadConfigs(Configuration configuration)
  {
    Sugar_RF = configuration.getInt(
      Names.Features.Configs.GENERATORS_SUGAR_RFPERT,
      Names.Features.GENERATORS,
      10,
      1,
      Integer.MAX_VALUE,
      "Set to any number larger than 0."
    );
    Sugar_Burntime = configuration.getInt(
      Names.Features.Configs.GENERATORS_SUGAR_BURNTIME,
      Names.Features.GENERATORS,
      10,
      1,
      Integer.MAX_VALUE,
      "Set to any number larger than 0."
    );
    Fire_RF = configuration.getInt(
      Names.Features.Configs.GENERATORS_FIRE_RFPERT,
      Names.Features.GENERATORS,
      1,
      1,
      Integer.MAX_VALUE,
      "Set to any number larger than 0."
    );
  }
  
  @Override
  public void registerEvents()
  {
  
  }
  
  @Override
  public void registerOreDict()
  {
    OreDictionary.registerOre("sugar", new ItemStack(Items.SUGAR));
  }
  
  @SideOnly(Side.CLIENT)
  @Override
  public void registerRenders()
  {
    registerIEnumMeta(generators, Types.VARIANTS);
  }
  
  @SideOnly(Side.CLIENT)
  @Override
  public void registerClientEvents()
  {
  
  }
  
  @Override
  public String getName()
  {
    return Names.Features.GENERATORS;
  }
  
  public static int Fire_RF = 1;
  
  public static int Sugar_Burntime = 10;
  
  public static int Sugar_RF = 10;
  
  public static BlockSRS generators = new BlockGenerator();
}
