package bluemonster122.mods.simplerandomstuff.core;

import bluemonster122.mods.simplerandomstuff.core.block.BlockSRS;
import bluemonster122.mods.simplerandomstuff.reference.ModInfo;
import bluemonster122.mods.simplerandomstuff.reference.Names;
import bluemonster122.mods.simplerandomstuff.reference.Names.OreDict;
import bluemonster122.mods.simplerandomstuff.util.IFeatureRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.registries.IForgeRegistry;

import javax.annotation.Nullable;

import static bluemonster122.mods.simplerandomstuff.core.ItemMisc.Types.*;
import static bluemonster122.mods.simplerandomstuff.util.ModelHelpers.registerIEnumMeta;
import static bluemonster122.mods.simplerandomstuff.util.ModelHelpers.registerItemModel;

public class FRCore
        implements IFeatureRegistry {

    public static final FRCore INSTANCE = new FRCore();

    public static final ItemST misc = new ItemMisc();

    public static final ItemWrench wrench = new ItemWrench();

    public static final BlockSRS test = new BlockSRS("test", Material.CLOTH) {
        @Override
        public boolean hasTileEntity(IBlockState state) {
            return true;
        }

        @Nullable
        @Override
        public TileEntity createTileEntity(
                World world, IBlockState state
        ) {
            return new TileEntityTickingSRS();
        }
    };

    private FRCore() {
    }

    @Override
    public void registerBlocks(IForgeRegistry<Block> registry) {
        registry.registerAll(test);
        /* NO OPERATION */
    }

    @Override
    public void registerItems(IForgeRegistry<Item> registry) {
        registry.registerAll(wrench, misc, test.createItemBlock());
    }

    @Override
    public void registerRecipes(IForgeRegistry<IRecipe> registry) {
        /* NO OPERATION */
        // Recipes are all in JSONs now.
    }

    @Override
    public void registerTileEntities() {
        GameRegistry.registerTileEntity(TileEntityTickingSRS.class, "test");
        /* NO OPERATION */
    }

    @Override
    public void loadConfigs(Configuration configuration) {
        /* NO OPERATION */
    }

    @Override
    public void registerEvents() {
        /* NO OPERATION */
    }

    @Override
    public void registerOreDict() {
        OreDictionary.registerOre("stickIron", new ItemStack(misc, 1, IRON_ROD.getMeta()));
        OreDictionary.registerOre("wrench", new ItemStack(wrench, 1, OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre("gearWood", new ItemStack(misc, 1, WOODEN_GEAR.getMeta()));
        OreDictionary.registerOre("gearWooden", new ItemStack(misc, 1, WOODEN_GEAR.getMeta()));
        OreDictionary.registerOre("gearStone", new ItemStack(misc, 1, STONE_GEAR.getMeta()));
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerRenders() {
        registerItemModel(wrench);
        registerIEnumMeta(misc, ItemMisc.Types.VARIANTS);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerClientEvents() {
        /* NO OPERATION */
    }

    @Override
    public String getName() {
        return Names.Features.CORE;
    }
}
