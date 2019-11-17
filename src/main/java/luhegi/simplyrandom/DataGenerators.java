package luhegi.simplyrandom;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import net.minecraft.advancements.criterion.InventoryChangeTrigger;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.data.*;
import net.minecraft.data.loot.BlockLootTables;
import net.minecraft.item.Items;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootParameterSet;
import net.minecraft.world.storage.loot.LootParameterSets;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraft.world.storage.loot.ValidationResults;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ExistingFileHelper;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.LanguageProvider;
import net.minecraftforge.registries.IForgeRegistryEntry;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class DataGenerators {
    public static class Lang extends LanguageProvider{

        public Lang(DataGenerator gen, String modid) {
            super(gen, modid, "en_us");
        }

        @Override
        protected void addTranslations() {
            add("itemGroup.simplyrandom", "Simply Random");
            addBlock(SimplyRandom.instance.cobblestone_generator_block);
            addBlock(SimplyRandom.instance.tree_farm_block);
        }

        private void addBlock(Supplier<? extends Block> block) {
            addBlock(block, auto(block));
        }

        private String auto(Supplier<? extends IForgeRegistryEntry<?>> sup) {
            return Arrays.stream(sup.get().getRegistryName().getPath().toLowerCase(Locale.ROOT).split("_"))
                    .map(StringUtils::capitalize).collect(Collectors.joining(" "));
        }

        @Override
        public String getName() {
            return "Simply Random English Language";
        }
    }

    public static class ItemModels extends ItemModelProvider {
        public ItemModels(DataGenerator generator, String modid, ExistingFileHelper existingFileHelper) {
            super(generator, modid, existingFileHelper);
        }

        @Override
        protected void registerModels() {
            itemBlock(SimplyRandom.instance.cobblestone_generator_block);
            itemBlock(SimplyRandom.instance.tree_farm_block);
        }

        public ItemModelBuilder itemBlock(Supplier<? extends Block> block) {
            String name = name(block);
            return withExistingParent(name, modLoc("block/" + name));
        }

        private String name(Supplier<? extends IItemProvider> item) {
            return item.get().asItem().getRegistryName().getPath();
        }

        @Override
        public String getName() {
            return "Simple Random Models";
        }
    }

    public static class BlockStates extends BlockStateProvider {
        public BlockStates(DataGenerator gen, String modid, ExistingFileHelper exFileHelper) {
            super(gen, modid, exFileHelper);
        }

        @Override
        protected void registerStatesAndModels() {
            simpleBlock(SimplyRandom.instance.cobblestone_generator_block);
            simpleBlock(SimplyRandom.instance.tree_farm_block);
        }

        private void simpleBlock(Supplier<? extends Block> block) {
            simpleBlock(block.get());
        }

        @Nonnull
        @Override
        public String getName() {
            return "Simply Random Block States";
        }
    }

    public static class Loots extends LootTableProvider {
        public Loots(DataGenerator dataGeneratorIn) {
            super(dataGeneratorIn);
        }

        @Override
        protected void validate(Map<ResourceLocation, LootTable> map, ValidationResults validationresults) {
            // NOOP
        }

        @Override
        protected List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootParameterSet>> getTables() {
            return ImmutableList.of(
                    Pair.of(BlockLoots::new, LootParameterSets.BLOCK)
            );
        }

        @Override
        public String getName() {
            return "Simply Random Loot Tables";
        }

        public static class BlockLoots extends BlockLootTables {
            @Override
            protected void addTables() {
                dropSelf(SimplyRandom.instance.cobblestone_generator_block);
                dropSelf(SimplyRandom.instance.tree_farm_block);
            }

            public void dropSelf(Supplier<? extends Block> block) {
                registerDropSelfLootTable(block.get());
            }

            @Override
            protected Iterable<Block> getKnownBlocks() {
                return SimplyRandom.instance.BLOCKS.getEntries().stream().map(Supplier::get).collect(Collectors.toList());
            }
        }
    }

    public static class Recipes extends RecipeProvider {
        public Recipes(DataGenerator generatorIn) {
            super(generatorIn);
        }

        @Override
        protected void registerRecipes(Consumer<IFinishedRecipe> consumer) {
            shapedRecipe(SimplyRandom.instance.cobblestone_generator_block)
                    .key('P', Items.IRON_PICKAXE)
                    .key('L', Items.LAVA_BUCKET)
                    .key('W', Items.WATER_BUCKET)
                    .key('C', Blocks.COBBLESTONE)
                    .patternLine("PPP")
                    .patternLine("WCL")
                    .patternLine("PPP")
                    .setGroup("cobblestone_generator")
                    .addCriterion("has_cobblestone_generator", hasItem(SimplyRandom.instance.cobblestone_generator_block))
                    .build(consumer);
            shapedRecipe(SimplyRandom.instance.tree_farm_block)
                    .key('A', Items.IRON_AXE)
                    .key('O', Blocks.OBSIDIAN)
                    .key('S', ItemTags.SAPLINGS)
                    .key('Q', Blocks.QUARTZ_BLOCK)
                    .patternLine("ASA")
                    .patternLine("AQA")
                    .patternLine("OOO")
                    .setGroup("tree_farm")
                    .addCriterion("has_tree_farm", hasItem(SimplyRandom.instance.tree_farm_block))
                    .build(consumer);
        }

        private InventoryChangeTrigger.Instance hasItem(Supplier<? extends IItemProvider> item) {
            return hasItem(item.get());
        }

        private ShapedRecipeBuilder shapedRecipe(Supplier<? extends IItemProvider> item) {
            return shapedRecipe(item, 1);
        }

        private ShapedRecipeBuilder shapedRecipe(Supplier<? extends IItemProvider> item, int amount) {
            return ShapedRecipeBuilder.shapedRecipe(item.get(), amount);
        }

        @Override
        public String getName() {
            return "Simply Random Recipes";
        }
    }
}
