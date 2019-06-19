package lhg.forgemods.simplyrandom.core;

import lhg.forgemods.simplyrandom.SimplyRandom;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.Item.Properties;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.common.ForgeConfigSpec.Builder;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;

/**
 * This exists to make it easier to disable recipes based on config options.
 * Since it has to exist anyway, it is also where all registry happens
 */
public abstract class DisableableFeature
{
    protected static final Properties DEAFULT_ITEM_PROPS = new Properties().group(SimplyRandom.itemGroup);
    protected BooleanValue enabled;

    /**
     * This constructor registers the feature too all relevant events and adds itself to the feature registry.
     */
    public DisableableFeature()
    {
        FMLJavaModLoadingContext.get().getModEventBus().addGenericListener(Block.class, this::onRegisterBlocks);
        FMLJavaModLoadingContext.get().getModEventBus().addGenericListener(Item.class, this::onRegisterItems);
        FMLJavaModLoadingContext.get().getModEventBus().addGenericListener(TileEntityType.class, this::onRegisterTileEntityType);
        DisableableFeatureRegistry.FEATURE_REGISTRY.put(name(), this);
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
        registry.register(object.setRegistryName(RLProvider.get(name)));
    }

    /**
     * Use to add configs values to the server config spec
     *
     * @param spec server config spec
     */
    protected abstract void constructConfig(Builder spec);

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
    public boolean enabled()
    {
        return enabled.get();
    }

    /**
     * @return the reference ResourceLocation that can be used by {@link FeatureEnabledCondition} to
     * identify this feature
     */
    public abstract ResourceLocation name();
}
