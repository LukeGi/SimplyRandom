package bluemonster122.simplethings.handler;

import bluemonster122.simplethings.feature.Feature;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Mod.EventBusSubscriber
public class FeatureHandler
{
    public static List<Feature> features = new ArrayList<>();

    public static boolean addFeature(Feature feature){
        if (feature.shouldLoad()){
            features.add(feature);
        }
        return feature.shouldLoad();
    }

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        forEach(feature -> feature.registerBlocks(event));
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        forEach(feature -> feature.registerItems(event));
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent event){
        forEachParallel(feature -> feature.registerModels(event));
    }

    public static void preInit(FMLPreInitializationEvent event)
    {
        forEachParallel(feature -> feature.preInit(event));
    }

    public static void init(FMLInitializationEvent event)
    {
        forEachParallel(feature -> feature.init(event));
    }

    public static void postInit(FMLPostInitializationEvent event)
    {
        forEachParallel(feature -> feature.postInit(event));
    }

    public static void loadConfigs(Configuration config) {
        forEach(feature -> feature.loadConfigs(config));
    }

    public static void forEachParallel(Consumer<Feature> consumer) {
        features.parallelStream().forEach(consumer);
    }

    public static void forEach(Consumer<Feature> consumer) {
        features.forEach(consumer);
    }
}
