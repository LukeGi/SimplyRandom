package luhegi.mods.simplyrandom.basis.setup;

import luhegi.mods.simplyrandom.SimplyRandom;
import luhegi.mods.simplyrandom.basis.config.BasisConfig;
import luhegi.mods.simplyrandom.basis.data.BasisBlockProvider;
import luhegi.mods.simplyrandom.basis.data.BasisItemProvider;
import luhegi.mods.simplyrandom.basis.data.BasisLangProvider;
import luhegi.mods.simplyrandom.basis.data.BasisRecipeProvider;
import luhegi.mods.simplyrandom.cobblegen.CobbleGen;
import luhegi.mods.simplyrandom.treefarm.TreeFarm;
import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.client.model.generators.ExistingFileHelper;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.HashSet;

public class ModSetupManager implements ISetupManager {
    public static final ModSetupManager INSTANCE = new ModSetupManager();
    private final HashSet<ISetupManager> features = new HashSet<>();
    public DataGenerator generator;
    public ExistingFileHelper existingFileHelper;

    @Override
    public void onConstruct() {
        // Construct each feature
        features.add(CobbleGen.INSTANCE);
        features.add(TreeFarm.INSTANCE);
        features.forEach(ISetupManager::onConstruct);
        // Setup the config
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, BasisConfig.clientSpec);
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, BasisConfig.serverSpec);
        // Register all the features to events.
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        modBus.addListener((FMLCommonSetupEvent event) -> this.onSetup());
        modBus.addGenericListener(Block.class, (RegistryEvent.Register<Block> event) -> this.registerBlocks(event.getRegistry()));
        modBus.addGenericListener(Item.class, (RegistryEvent.Register<Item> event) -> this.registerItems(event.getRegistry()));
        modBus.addGenericListener(TileEntityType.class, (RegistryEvent.Register<TileEntityType<?>> event) -> this.registerTileType(event.getRegistry()));
        modBus.addListener((GatherDataEvent event) -> {
            this.generator = event.getGenerator();
            this.existingFileHelper = event.getExistingFileHelper();
            this.generateData();
        });
    }

    @Override
    public void onClientConfig(final ForgeConfigSpec.Builder spec) {
        features.forEach(features -> features.onClientConfig(spec));
    }

    @Override
    public void onServerConfig(final ForgeConfigSpec.Builder spec) {
        features.forEach(features -> features.onServerConfig(spec));
    }

    @Override
    public void onSetup() {
        features.forEach(ISetupManager::onSetup);
    }

    @Override
    public void registerBlocks(IForgeRegistry<Block> registry) {
        features.forEach(manager -> manager.registerBlocks(registry));
    }

    @Override
    public void registerItems(IForgeRegistry<Item> registry) {
        features.forEach(manager -> manager.registerItems(registry));
    }

    @Override
    public void registerTileType(IForgeRegistry<TileEntityType<?>> registry) {
        features.forEach(manager -> manager.registerTileType(registry));
    }

    @Override
    public void generateData() {
        features.forEach(ISetupManager::generateData);
        BasisLangProvider.EN_US.add("itemGroup.simplyrandom", "Simply Random Tab");
        generator.addProvider(BasisBlockProvider.INSTANCE);
        generator.addProvider(BasisItemProvider.INSTANCE);
        generator.addProvider(BasisLangProvider.EN_US);
        generator.addProvider(BasisRecipeProvider.INSTANCE);
    }

    @Override
    public String getID() {
        return SimplyRandom.ID;
    }

    @Override
    public String getName() {
        return SimplyRandom.NAME;
    }
}
