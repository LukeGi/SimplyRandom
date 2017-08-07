package bluemonster122.mods.simplerandomstuff.miner;

import bluemonster122.mods.simplerandomstuff.core.FRCore;
import bluemonster122.mods.simplerandomstuff.core.ItemMisc;
import bluemonster122.mods.simplerandomstuff.core.block.BlockSRS;
import bluemonster122.mods.simplerandomstuff.reference.ModInfo;
import bluemonster122.mods.simplerandomstuff.reference.Names;
import bluemonster122.mods.simplerandomstuff.util.IFeatureRegistry;
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

public class FRMiner
  implements IFeatureRegistry
{
  public static final FRMiner INSTANCE = new FRMiner();
  
  private FRMiner()
  {
  }
  
  @Override
  public void registerBlocks(IForgeRegistry<Block> registry)
  {
    registry.registerAll(miner);
  }
  
  @Override
  public void registerItems(IForgeRegistry<Item> registry)
  {
    registry.registerAll(miner.createItemBlock());
  }
  
  @Override
  public void registerRecipes(IForgeRegistry<IRecipe> registry)
  {
    //@formatter:off
    
    registry.registerAll(new ShapedOreRecipe(new ResourceLocation(ModInfo.MOD_ID, "blah"),
                                             new ItemStack(miner, 1),
                                             "RGR",
                                             "GCG",
                                             "IDI",
                                             'R',
                                             "dustGlowstone",
                                             'G',
                                             "gearStone",
                                             'C',
                                             new ItemStack(FRCore.misc,
                                                           1,
                                                           ItemMisc.Types.COMPLEX_MACHINE_BASE.getMeta()
                                             ),
                                             'I',
                                             new ItemStack(Items.DIAMOND_PICKAXE),
                                             'D',
                                             new ItemStack(Items.GOLDEN_PICKAXE)
    ).setRegistryName("miner"));
    
    //@formatter:on
  }
  
  @Override
  public void registerTileEntities()
  {
    GameRegistry.registerTileEntity(TileMiner.class, "simplethings:miner");
  }
  
  @Override
  public void loadConfigs(Configuration configuration)
  {
    SCAN_POWER = configuration.getInt(Names.Features.Configs.MINER_SCAN_POWER,
                                      getName(),
                                      10,
                                      0,
                                      5000,
                                      "This is the amount of power the miner uses to scan a block. \nThe limit is 5000 as that is the size of the machine's internal battery. \nIf set to 0, the scanning of a block will be free."
    );
    BREAK_POWER = configuration.getInt(Names.Features.Configs.MINER_BREAK_POWER,
                                       getName(),
                                       100,
                                       0,
                                       5000,
                                       "This is the amount of power the miner uses to break a block. \nThe limit is 5000 as that is the size of the machine's internal battery. \nIf set to 0, the breaking of a block will be free"
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
  
  }
  
  @SideOnly(Side.CLIENT)
  @Override
  public void registerClientEvents()
  {
  
  }
  
  @Override
  public String getName()
  {
    return Names.Features.MINER;
  }
  
  public static int SCAN_POWER = 0;
  
  public static int BREAK_POWER = 0;
  
  public static BlockSRS miner = new BlockMiner();
}
