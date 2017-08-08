package bluemonster122.mods.simplerandomstuff.tanks;

import bluemonster122.mods.simplerandomstuff.client.renderer.BoxRendererManager;
import bluemonster122.mods.simplerandomstuff.client.renderer.TESRTank;
import bluemonster122.mods.simplerandomstuff.core.block.BlockSRS;
import bluemonster122.mods.simplerandomstuff.reference.Names;
import bluemonster122.mods.simplerandomstuff.util.IFeatureRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;

import static bluemonster122.mods.simplerandomstuff.util.ModelHelpers.registerIEnumMeta;

public class FRTank
        implements IFeatureRegistry {
    public static final IFeatureRegistry INSTANCE = new FRTank();

    public static final BlockSRS tank = new BlockTank();

    public static final Item upgrade = new ItemTankUpgrade();

    private ItemStack glass_tank;

    private ItemStack iron_tank;

    private ItemStack gold_tank;

    private ItemStack obsidian_tank;

    private ItemStack diamond_tank;

    private FRTank() {
    }

    @Override
    public void registerBlocks(IForgeRegistry<Block> registry) {
        registry.registerAll(tank);
    }

    @Override
    public void registerItems(IForgeRegistry<Item> registry) {
        registry.registerAll(tank.createItemBlock(), upgrade);
        glass_tank = new ItemStack(tank, 1, 0);
        iron_tank = new ItemStack(tank, 1, 1);
        gold_tank = new ItemStack(tank, 1, 2);
        obsidian_tank = new ItemStack(tank, 1, 3);
        diamond_tank = new ItemStack(tank, 1, 4);
    }

    @Override
    public void registerTileEntities() {
        GameRegistry.registerTileEntity(TileTank.class, "simplerandomstuff:tank");
    }

    @Override
    public void loadConfigs(Configuration configuration) {
    }

    @Override
    public void registerEvents() {
        /* NO OPERATION */
    }

    @Override
    public void registerOreDict() {
        /* NO OPERATION */
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

    @Override
    public String getName() {
        return Names.Features.TANK;
    }

    //  private class UpgradeRecipe
    //    extends ShapedOreRecipe
    //  {
    //
    //    public UpgradeRecipe(@Nonnull ItemStack result, Object... recipes)
    //    {
    //      super(result, recipes);
    //    }
    //
    //    @Override
    //    public ItemStack getCraftingResult(InventoryCrafting inv)
    //    {
    //      ItemStack output = super.getCraftingResult(inv)
    //                              .copy();
    //      output.setTagCompound(inv.getStackInRowAndColumn(1, 1)
    //                               .getTagCompound());
    //      return output;
    //    }
    //  }
}
