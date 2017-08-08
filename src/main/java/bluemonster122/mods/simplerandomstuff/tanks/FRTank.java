package bluemonster122.mods.simplerandomstuff.tanks;

import bluemonster122.mods.simplerandomstuff.client.renderer.BoxRendererManager;
import bluemonster122.mods.simplerandomstuff.client.renderer.TESRTank;
import bluemonster122.mods.simplerandomstuff.core.FRCore;
import bluemonster122.mods.simplerandomstuff.core.block.BlockSRS;
import bluemonster122.mods.simplerandomstuff.reference.ModInfo;
import bluemonster122.mods.simplerandomstuff.reference.Names;
import bluemonster122.mods.simplerandomstuff.reference.Names.OreDict;
import bluemonster122.mods.simplerandomstuff.util.IFeatureRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.ShapedOreRecipe;
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
    public void registerRecipes(IForgeRegistry<IRecipe> registry) {
        //@formatter:off

        ResourceLocation blah = new ResourceLocation(ModInfo.MOD_ID, "blah");
        registry.registerAll(
                new ShapedOreRecipe(blah,
                        new ItemStack(upgrade, 1, ItemTankUpgrade.Types.GLASS_TO_IRON.getMeta()),
                        "UPU",
                        "PWP",
                        "UPU",
                        'P',
                        OreDict.GLASS_PANES,
                        'U',
                        OreDict.IRON_INGOT,
                        'W',
                        new ItemStack(FRCore.wrench)
                ).setRegistryName("glass_to_iron_upgrade"), new ShapedOreRecipe(blah,
                        new ItemStack(upgrade,
                                1,
                                ItemTankUpgrade.Types.IRON_TO_GOLD.getMeta()
                        ),
                        "UPU",
                        "PWP",
                        "UPU",
                        'P',
                        OreDict.GLASS_PANES,
                        'U',
                        OreDict.GOLD_INGOT,
                        'W',
                        new ItemStack(FRCore.wrench)
                ).setRegistryName("iron_to_gold_upgrade"), new ShapedOreRecipe(blah,
                        new ItemStack(upgrade,
                                1,
                                ItemTankUpgrade.Types.GOLD_TO_OBSIDIAN.getMeta()
                        ),
                        "UPU",
                        "PWP",
                        "UPU",
                        'P',
                        OreDict.GLASS_PANES,
                        'U',
                        OreDict.OBSIDIAN,
                        'W',
                        new ItemStack(FRCore.wrench)
                ).setRegistryName("gold_to_obsidian_upgrade"), new ShapedOreRecipe(blah,
                        new ItemStack(upgrade,
                                1,
                                ItemTankUpgrade.Types.OBSIDIAN_TO_DIAMOND.getMeta()
                        ),
                        "UPU",
                        "PWP",
                        "UPU",
                        'P',
                        OreDict.GLASS_PANES,
                        'U',
                        OreDict.DIAMOND,
                        'W',
                        new ItemStack(FRCore.wrench)
                ).setRegistryName("obsidian_to_diamond_upgrade")
        );

        //@formatter:on
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
