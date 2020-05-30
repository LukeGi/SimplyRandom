package luhegi.mods.simplyrandom.treefarm;

import luhegi.mods.simplyrandom.SimplyRandom;
import luhegi.mods.simplyrandom.basis.data.BasisBlockProvider;
import luhegi.mods.simplyrandom.basis.data.BasisItemProvider;
import luhegi.mods.simplyrandom.basis.data.BasisLangProvider;
import luhegi.mods.simplyrandom.basis.data.BasisRecipeProvider;
import luhegi.mods.simplyrandom.basis.objects.BasisTileType;
import luhegi.mods.simplyrandom.basis.setup.SetupManager;
import luhegi.mods.simplyrandom.cobblegen.CobbleGen;
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

public class TreeFarm extends SetupManager {
    public static final TreeFarm INSTANCE = new TreeFarm();

    public TreeFarmBlock BLOCK;
    public TreeFarmItem ITEM;
    public BasisTileType<TreeFarmTile> TILE_TYPE;

    private ForgeConfigSpec.BooleanValue useEnergy;
    private ForgeConfigSpec.IntValue maxStorage;
    private ForgeConfigSpec.IntValue energyPerWork;

    public static int getEnergyPerWork() {
        return getUseEnergy() ? INSTANCE.energyPerWork.get() : 0;
    }

    public static int getMaxStorage() {
        return getUseEnergy() ? INSTANCE.maxStorage.get() : 0;
    }

    public static boolean getUseEnergy() {
        return INSTANCE.useEnergy.get();
    }

    @Override
    public void registerBlocks(IForgeRegistry<Block> registry) {
        BLOCK = register(registry, new TreeFarmBlock(Block.Properties.from(Blocks.FURNACE)));
    }

    @Override
    public void registerItems(IForgeRegistry<Item> registry) {
        ITEM = register(registry, new TreeFarmItem(BLOCK));
    }

    @Override
    public void registerTileType(IForgeRegistry<TileEntityType<?>> registry) {
        TILE_TYPE = register(registry, new BasisTileType<>(TreeFarmTile::new, BLOCK));
    }

    @Override
    protected void generateBlockData(BasisBlockProvider provider) {
        provider.simpleBlock(BLOCK, provider.models()
                .cubeBottomTop(
                        getID(),
                        provider.modLoc("block/" + getID() + "_side"),
                        provider.modLoc("block/" + getID() + "_base"),
                        provider.modLoc("block/" + getID() + "_top")
                )
        );
    }

    @Override
    protected void generateItemData(BasisItemProvider provider) {
        provider.getBuilder(getID()).parent(provider.getBlockModel(BLOCK));
    }

    @Override
    protected void generateEnUsLangData(BasisLangProvider provider) {
        provider.add(BLOCK, getName());
        provider.addTooltip(BLOCK, "Uses a pocket dimension to collect and harvest trees at the cost of power.");
    }

    @Override
    protected void generateRecipeData(BasisRecipeProvider provider, Consumer<IFinishedRecipe> consumer) {
        ShapedRecipeBuilder.shapedRecipe(BLOCK, 1)
                .patternLine("IDI")
                .patternLine("AOA")
                .patternLine("III")
                .key('I', Tags.Items.INGOTS_IRON)
                .key('D', Items.DIRT)
                .key('O', Items.OBSERVER)
                .key('A', Items.IRON_AXE)
                .setGroup(SimplyRandom.ID + ":" + getID())
                .addCriterion("has_observer", provider.hasItem(Items.OBSERVER))
                .addCriterion("has_iron_axe", provider.hasItem(Items.IRON_AXE))
                .build(consumer);
    }

    @Override
    protected void addClientConfigs(ForgeConfigSpec.Builder spec) {

    }

    @Override
    protected void addServerConfigs(ForgeConfigSpec.Builder spec) {
        useEnergy = spec.comment("Whether the tree farm uses energy. Set to false and the rest of these values will be ignored")
                .worldRestart()
                .define("use_energy", false);
        maxStorage = spec.comment("The maximum storage that the tree farm can hold at once.")
                .defineInRange("max_storage", 1_000_000, 0, Integer.MAX_VALUE);
        energyPerWork = spec.comment("The amount of energy the tree farm consumer per unit of work done (relates to block hardness).")
                .defineInRange("energy_per_work", 100, 0, Integer.MAX_VALUE);
    }

    @Override
    public String getID() {
        return "tree_farm";
    }

    @Override
    public String getName() {
        return "Tree Farm";
    }
}
