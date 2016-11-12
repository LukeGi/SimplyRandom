package bluemonster122.simplethings.feature.treefarm;

import bluemonster122.simplethings.feature.Feature;
import bluemonster122.simplethings.handler.GuiHandler;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.terraingen.SaplingGrowTreeEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;

public class FeatureTreeFarm extends Feature
{
    // INFO
    public static final String TREE_FARM_FEATURE_NAME = "Tree Farm";

    // CONFIGS
    public static boolean LOAD_TREE_FARM = true;
    public static int ENERGY_CONSUMPTION_PER_BLOCK_PLACE = 20;
    public static int ENERGY_CONSUMPTION_PER_BLOCK_BREAK = 100;

    // CONTENT
    public static final Block TREE_FARM = new BlockTreeFarm();
    public static final Item TREE_FARM_ITEM = new ItemBlockTreeFarm(TREE_FARM);
    public static final GuiHandler.GuiInfo TREE_FARM_GUI;

    static
    {
        //noinspection NewExpressionSideOnly
        TREE_FARM_GUI = GuiHandler.addGui("tree_farm", (p, t) -> new ContainerTreeFarm(p, (TileEntityTreeFarm) t), FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT ? ((p, t) -> new GuiTreeFarm(p, (TileEntityTreeFarm) t)) : null);
    }

    // METHODS
    @Override
    public boolean shouldLoad()
    {
        return LOAD_TREE_FARM;
    }

    @Override
    public void registerBlocks(RegistryEvent.Register<Block> event)
    {
        event.getRegistry().registerAll(
                TREE_FARM
        );
    }

    @Override
    public void registerItems(RegistryEvent.Register<Item> event)
    {
        event.getRegistry().registerAll(
                TREE_FARM_ITEM
        );
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerModels(ModelRegistryEvent event)
    {
        super.defaultModelRegister(TREE_FARM_ITEM);
    }

    @Override
    public void registerTileEntities()
    {
        GameRegistry.registerTileEntity(TileEntityTreeFarm.class, TREE_FARM.getRegistryName().getResourcePath());
    }

    @Override
    public void registerEventHandlers()
    {
        MinecraftForge.TERRAIN_GEN_BUS.register(TreeHandler.class);
    }

    @Override
    public String getName()
    {
        return TREE_FARM_FEATURE_NAME;
    }

    @Override
    public void setShouldLoad(boolean shouldLoad)
    {
        LOAD_TREE_FARM = shouldLoad;
    }

    @Override
    public void registerRecipes()
    {
        GameRegistry.addShapedRecipe(new ItemStack(TREE_FARM, 1), "SAS", "IOI", "SAS", 'S', new ItemStack(Blocks.SAPLING, 1, OreDictionary.WILDCARD_VALUE), 'A', new ItemStack(Items.IRON_AXE, 1), 'I', new ItemStack(Blocks.IRON_BLOCK, 1), 'O', new ItemStack(Blocks.OBSIDIAN, 1));
    }

    @Override
    public void loadConfigs(Configuration configuration)
    {
        super.loadConfigs(configuration);
        ENERGY_CONSUMPTION_PER_BLOCK_BREAK = configuration.getInt("Energy for block break", Configuration.CATEGORY_GENERAL, 100, 0, 1000, "The amount of energy used by the tree farm to break a block");
        ENERGY_CONSUMPTION_PER_BLOCK_PLACE = configuration.getInt("Energy for block place", Configuration.CATEGORY_GENERAL, 20, 0, 1000, "The amount of energy used by the tree farm to place a block");
    }

    public static class TreeHandler
    {
        @SubscribeEvent
        public static void stopGrowth(SaplingGrowTreeEvent evt)
        {
            for (int i = -3; i <= 3 && !evt.getResult().equals(Event.Result.DENY); i++)
            {
                for (int j = -3; j <= 3; j++)
                {
                    if (evt.getWorld().getTileEntity(evt.getPos().add(i, 0, j)) instanceof TileEntityTreeFarm)
                    {
                        evt.setResult(Event.Result.DENY);
                        break;
                    }
                }
            }
        }
    }
}
