package bluemonster122.simplethings.feature;

import bluemonster122.simplethings.util.IInitialize;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class Feature implements IInitialize
{
    public abstract boolean shouldLoad();
    public abstract void registerBlocks(RegistryEvent.Register<Block> event);
    public abstract void registerItems(RegistryEvent.Register<Item> event);
    @SideOnly(Side.CLIENT)
    public abstract void registerModels(ModelRegistryEvent event);
    public abstract void registerTileEntities();
    public abstract void registerEventHandlers();
    public abstract String getName();
    public abstract void setShouldLoad(boolean shouldLoad);
    public abstract void registerRecipes();

    @Override
    public void preInit(FMLPreInitializationEvent event)
    {
        registerTileEntities();
        registerEventHandlers();
    }

    @Override
    public void init(FMLInitializationEvent event)
    {
        registerRecipes();
    }

    @Override
    public void postInit(FMLPostInitializationEvent event)
    {

    }

    public void loadConfigs(Configuration configuration) {
        setShouldLoad(configuration.getBoolean("Enable Feature" + getName(), Configuration.CATEGORY_GENERAL, true, "Set to false to disable the " + getName() + " feature"));
    }

    @SideOnly(Side.CLIENT)
    public void defaultModelRegister(Item item)
    {
        ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName(), "inventory"));
    }
}
