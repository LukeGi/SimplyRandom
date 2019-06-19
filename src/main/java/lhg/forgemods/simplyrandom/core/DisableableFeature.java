package lhg.forgemods.simplyrandom.core;

import com.google.common.collect.Maps;
import lhg.forgemods.simplyrandom.cobblemaker.CobblestoneMaker;
import lhg.forgemods.simplyrandom.miner.Miner;
import lhg.forgemods.simplyrandom.treefarm.TreeFarm;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * This exists to make it easier to disable recipes based on config options.
 * Since it has to exist anyway, it is also where all registry happens
 */
public abstract class DisableableFeature
{
    /**
     * This is the storage place for all features
     */
    public static final Map<ResourceLocation, DisableableFeature> FEATURE_REGISTRY = Maps.newHashMap();
    /**
     * LOGGER
     */
    private static final Logger LOGGER = LogManager.getLogger();
    /**
     * This is a ResourceLocation Cache for the mod, to attempt a decrease in memory usage
     */
    private static final HashMap<String, ResourceLocation> NAMES = new HashMap<>();

    static
    {
        new CobblestoneMaker();
        new TreeFarm();
        new Miner();
    }

    /**
     * This constructor registers the feature too all relevant events and adds itself to the feature registry.
     */
    public DisableableFeature()
    {
        FMLJavaModLoadingContext.get().getModEventBus().addGenericListener(Block.class, this::onRegisterBlocks);
        FMLJavaModLoadingContext.get().getModEventBus().addGenericListener(Item.class, this::onRegisterItems);
        FMLJavaModLoadingContext.get().getModEventBus().addGenericListener(TileEntityType.class, this::onRegisterTileEntityType);
        FEATURE_REGISTRY.put(name(), this);
    }

    public static Collection<ResourceLocation> getRegisteredNames()
    {
        return NAMES.values();
    }

    /**
     * This method attempts to use the ResourceLocation cache if the parameter {@code name} has already
     * been passed to it before.
     *
     * @param name usually the Path half of a resource location
     * @return a valid ResourceLocation
     */
    protected static ResourceLocation getOrCreateName(String name)
    {
        if (!name.startsWith("simplyrandom:"))
        {
            name = "simplyrandom:" + name;
        }
        return NAMES.computeIfAbsent(name, ResourceLocation::new);
    }

    /**
     * @param registry The registry to register the registry object to
     * @param name     The name of the registry object, e.g. {@code "example_block"}
     * @param object   The registry object being registered to {@code registry}
     * @param <R>      Registry type
     * @param <T>      Registry object type
     */
    protected static <R extends IForgeRegistry<T>, T extends IForgeRegistryEntry<T>> void register(final R registry, String name, final T object)
    {
        registry.register(object.setRegistryName(getOrCreateName(name)));
    }

    public static void init()
    {
        LOGGER.info("Features being registered");
    }

    /**
     * When this method is overridden, it receives the registry event
     *
     * @param event Block Registry event
     */
    public abstract void onRegisterBlocks(final Register<Block> event);

    /**
     * When this method is overridden, it receives the registry event
     *
     * @param event Item Registry event
     */
    public abstract void onRegisterItems(final Register<Item> event);

    /**
     * When this method is overridden, it receives the registry event
     *
     * @param event TileEntityType Registry event
     */
    public abstract void onRegisterTileEntityType(final Register<TileEntityType<?>> event);

    /**
     * @return true if all recipes related to this feature should be enabled
     */
    public abstract boolean enabled();

    /**
     * @return the reference ResourceLocation that can be used by {@link FeatureEnabledCondition} to
     * identify this feature
     */
    public abstract ResourceLocation name();
}
