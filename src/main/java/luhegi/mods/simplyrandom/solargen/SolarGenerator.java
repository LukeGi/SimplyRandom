package luhegi.mods.simplyrandom.solargen;

import luhegi.mods.simplyrandom.SimplyRandom;
import luhegi.mods.simplyrandom.basis.data.BasisBlockProvider;
import luhegi.mods.simplyrandom.basis.data.BasisItemProvider;
import luhegi.mods.simplyrandom.basis.data.BasisLangProvider;
import luhegi.mods.simplyrandom.basis.data.BasisRecipeProvider;
import luhegi.mods.simplyrandom.basis.objects.BasisTileType;
import luhegi.mods.simplyrandom.basis.setup.Setup;
import luhegi.mods.simplyrandom.basis.setup.SetupManager;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.ShapedRecipeBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.Tags;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.function.Consumer;

import static luhegi.mods.simplyrandom.solargen.SolarGeneratorBlock.POWERED;

@Setup
public class SolarGenerator extends SetupManager {
    public static final SolarGenerator INSTANCE = new SolarGenerator();

    public SolarGeneratorBlock block;
    public SolarGeneratorItem item;
    public BasisTileType<SolarGeneratorTile> tileType;

    private ForgeConfigSpec.IntValue maxStorage;
    private ForgeConfigSpec.IntValue energyPerTick;
    private ForgeConfigSpec.DoubleValue waterloggedMultipler;

    public static int getMaxStorage() {
        return INSTANCE.maxStorage.get();
    }

    public static int getEnergyPerTick() {
        return INSTANCE.energyPerTick.get();
    }

    public static double getWaterloggedMultiplier() {
        return INSTANCE.waterloggedMultipler.get();
    }

    @Override
    public void registerBlocks(IForgeRegistry<Block> registry) {
        block = register(registry, new SolarGeneratorBlock(Block.Properties.from(Blocks.QUARTZ_BLOCK)));
    }

    @Override
    public void registerItems(IForgeRegistry<Item> registry) {
        item = register(registry, new SolarGeneratorItem(block));
    }

    @Override
    public void registerTileType(IForgeRegistry<TileEntityType<?>> registry) {
        tileType = register(registry, new BasisTileType<>(SolarGeneratorTile::new, block));
    }

    @Override
    protected void generateBlockData(BasisBlockProvider provider) {
        ModelFile activeMode = provider.models().getExistingFile(provider.modLoc("block/" + block.getRegistryName().getPath() + "_active"));
        ModelFile unactiveMode = provider.getBlockModel(block);
        provider.getVariantBuilder(block)
                .partialState().with(POWERED, true).modelForState().modelFile(activeMode).addModel()
                .partialState().with(POWERED, false).modelForState().modelFile(unactiveMode).addModel();
    }

    @Override
    protected void generateItemData(BasisItemProvider provider) {
        provider.getBuilder(getID()).parent(provider.getBlockModel(block));
    }

    @Override
    protected void generateEnUsLangData(BasisLangProvider provider) {
        provider.add(block, getName());
        provider.addTooltip(block, "Generates energy when it can see the sun.");
        provider.add(block.getTranslationKey() + ".tooltip.generates", "Generates: {0} energy per tick");
    }

    @Override
    protected void generateRecipeData(BasisRecipeProvider provider, Consumer<IFinishedRecipe> consumer) {
        ShapedRecipeBuilder.shapedRecipe(block, 1)
                .patternLine("LGL")
                .patternLine("IDI")
                .patternLine("III")
                .key('I', Tags.Items.INGOTS_IRON)
                .key('D', Items.DAYLIGHT_DETECTOR)
                .key('L', Tags.Items.STORAGE_BLOCKS_LAPIS)
                .key('G', Tags.Items.GLASS)
                .setGroup(SimplyRandom.ID + ":" + getID())
                .addCriterion("has_daylight_detector", provider.hasItem(Items.DAYLIGHT_DETECTOR))
                .addCriterion("has_lapis_block", provider.hasItem(Tags.Items.STORAGE_BLOCKS_LAPIS))
                .build(consumer);
    }

    @Override
    protected void addClientConfigs(ForgeConfigSpec.Builder spec) {

    }

    @Override
    protected void addServerConfigs(ForgeConfigSpec.Builder spec) {
        maxStorage = spec.comment("The most energy that can be stored in the block at any one time.")
                .defineInRange("max_storage", 1_000_000, 1, Integer.MAX_VALUE);
        energyPerTick = spec.comment("The amount of energy this block will create per tick during the daytime.")
                .defineInRange("energy_per_tick", 5, 1, Integer.MAX_VALUE);
        waterloggedMultipler = spec.comment("The multiplier applied to the energy produced per tick when the block is waterlogged.")
                .defineInRange("waterlogged_multipler", 0.5, 0.0, 1.0);
    }

    @Override
    public String getID() {
        return "solar_generator";
    }

    @Override
    public String getName() {
        return "Solar Generator";
    }
}
