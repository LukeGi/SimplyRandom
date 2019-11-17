package luhegi.simplyrandom;

import com.google.common.collect.Sets;
import luhegi.simplyrandom.block.CobblestoneGeneratorBlock;
import luhegi.simplyrandom.block.TreeFarmBlock;
import luhegi.simplyrandom.config.Config;
import luhegi.simplyrandom.tile.CobblestoneGeneratorTile;
import luhegi.simplyrandom.tile.TreeFarmTile;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.data.DataGenerator;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.client.model.generators.ExistingFileHelper;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

@Mod(SimplyRandom.ID) //TODO: figure out maven if ewy hasn't
public class SimplyRandom {
    public static final String ID = "simplyrandom";
    public static final Config config = new Config();
    private final ItemGroup simply_random_group = new ItemGroup(ID) {
        @Override
        public ItemStack createIcon() { // Try cycling the item
            return new ItemStack(cobblestone_generator_item.get());
        }
    };

    public final RegistryObject<CobblestoneGeneratorBlock> cobblestone_generator_block;
    public final RegistryObject<BlockItem> cobblestone_generator_item;
    public final RegistryObject<TileEntityType<CobblestoneGeneratorTile>> cobblestone_generator_tile;
    public final RegistryObject<TreeFarmBlock> tree_farm_block;
    public final RegistryObject<BlockItem> tree_farm_item;
    public final RegistryObject<TileEntityType<TreeFarmTile>> tree_farm_tile;

    public static SimplyRandom instance;
    final DeferredRegister<Block> BLOCKS;
    final DeferredRegister<Item> ITEMS;
    final DeferredRegister<TileEntityType<?>> TILES;

    public SimplyRandom() {
        instance = this;
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, config.getSpec());
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        BLOCKS = new DeferredRegister<>(ForgeRegistries.BLOCKS, ID);
        BLOCKS.register(modBus);
        ITEMS = new DeferredRegister<>(ForgeRegistries.ITEMS, ID);
        ITEMS.register(modBus);
        TILES = new DeferredRegister<>(ForgeRegistries.TILE_ENTITIES, ID);
        TILES.register(modBus);
        modBus.addListener(this::dataGen);

        // REGISTER COBBLESTONE GENERATOR
        String cobblestoneGeneratorName = "cobblestone_generator";
        this.cobblestone_generator_block = BLOCKS.register(cobblestoneGeneratorName,
                () -> new CobblestoneGeneratorBlock(Block.Properties.from(Blocks.IRON_BLOCK)));
        this.cobblestone_generator_item = ITEMS.register(cobblestoneGeneratorName,
                () -> new BlockItem(this.cobblestone_generator_block.get(), new Item.Properties().maxStackSize(1).group(simply_random_group)));
        this.cobblestone_generator_tile = TILES.register(cobblestoneGeneratorName,
                () -> new TileEntityType<>(CobblestoneGeneratorTile::new, Sets.newHashSet(this.cobblestone_generator_block.get()), null));

        // REGISTER TREE FARM
        String treeFarmName = "tree_farm";
        this.tree_farm_block = BLOCKS.register(treeFarmName,
                () -> new TreeFarmBlock(Block.Properties.from(Blocks.QUARTZ_BLOCK)));
        this.tree_farm_item = ITEMS.register(treeFarmName,
                () -> new BlockItem(this.tree_farm_block.get(), new Item.Properties().maxStackSize(1).group(simply_random_group)));
        this.tree_farm_tile = TILES.register(treeFarmName,
                () -> new TileEntityType<>(TreeFarmTile::new, Sets.newHashSet(this.tree_farm_block.get()), null));
    }

private void dataGen(final GatherDataEvent event) {
    final DataGenerator gen = event.getGenerator();
    final ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
    if (event.includeServer()) {
        gen.addProvider(new DataGenerators.Loots(gen));
        gen.addProvider(new DataGenerators.Recipes(gen));
    }
    if (event.includeClient()) {
        gen.addProvider(new DataGenerators.BlockStates(gen, ID, existingFileHelper));
        gen.addProvider(new DataGenerators.ItemModels(gen, ID, existingFileHelper));
        gen.addProvider(new DataGenerators.Lang(gen, ID));
    }
}
}
