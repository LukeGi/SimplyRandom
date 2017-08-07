package bluemonster122.mods.simplerandomstuff.core;

import static bluemonster122.mods.simplerandomstuff.core.ItemMisc.Types.*;
import bluemonster122.mods.simplerandomstuff.core.block.BlockSRS;
import bluemonster122.mods.simplerandomstuff.reference.ModInfo;
import bluemonster122.mods.simplerandomstuff.reference.Names;
import bluemonster122.mods.simplerandomstuff.reference.Names.OreDict;
import bluemonster122.mods.simplerandomstuff.util.IFeatureRegistry;
import static bluemonster122.mods.simplerandomstuff.util.ModelHelpers.registerIEnumMeta;
import static bluemonster122.mods.simplerandomstuff.util.ModelHelpers.registerItemModel;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.registries.IForgeRegistry;

import javax.annotation.Nullable;

public class FRCore
  implements IFeatureRegistry
{
  
  public static final FRCore INSTANCE = new FRCore();
  
  public static final ItemST misc = new ItemMisc();
  
  public static final ItemWrench wrench = new ItemWrench();
  
  public static final BlockSRS test = new BlockSRS("test", Material.CLOTH)
  {
    @Override
    public boolean hasTileEntity(IBlockState state)
    {
      return true;
    }
    
    @Nullable
    @Override
    public TileEntity createTileEntity(
      World world, IBlockState state
    )
    {
      return new TileEntityTickingSRS();
    }
  };
  
  private FRCore()
  {
  }
  
  @Override
  public void registerBlocks(IForgeRegistry<Block> registry)
  {
    registry.registerAll(test);
        /* NO OPERATION */
  }
  
  @Override
  public void registerItems(IForgeRegistry<Item> registry)
  {
    registry.registerAll(wrench, misc, test.createItemBlock());
  }
  
  @Override
  public void registerRecipes(IForgeRegistry<IRecipe> registry)
  {
    //@formatter:off
    ResourceLocation blah = new ResourceLocation(ModInfo.MOD_ID, "blah");
    registry.registerAll(new ShapedOreRecipe(blah,
                                             new ItemStack(wrench, 1),
                                             " I ",
                                             " SI",
                                             "S  ",
                                             'S',
                                             OreDict.IRON_STICK,
                                             'I',
                                             "ingotIron"
                         ).setMirrored(true)
                          .setRegistryName("wrench"),
                         new ShapedOreRecipe(blah,
                                             new ItemStack(misc, 8, IRON_ROD.getMeta()),
                                             "I",
                                             "I",
                                             'I',
                                             "ingotIron"
                         ).setRegistryName("iron_rod"),
                         new ShapedOreRecipe(blah,
                                             new ItemStack(misc, 1, WOODEN_GEAR.getMeta()),
                                             "MMM",
                                             "MSM",
                                             "MMM",
                                             'M',
                                             "stickWood",
                                             'S',
                                             OreDict.IRON_STICK
                         ).setRegistryName("wooden_gear"),
                         new ShapedOreRecipe(blah,
                                             new ItemStack(misc, 1, STONE_GEAR.getMeta()),
                                             " M ",
                                             "MSM",
                                             " M ",
                                             'M',
                                             "cobblestone",
                                             'S',
                                             OreDict.IRON_STICK
                         ).setRegistryName("stone_gear"),
                         new ShapedOreRecipe(blah,
                                             new ItemStack(misc, 1, MACHINE_BASE.getMeta()),
                                             "SWS",
                                             "WIW",
                                             "SWS",
                                             'W',
                                             OreDict.IRON_STICK,
                                             'S',
                                             new ItemStack(misc, 1, STONE_GEAR.getMeta()),
                                             'I',
                                             OreDict.IRON_BLOCK
                         ).setRegistryName("machine_base"),
                         new ShapedOreRecipe(blah,
                                             new ItemStack(misc, 1, COMPLEX_MACHINE_BASE.getMeta()),
                                             "MMM",
                                             "MWM",
                                             "MMM",
                                             'M',
                                             new ItemStack(misc, 1, MACHINE_BASE.getMeta()),
                                             'W',
                                             "wrench"
                         ).setRegistryName("complex_machine_base")
    );
    
    //@formatter:on
  }
  
  @Override
  public void registerTileEntities()
  {
    GameRegistry.registerTileEntity(TileEntityTickingSRS.class, "test");
        /* NO OPERATION */
  }
  
  @Override
  public void loadConfigs(Configuration configuration)
  {
        /* NO OPERATION */
  }
  
  @Override
  public void registerEvents()
  {
        /* NO OPERATION */
  }
  
  @Override
  public void registerOreDict()
  {
    OreDictionary.registerOre("stickIron", new ItemStack(misc, 1, IRON_ROD.getMeta()));
    OreDictionary.registerOre("wrench", new ItemStack(wrench));
    OreDictionary.registerOre("gearWood", new ItemStack(misc, 1, WOODEN_GEAR.getMeta()));
    OreDictionary.registerOre("gearWooden", new ItemStack(misc, 1, WOODEN_GEAR.getMeta()));
    OreDictionary.registerOre("gearStone", new ItemStack(misc, 1, STONE_GEAR.getMeta()));
  }
  
  @SideOnly(Side.CLIENT)
  @Override
  public void registerRenders()
  {
    registerItemModel(wrench);
    registerIEnumMeta(misc, ItemMisc.Types.VARIANTS);
  }
  
  @SideOnly(Side.CLIENT)
  @Override
  public void registerClientEvents()
  {
        /* NO OPERATION */
  }
  
  @Override
  public String getName()
  {
    return Names.Features.CORE;
  }
}
