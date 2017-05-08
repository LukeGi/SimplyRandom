package bluemonster122.mods.simplethings.treefarm;

import bluemonster122.mods.simplethings.core.block.BlockST;
import bluemonster122.mods.simplethings.reference.ModInfo;
import bluemonster122.mods.simplethings.reference.Names;
import bluemonster122.mods.simplethings.util.IFeatureRegistry;
import net.minecraft.block.BlockPlanks;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;

import static bluemonster122.mods.simplethings.util.ModelHelpers.registerBlockModelAsItem;

public class FRTreeFarm implements IFeatureRegistry {
    public static final FRTreeFarm INSTANCE = new FRTreeFarm();
    public static boolean shouldLoad = false;

    @Override
    public void registerBlocks( ) {
        GameRegistry.register(tree_farm);
    }

    @Override
    public void registerItems( ) {
        GameRegistry.register(tree_farm.createItemBlock());
    }

    @Override
    public void registerRecipes( ) {
        //@formatter:off

        GameRegistry.addRecipe(new ShapedOreRecipe(
                new ItemStack(tree_farm, 1),
                "SAS",
                "IOI",
                "SAS",
                'S', Names.OreDict.SAPLING,
                'A', new ItemStack(Items.IRON_AXE),
                'I', Names.OreDict.IRON_BLOCK, /* TODO: change this to be a machine block */
                'O', Names.OreDict.OBSIDIAN
        ));

        //@formatter:on
    }

    @Override
    public void registerTileEntities( ) {
        GameRegistry.registerTileEntity(TileTreeFarm.class, "simplethings:tree_farm");
    }

    @Override
    public void loadConfigs(Configuration configuration) {
        shouldLoad = configuration.getBoolean("Tree Farm", ModInfo.CONFIG_FEATURES, true, "Set to false to disable the tree farm");
        setBreakEnergy(configuration.getInt("Energy Consumed On Block Break", "tree_farm", 50, 0, 1000, "Set to 0 to make the farm cost no power."));
    }

    @Override
    public void registerEvents( ) {
        /* NO OPERATION */
    }

    @Override
    public void registerOreDict( ) {
        for (BlockPlanks.EnumType type : BlockPlanks.EnumType.values()) {
            String name = type.getName();
            OreDictionary.registerOre("sapling", new ItemStack(Blocks.SAPLING, 1, type.getMetadata()));
            OreDictionary.registerOre("sapling" + name.substring(0, 1).toUpperCase() + name.substring(1), new ItemStack(Blocks.SAPLING, 1, type.getMetadata()));
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerRenders( ) {
        registerBlockModelAsItem(tree_farm);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerClientEvents( ) {
        /* NO OPERATION */
    }

    @Override
    public boolean shouldLoad( ) {
        return shouldLoad;
    }

    public int getBreakEnergy( ) {
        return tree_farm_break_energy;
    }

    public void setBreakEnergy(int energy) {
        this.tree_farm_break_energy = energy;
    }

    private FRTreeFarm( ) {
    }
    private int tree_farm_break_energy = 50;
    public static final BlockST tree_farm = new BlockTreeFarm();
}
