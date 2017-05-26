package bluemonster122.mods.simplerandomstuff.core;

import bluemonster122.mods.simplerandomstuff.reference.Names.OreDict;
import bluemonster122.mods.simplerandomstuff.util.IFeatureRegistry;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;

import static bluemonster122.mods.simplerandomstuff.core.ItemMisc.Types.*;
import static bluemonster122.mods.simplerandomstuff.util.ModelHelpers.registerIEnumMeta;
import static bluemonster122.mods.simplerandomstuff.util.ModelHelpers.registerItemModel;

public class FRCore implements IFeatureRegistry {

    public static final FRCore INSTANCE = new FRCore();
    public static final ItemST misc = new ItemMisc();
    public static final ItemWrench wrench = new ItemWrench();

    @Override
    public void registerBlocks( ) {
        /* NO OPERATION */
    }

    @Override
    public void registerItems( ) {
        GameRegistry.register(wrench);
        GameRegistry.register(misc);
    }

    @Override
    public void registerRecipes( ) {
        //@formatter:off

        GameRegistry.addRecipe(new ShapedOreRecipe(
                new ItemStack(wrench, 1),
                " I ",
                " SI",
                "S  ",
                'S', OreDict.IRON_STICK,
                'I', "ingotIron"
        ).setMirrored(true));

        GameRegistry.addRecipe(new ShapedOreRecipe(
                new ItemStack(misc, 8, IRON_ROD.getMeta()),
                "I",
                "I",
                'I', "ingotIron"
        ));

        GameRegistry.addRecipe(new ShapedOreRecipe(
                new ItemStack(misc, 1, WOODEN_GEAR.getMeta()),
                "MMM",
                "MSM",
                "MMM",
                'M', "stickWood",
                'S', OreDict.IRON_STICK
        ));

        GameRegistry.addRecipe(new ShapedOreRecipe(
                new ItemStack(misc, 1, STONE_GEAR.getMeta()),
                " M ",
                "MSM",
                " M ",
                'M', "cobblestone",
                'S', OreDict.IRON_STICK
        ));

        GameRegistry.addRecipe(new ShapedOreRecipe(
                new ItemStack(misc, 1, MACHINE_BASE.getMeta()),
                "SWS",
                "WIW",
                "SWS",
                'W', OreDict.IRON_STICK,
                'S', new ItemStack(misc, 1, STONE_GEAR.getMeta()),
                'I', OreDict.IRON_BLOCK
        ));

        GameRegistry.addRecipe(new ShapedOreRecipe(
                new ItemStack(misc, 1, COMPLEX_MACHINE_BASE.getMeta()),
                "MMM",
                "MWM",
                "MMM",
                'M', new ItemStack(misc, 1, MACHINE_BASE.getMeta()),
                'W', "wrench"
        ));

        //@formatter:on
    }

    @Override
    public void registerTileEntities( ) {
        /* NO OPERATION */
    }

    @Override
    public void loadConfigs(Configuration configuration) {
        /* NO OPERATION */
    }

    @Override
    public void registerEvents( ) {
        /* NO OPERATION */
    }

    @Override
    public void registerOreDict( ) {
        OreDictionary.registerOre("stickIron", new ItemStack(misc, 1, IRON_ROD.getMeta()));
        OreDictionary.registerOre("wrench", new ItemStack(wrench));
        OreDictionary.registerOre("gearWood", new ItemStack(misc, 1, WOODEN_GEAR.getMeta()));
        OreDictionary.registerOre("gearWooden", new ItemStack(misc, 1, WOODEN_GEAR.getMeta()));
        OreDictionary.registerOre("gearStone", new ItemStack(misc, 1, STONE_GEAR.getMeta()));
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerRenders( ) {
        registerItemModel(wrench);
        registerIEnumMeta(misc, ItemMisc.Types.VARIANTS);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerClientEvents( ) {
        /* NO OPERATION */
    }

    @Override
    public boolean shouldLoad( ) {
        return true;
    }

    private FRCore( ) {
    }
}
