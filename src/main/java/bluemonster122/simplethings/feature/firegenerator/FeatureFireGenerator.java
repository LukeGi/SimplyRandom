package bluemonster122.simplethings.feature.firegenerator;

import bluemonster122.simplethings.feature.Feature;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class FeatureFireGenerator extends Feature
{
    // INFO
    public static final String FIRE_GEN_FEATURE_NAME = "Fire Energy Harness";

    // CONFIGS
    public static boolean LOAD_FIRE_GEN = true;

    //CONTENT
    public static final Block FIRE_GENERATOR = new BlockFireGenerator();
    public static final ItemBlock FIRE_GENERATOR_ITEM = new ItemBlockFireGenerator();

    // METHODS

    @Override
    public boolean shouldLoad()
    {
        return LOAD_FIRE_GEN;
    }

    @Override
    public void registerBlocks(RegistryEvent.Register<Block> event)
    {
        event.getRegistry().registerAll(
                FIRE_GENERATOR
        );
    }

    @Override
    public void registerItems(RegistryEvent.Register<Item> event)
    {
        event.getRegistry().registerAll(
                FIRE_GENERATOR_ITEM
        );
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerModels(ModelRegistryEvent event)
    {
        super.defaultModelRegister(FIRE_GENERATOR_ITEM);
    }

    @Override
    public void registerTileEntities()
    {
        GameRegistry.registerTileEntity(TileEntityFireGenerator.class, FIRE_GENERATOR.getRegistryName().getResourcePath());
    }

    @Override
    public void registerEventHandlers()
    {

    }

    @Override
    public String getName()
    {
        return FIRE_GEN_FEATURE_NAME;
    }

    @Override
    public void setShouldLoad(boolean shouldLoad)
    {
        LOAD_FIRE_GEN = shouldLoad;
    }

    @Override
    public void registerRecipes()
    {

    }

    @Override
    public void loadConfigs(Configuration configuration)
    {
        super.loadConfigs(configuration);
    }
}
