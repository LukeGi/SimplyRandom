package luhegi.mods.simplyrandom.basis.data;

import luhegi.mods.simplyrandom.basis.setup.ModSetupManager;
import net.minecraft.advancements.criterion.EnterBlockTrigger;
import net.minecraft.advancements.criterion.InventoryChangeTrigger;
import net.minecraft.advancements.criterion.ItemPredicate;
import net.minecraft.block.Block;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.RecipeProvider;
import net.minecraft.item.Item;
import net.minecraft.tags.Tag;
import net.minecraft.util.IItemProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class BasisRecipeProvider extends RecipeProvider {
    public static final BasisRecipeProvider INSTANCE = new BasisRecipeProvider();

    private BasisRecipeProvider() {
        super(ModSetupManager.INSTANCE.generator);
    }

    private List<BiConsumer<BasisRecipeProvider, Consumer<IFinishedRecipe>>> callbacks = new ArrayList<>();

    public void addCallback(BiConsumer<BasisRecipeProvider, Consumer<IFinishedRecipe>> callback) {
        callbacks.add(callback);
    }

    @Override
    protected void registerRecipes(final Consumer<IFinishedRecipe> consumer) {
        callbacks.forEach(callback -> callback.accept(this, consumer));
        callbacks.clear();
    }

    @Override
    public EnterBlockTrigger.Instance enteredBlock(Block blockIn) {
        return super.enteredBlock(blockIn);
    }

    @Override
    public InventoryChangeTrigger.Instance hasItem(IItemProvider itemIn) {
        return super.hasItem(itemIn);
    }

    @Override
    public InventoryChangeTrigger.Instance hasItem(Tag<Item> tagIn) {
        return super.hasItem(tagIn);
    }

    @Override
    public InventoryChangeTrigger.Instance hasItem(ItemPredicate... predicates) {
        return super.hasItem(predicates);
    }

    @Override
    public String getName() {
        return "Simply Random Recipe Provider";
    }
}
