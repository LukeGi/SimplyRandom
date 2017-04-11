package bluemonster122.mods.simplethings.treefarm;

import bluemonster122.mods.simplethings.block.BlockST;
import bluemonster122.mods.simplethings.util.IFeatureRegistry;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.ShapedOreRecipe;

import static bluemonster122.mods.simplethings.util.ModelHelpers.registerBlockModelAsItem;

public class TreeFarmRegistry implements IFeatureRegistry {
    public static final TreeFarmRegistry INSTANCE = new TreeFarmRegistry();

    @Override
    public void registerBlocks() {
        GameRegistry.register(tree_farm);
    }

    @Override
    public void registerItems() {
        GameRegistry.register(tree_farm.createItemBlock());
    }

    @Override
    public void registerRecipes() {
        //@formatter:off

        GameRegistry.addRecipe(new ShapedOreRecipe(
                new ItemStack(tree_farm, 1),
                "SAS",
                "IOI",
                "SAS",
                'S', "sapling",
                'A', new ItemStack(Items.IRON_AXE),
                'I', "blockIron", /* TODO: change this to be a machine block */
                'O', "obsidian"
        ));

        //@formatter:on
    }

    @Override
    public void registerTileEntities() {
        GameRegistry.registerTileEntityWithAlternatives(TileTreeFarm.class, TreeFarmRegistry.tree_farm.getRegistryName().toString(), "tileTreeFarm");
    }

    @Override
    public void registerEvents() {

    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerRenders() {
        registerBlockModelAsItem(tree_farm);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerClientEvents() {

    }


    private TreeFarmRegistry() {
    }

    public static BlockST tree_farm = new BlockTreeFarm();
}
