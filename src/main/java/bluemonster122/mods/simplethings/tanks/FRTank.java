package bluemonster122.mods.simplethings.tanks;

import bluemonster122.mods.simplethings.core.block.BlockST;
import bluemonster122.mods.simplethings.client.renderer.BoxRendererManager;
import bluemonster122.mods.simplethings.client.renderer.TESRTank;
import bluemonster122.mods.simplethings.util.IFeatureRegistry;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.RecipeSorter;
import net.minecraftforge.oredict.ShapedOreRecipe;

import javax.annotation.Nonnull;

import static bluemonster122.mods.simplethings.util.ModelHelpers.registerIEnumMeta;

public class FRTank implements IFeatureRegistry {
    public static final IFeatureRegistry INSTANCE = new FRTank();

    @Override
    public void registerBlocks() {
        GameRegistry.register(tank);
    }

    @Override
    public void registerItems() {
        GameRegistry.register(tank.createItemBlock());
        GameRegistry.register(upgrade);
        glass_tank = new ItemStack(tank, 1, 0);
        iron_tank = new ItemStack(tank, 1, 1);
        gold_tank = new ItemStack(tank, 1, 2);
        obsidian_tank = new ItemStack(tank, 1, 3);
        diamond_tank = new ItemStack(tank, 1, 4);
    }

    @Override
    public void registerRecipes() {
        RecipeSorter.register("tank_upgrade_recipe", UpgradeRecipe.class, RecipeSorter.Category.SHAPED, "");
        //@formatter:off

        GameRegistry.addRecipe(new UpgradeRecipe(
                glass_tank,
                "GPG",
                "P P",
                "GPG",
                'G', "blockGlass",
                'P', "paneGlass"
        ));

        GameRegistry.addRecipe(new UpgradeRecipe(
                iron_tank,
                "GUG",
                "UTU",
                "GUG",
                'G', "blockGlass",
                'U', "ingotIron",
                'T', glass_tank
        ));

        GameRegistry.addRecipe(new UpgradeRecipe(
                gold_tank,
                "GUG",
                "UTU",
                "GUG",
                'G', "blockGlass",
                'U', "ingotGold",
                'T', iron_tank
        ));

        GameRegistry.addRecipe(new UpgradeRecipe(
                obsidian_tank,
                "GUG",
                "UTU",
                "GUG",
                'G', "blockGlass",
                'U', "obsidian",
                'T', gold_tank
        ));

        GameRegistry.addRecipe(new UpgradeRecipe(
                diamond_tank,
                "GUG",
                "UTU",
                "GUG",
                'G', "blockGlass",
                'U', "gemDiamond",
                'T', obsidian_tank
        ));

        //@formatter:on
    }

    @Override
    public void registerTileEntities() {
        GameRegistry.registerTileEntity(TileTank.class, "simplethings:tank");
    }

    @Override
    public void loadConfigs(Configuration configuration) {

    }

    @Override
    public void registerEvents() {

    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerRenders() {
        registerIEnumMeta(tank, BlockTank.Types.VARIANTS);
        registerIEnumMeta(upgrade, ItemTankUpgrade.Types.VARIANTS);
        ClientRegistry.bindTileEntitySpecialRenderer(TileTank.class, new TESRTank());
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerClientEvents() {
        MinecraftForge.EVENT_BUS.register(BoxRendererManager.INSTANCE);
    }

    private class UpgradeRecipe extends ShapedOreRecipe {

        public UpgradeRecipe(@Nonnull ItemStack result, Object... recipe) {
            super(result, recipe);
        }

        @Override
        public ItemStack getCraftingResult(InventoryCrafting inv) {
            ItemStack output = super.getCraftingResult(inv).copy();
            output.setTagCompound(inv.getStackInRowAndColumn(1, 1).getTagCompound());
            return output;
        }
    }

    private FRTank() {
    }
    public static BlockST tank = new BlockTank();
    public static Item upgrade = new ItemTankUpgrade();
    public static ItemStack glass_tank;
    public static ItemStack iron_tank;
    public static ItemStack gold_tank;
    public static ItemStack obsidian_tank;
    public static ItemStack diamond_tank;
}
