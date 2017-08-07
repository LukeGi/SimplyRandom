package bluemonster122.mods.simplerandomstuff.tanks;

import bluemonster122.mods.simplerandomstuff.client.renderer.BoxRendererManager;
import bluemonster122.mods.simplerandomstuff.client.renderer.TESRTank;
import bluemonster122.mods.simplerandomstuff.core.FRCore;
import bluemonster122.mods.simplerandomstuff.core.block.BlockSRS;
import bluemonster122.mods.simplerandomstuff.reference.Names;
import bluemonster122.mods.simplerandomstuff.reference.Names.OreDict;
import bluemonster122.mods.simplerandomstuff.util.IFeatureRegistry;
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

import static bluemonster122.mods.simplerandomstuff.util.ModelHelpers.registerIEnumMeta;

public class FRTank implements IFeatureRegistry {
    public static final IFeatureRegistry INSTANCE = new FRTank();

    @Override
    public void registerBlocks( ) {
        GameRegistry.register(tank);
    }

    @Override
    public void registerItems( ) {
        GameRegistry.register(tank.createItemBlock());
        GameRegistry.register(upgrade);
        glass_tank = new ItemStack(tank, 1, 0);
        iron_tank = new ItemStack(tank, 1, 1);
        gold_tank = new ItemStack(tank, 1, 2);
        obsidian_tank = new ItemStack(tank, 1, 3);
        diamond_tank = new ItemStack(tank, 1, 4);
    }

    @Override
    public void registerRecipes( ) {
        RecipeSorter.register("tank_upgrade_recipe", UpgradeRecipe.class, RecipeSorter.Category.SHAPED, "");
        //@formatter:off

        GameRegistry.addRecipe(new UpgradeRecipe(
                glass_tank,
                "GPG",
                "P P",
                "GPG",
                'G', OreDict.GLASS_BLOCK,
                'P', OreDict.GLASS_PANES
        ));

        GameRegistry.addRecipe(new UpgradeRecipe(
                iron_tank,
                "UPU",
                "PTP",
                "UPU",
                'P', OreDict.GLASS_PANES,
                'U', OreDict.IRON_INGOT,
                'T', glass_tank
        ));

        GameRegistry.addRecipe(new UpgradeRecipe(
                gold_tank,
                "UPU",
                "PTP",
                "UPU",
                'P', OreDict.GLASS_PANES,
                'U', OreDict.GOLD_INGOT,
                'T', iron_tank
        ));

        GameRegistry.addRecipe(new UpgradeRecipe(
                obsidian_tank,
                "UPU",
                "PTP",
                "UPU",
                'P', OreDict.GLASS_PANES,
                'U', OreDict.OBSIDIAN,
                'T', gold_tank
        ));

        GameRegistry.addRecipe(new UpgradeRecipe(
                diamond_tank,
                "UPU",
                "PTP",
                "UPU",
                'P', OreDict.GLASS_PANES,
                'U', OreDict.DIAMOND,
                'T', obsidian_tank
        ));

        GameRegistry.addRecipe(new ShapedOreRecipe(
                new ItemStack(upgrade, 1, ItemTankUpgrade.Types.GLASS_TO_IRON.getMeta()),
                "UPU",
                "PWP",
                "UPU",
                'P', OreDict.GLASS_PANES,
                'U', OreDict.IRON_INGOT,
                'W', new ItemStack(FRCore.wrench)
        ));

        GameRegistry.addRecipe(new ShapedOreRecipe(
                new ItemStack(upgrade, 1, ItemTankUpgrade.Types.IRON_TO_GOLD.getMeta()),
                "UPU",
                "PWP",
                "UPU",
                'P', OreDict.GLASS_PANES,
                'U', OreDict.GOLD_INGOT,
                'W', new ItemStack(FRCore.wrench)
        ));

        GameRegistry.addRecipe(new ShapedOreRecipe(
                new ItemStack(upgrade, 1, ItemTankUpgrade.Types.GOLD_TO_OBSIDIAN.getMeta()),
                "UPU",
                "PWP",
                "UPU",
                'P', OreDict.GLASS_PANES,
                'U', OreDict.OBSIDIAN,
                'W', new ItemStack(FRCore.wrench)
        ));

        GameRegistry.addRecipe(new ShapedOreRecipe(
                new ItemStack(upgrade, 1, ItemTankUpgrade.Types.OBSIDIAN_TO_DIAMOND.getMeta()),
                "UPU",
                "PWP",
                "UPU",
                'P', OreDict.GLASS_PANES,
                'U', OreDict.DIAMOND,
                'W', new ItemStack(FRCore.wrench)
        ));

        //@formatter:on
    }

    @Override
    public void registerTileEntities( ) {
        GameRegistry.registerTileEntity(TileTank.class, "simplerandomstuff:tank");
    }

    @Override
    public void loadConfigs(Configuration configuration) {
    }

    @Override
    public void registerEvents( ) {
        /* NO OPERATION */
    }

    @Override
    public void registerOreDict( ) {
        /* NO OPERATION */
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerRenders( ) {
        registerIEnumMeta(tank, BlockTank.Types.VARIANTS);
        registerIEnumMeta(upgrade, ItemTankUpgrade.Types.VARIANTS);
        ClientRegistry.bindTileEntitySpecialRenderer(TileTank.class, new TESRTank());
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerClientEvents( ) {
        MinecraftForge.EVENT_BUS.register(BoxRendererManager.INSTANCE);
    }

    @Override
    public String getName( ) {
        return Names.Features.TANK;
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

    private FRTank( ) {
    }

    public static final BlockSRS tank = new BlockTank();
    public static final Item upgrade = new ItemTankUpgrade();
    private ItemStack glass_tank;
    private ItemStack iron_tank;
    private ItemStack gold_tank;
    private ItemStack obsidian_tank;
    private ItemStack diamond_tank;
}
