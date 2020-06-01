package luhegi.mods.simplyrandom.coalgen;

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
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.Tags;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.function.Consumer;

//TODO: make block orientable
@Setup
public class CoalGen extends SetupManager {
    public static final CoalGen INSTANCE = new CoalGen();

    public CoalGenBlock block;
    public CoalGenItem item;
    public BasisTileType<CoalGenTile> tileType;

    private ForgeConfigSpec.IntValue maxStorage;
    private ForgeConfigSpec.IntValue energyPerTick;

    public static int getMaxStorage() {
        return INSTANCE.maxStorage.get();
    }

    public static int getEnergyPerTick() {
        return INSTANCE.energyPerTick.get();
    }

    @Override
    public void registerBlocks(IForgeRegistry<Block> registry) {
        block = register(registry, new CoalGenBlock(Block.Properties.from(Blocks.FURNACE)));
    }

    @Override
    public void registerItems(IForgeRegistry<Item> registry) {
        item = register(registry, new CoalGenItem(block));
    }

    @Override
    public void registerTileType(IForgeRegistry<TileEntityType<?>> registry) {
        tileType = register(registry, new BasisTileType<>(CoalGenTile::new, block));
    }

    @Override
    protected void generateBlockData(BasisBlockProvider provider) {
        provider.horizontalBlock(block, provider.models().orientable(
                getID(),
                provider.tex(getID(), "side"),
                provider.tex(getID(), "front"),
                provider.tex(getID(), "top")
        ));
    }

    @Override
    protected void generateItemData(BasisItemProvider provider) {
        provider.getBuilder(getID()).parent(provider.getBlockModel(block));
    }

    @Override
    protected void generateEnUsLangData(BasisLangProvider provider) {
        provider.add(block, getName());
        provider.addTooltip(block, "This will make power when you fill it with some coal or other furnace-accepted fuels.");
    }

    @Override
    protected void generateRecipeData(BasisRecipeProvider provider, Consumer<IFinishedRecipe> consumer) {
        ShapedRecipeBuilder.shapedRecipe(block, 1)
                .patternLine("ITI")
                .patternLine("CFC")
                .patternLine("I&I")
                .key('I', Tags.Items.INGOTS_IRON)
                .key('T', Items.IRON_TRAPDOOR)
                .key('C', Items.COAL_BLOCK)
                .key('F', Items.FURNACE)
                .key('&', Items.FLINT_AND_STEEL)
                .setGroup(SimplyRandom.ID + ":" + getID())
                .addCriterion("has_flint_and_steel",  provider.hasItem(Items.FLINT_AND_STEEL))
                .addCriterion("has_furnace", provider.hasItem(Items.FURNACE))
                .build(consumer);
    }

    @Override
    protected void addClientConfigs(ForgeConfigSpec.Builder spec) {

    }

    @Override
    protected void addServerConfigs(ForgeConfigSpec.Builder spec) {
        maxStorage = spec.comment("The largest amount of energy that the coal generator can hold")
                .defineInRange("max_storage", 1_000_000, 0, Integer.MAX_VALUE);
        energyPerTick = spec.comment("The amount of energy produced per tick. Fuels last for the same amount of tips as they do in a furnace.")
                .defineInRange("energy_per_tick", 10, 0, Integer.MAX_VALUE);
    }

    @Override
    public String getID() {
        return "coal_generator";
    }

    @Override
    public String getName() {
        return "Coal Generator";
    }
}
