package bluemonster122.mods.simplerandomstuff.workbench;

import bluemonster122.mods.simplerandomstuff.core.block.BlockSRS;
import bluemonster122.mods.simplerandomstuff.reference.ModInfo;
import bluemonster122.mods.simplerandomstuff.reference.Names;
import bluemonster122.mods.simplerandomstuff.util.IFeatureRegistry;
import static bluemonster122.mods.simplerandomstuff.util.ModelHelpers.registerBlockModelAsItem;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
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
import net.minecraftforge.oredict.ShapelessOreRecipe;
import net.minecraftforge.registries.IForgeRegistry;

public class FRCrafters
  implements IFeatureRegistry
{
  public static final FRCrafters INSTANCE = new FRCrafters();
  
  public BlockSRS crafting_table = new BlockCraftingTable();
  
  public BlockSRS crafting_table_auto = new BlockCraftingTable.BlockCraftingTableAuto();
  
  private FRCrafters()
  {
  }
  
  @Override
  public void registerBlocks(IForgeRegistry<Block> registry)
  {
    registry.registerAll(crafting_table, crafting_table_auto);
  }
  
  @Override
  public void registerItems(IForgeRegistry<Item> registry)
  {
    registry.registerAll(crafting_table.createItemBlock(), crafting_table_auto.createItemBlock());
  }
  
  @Override
  public void registerRecipes(IForgeRegistry<IRecipe> registry)
  {
    //@formatter:off
    
    ResourceLocation blah = new ResourceLocation(ModInfo.MOD_ID, "blah");
    
    registry.registerAll(
      new ShapelessOreRecipe(blah, new ItemStack(crafting_table), "pressurePlateWood", "workbench").setRegistryName("workbench"),
      
      new ShapedOreRecipe(blah,
                          new ItemStack(crafting_table_auto),
                          "W",
                          "G",
                          "C",
                          'W',
                          "wrench",
                          'G',
                          "gearStone",
                          'C',
                          new ItemStack(crafting_table)
      ).setRegistryName("auto_workbench")
    );
    
    //@formatter:on
  }
  
  @Override
  public void registerTileEntities()
  {
    GameRegistry.registerTileEntity(TileCraftingTable.class, "simplerandomstuff:crafting_table");
    GameRegistry.registerTileEntity(TileCraftingTable.TileCraftingTableAuto.class,
                                    "simplerandomstuff:crafting_table_auto"
    );
  }
  
  @Override
  public void loadConfigs(Configuration configuration)
  {
  }
  
  @Override
  public void registerEvents()
  {
        /* NO OPERATION */
  }
  
  @Override
  public void registerOreDict()
  {
    OreDictionary.registerOre("workbench", new ItemStack(Blocks.CRAFTING_TABLE));
    OreDictionary.registerOre("pressurePlateWood", new ItemStack(Blocks.WOODEN_PRESSURE_PLATE));
  }
  
  @SideOnly(Side.CLIENT)
  @Override
  public void registerRenders()
  {
    registerBlockModelAsItem(crafting_table);
    registerBlockModelAsItem(crafting_table_auto);
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
    return Names.Features.CRAFTERS;
  }
}
