package luhegi.mods.simplyrandom.cobblegen;

import luhegi.mods.simplyrandom.SimplyRandom;
import luhegi.mods.simplyrandom.basis.data.BasisBlockProvider;
import luhegi.mods.simplyrandom.basis.data.BasisItemProvider;
import luhegi.mods.simplyrandom.basis.data.BasisLangProvider;
import luhegi.mods.simplyrandom.basis.data.BasisRecipeProvider;
import luhegi.mods.simplyrandom.basis.objects.BasisTileType;
import luhegi.mods.simplyrandom.basis.setup.SetupManager;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.ShapedRecipeBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.Items;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.function.Consumer;

public class CobbleGen extends SetupManager {
    public static final CobbleGen INSTANCE = new CobbleGen();
    public CobbleGenBlock BLOCK = null;
    public CobbleGenItem ITEM = null;
    public BasisTileType<CobbleGenTile> TILE_TYPE = null;
    private ForgeConfigSpec.BooleanValue useEnergy;
    private ForgeConfigSpec.IntValue energyPerCobble;
    private ForgeConfigSpec.IntValue maxEnergyStored;

    public static boolean getUseEnergy() {
        return INSTANCE.useEnergy.get();
    }

    public static int getEnergyPerCobble() {
        return INSTANCE.energyPerCobble.get();
    }

    public static int getMaxEnergyStorage() {
        return INSTANCE.maxEnergyStored.get();
    }

    @Override
    public void registerBlocks(IForgeRegistry<Block> registry) {
        BLOCK = register(registry, new CobbleGenBlock(Block.Properties.from(Blocks.COBBLESTONE)));
    }

    @Override
    public void registerItems(IForgeRegistry<Item> registry) {
        // TODO: Create ItemGroup
        ITEM = register(registry, new CobbleGenItem(BLOCK));
    }

    @Override
    public void registerTileType(IForgeRegistry<TileEntityType<?>> registry) {
        TILE_TYPE = register(registry, new BasisTileType<>(CobbleGenTile::new, BLOCK));
    }

    @Override
    protected void generateBlockData(BasisBlockProvider provider) {
        provider.simpleBlock(BLOCK);
    }

    @Override
    protected void generateItemData(BasisItemProvider provider) {
        provider.getBuilder(getID()).parent(provider.getBlockModel(BLOCK));
    }

    @Override
    protected void generateEnUsLangData(BasisLangProvider provider) {
        provider.add(BLOCK, getName());
        provider.addTooltip(BLOCK, "Creates cobblestone as fast as you can extract it, so long as it's conditions are met.");
    }

    @Override
    protected void generateRecipeData(BasisRecipeProvider provider, Consumer<IFinishedRecipe> consumer) {
        ShapedRecipeBuilder.shapedRecipe(BLOCK, 1)
                .patternLine("PPP")
                .patternLine("LCW")
                .patternLine("PPP")
                .key('P', Items.IRON_PICKAXE)
                .key('L', Items.LAVA_BUCKET)
                .key('W', Items.WATER_BUCKET)
                .key('C', Items.COBBLESTONE)
                .setGroup(SimplyRandom.ID + ":" + getID())
                .addCriterion("has_cobblestone", provider.hasItem(Items.COBBLESTONE))
                .addCriterion("has_iron_pickaxe", provider.hasItem(Items.IRON_PICKAXE))
                .build(consumer);
    }

    @Override
    protected void addClientConfigs(ForgeConfigSpec.Builder spec) {
        // NOOP
    }

    @Override
    protected void addServerConfigs(ForgeConfigSpec.Builder spec) {
        useEnergy = spec.comment("Whether the cobblegen should use energy. the following values will be ignored if this is false.")
                .worldRestart()
                .define("use_energy", true);
        energyPerCobble = spec.comment("The amount of energy it takes to create a single piece of cobble.")
                .defineInRange("energy_per_cobble", 0, 0, Integer.MAX_VALUE);
        maxEnergyStored = spec.comment("The maximum size of the machine's energy buffer.")
                .defineInRange("max_energy", 0, 0, Integer.MAX_VALUE);
    }

    @Override
    public String getID() {
        return "cobblestone_generator";
    }

    @Override
    public String getName() {
        return "Cobblestone Generator";
    }
}
