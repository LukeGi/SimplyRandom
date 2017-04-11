package bluemonster122.mods.simplethings.treefarm;

import bluemonster122.mods.simplethings.core.block.BlockST;
import bluemonster122.mods.simplethings.util.IFeatureRegistry;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.ShapedOreRecipe;

import static bluemonster122.mods.simplethings.util.ModelHelpers.registerBlockModelAsItem;

public class FRTreeFarm implements IFeatureRegistry {
    public static final FRTreeFarm INSTANCE = new FRTreeFarm();
    public static int tree_farm_break_energy = 50;

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
        GameRegistry.registerTileEntity(TileTreeFarm.class, "simplethings:tree_farm");
    }

    @Override
    public void loadConfigs(Configuration configuration) {
        tree_farm_break_energy = configuration.getInt("Energy Consumed On Block Break", "tree_farm", 50, 0, 1000, "Set to 0 to make the farm cost no power.");
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


    private FRTreeFarm() {
    }

    public static BlockST tree_farm = new BlockTreeFarm();
}
