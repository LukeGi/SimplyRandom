package bluemonster122.simplethings.feature.cobblegen;

import bluemonster122.simplethings.feature.Feature;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class FeatureCobblestoneGenerator extends Feature
{
    // INFO
    public static final String COBBLE_GEN_FEATURE_NAME = "Cobblestone Generator";

    // CONFIGS
    public static boolean LOAD_COBBLE_GEN = true;
    public static boolean COBBLE_GEN_REQ_POWER = true;

    // CONTENT
    public static final Block COBBLESTONE_GENERATOR = new BlockCobblestoneGenerator();
    public static final Item COBBLESTONE_GENERATOR_ITEM = new ItemBlockCobblestoneGenerator();

    // METHODS
    @Override
    public boolean shouldLoad()
    {
        return LOAD_COBBLE_GEN;
    }

    @Override
    public void registerBlocks(RegistryEvent.Register<Block> event)
    {
        event.getRegistry().registerAll(
                COBBLESTONE_GENERATOR
        );
    }

    @Override
    public void registerItems(RegistryEvent.Register<Item> event)
    {
        event.getRegistry().registerAll(
                COBBLESTONE_GENERATOR_ITEM
        );
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerModels(ModelRegistryEvent event)
    {
        super.defaultModelRegister(COBBLESTONE_GENERATOR_ITEM);
    }

    @Override
    public void registerTileEntities()
    {
        GameRegistry.registerTileEntity(TileEntityCobblestoneGenerator.class, COBBLESTONE_GENERATOR.getRegistryName().getResourcePath());
    }

    @Override
    public void registerEventHandlers()
    {

    }

    @Override
    public String getName()
    {
        return COBBLE_GEN_FEATURE_NAME;
    }

    @Override
    public void setShouldLoad(boolean shouldLoad)
    {
        LOAD_COBBLE_GEN = shouldLoad;
    }

    @Override
    public void registerRecipes()
    {
        GameRegistry.addShapedRecipe(new ItemStack(COBBLESTONE_GENERATOR, 1), "PPP", "WCL", "PPP", 'W', new ItemStack(Items.WATER_BUCKET, 1), 'C', new ItemStack(Blocks.COBBLESTONE, 1), 'L', new ItemStack(Items.LAVA_BUCKET, 1), 'P', new ItemStack(Items.IRON_PICKAXE, 1));
    }

    @Override
    public void loadConfigs(Configuration configuration)
    {
        super.loadConfigs(configuration);
        COBBLE_GEN_REQ_POWER = configuration.getBoolean("Cobble Generator Requires Power", Configuration.CATEGORY_GENERAL, true, "Set to false to make the Cobble Generator require no power.");
    }
}
