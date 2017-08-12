package bluemonster.simplerandomstuff.tanks;

import bluemonster.simplerandomstuff.reference.ModInfo;
import com.google.gson.JsonObject;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.IRecipeFactory;
import net.minecraftforge.common.crafting.JsonContext;
import net.minecraftforge.oredict.ShapedOreRecipe;

import javax.annotation.Nonnull;

public class UpgradeRecipeFactory implements IRecipeFactory {
    @Override
    public IRecipe parse(JsonContext context, JsonObject json) {
        ShapedOreRecipe recipe = ShapedOreRecipe.factory(context, json);

        CraftingHelper.ShapedPrimer primer = new CraftingHelper.ShapedPrimer();
        primer.width = recipe.getWidth();
        primer.height = recipe.getHeight();
        primer.mirrored = JsonUtils.getBoolean(json, "mirrored", true);
        primer.input = recipe.getIngredients();

        return new UpgradeRecipe(new ResourceLocation(ModInfo.MOD_ID, "tank_upgrade"), recipe.getRecipeOutput(), primer);
    }

    public static class UpgradeRecipe extends ShapedOreRecipe {
        @Nonnull
        @Override
        public ItemStack getCraftingResult(@Nonnull InventoryCrafting var1) {
            ItemStack output = super.getCraftingResult(var1).copy();
            ItemStack oldtank = var1.getStackInRowAndColumn(1, 1);
            if (oldtank.hasTagCompound())
                output.setTagCompound(oldtank.getTagCompound());
            return output;
        }

        public UpgradeRecipe(ResourceLocation group, @Nonnull ItemStack result, CraftingHelper.ShapedPrimer primer) {
            super(group, result, primer);
        }
    }
}
