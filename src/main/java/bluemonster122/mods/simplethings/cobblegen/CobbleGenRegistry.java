package bluemonster122.mods.simplethings.cobblegen;

import bluemonster122.mods.simplethings.block.BlockST;
import bluemonster122.mods.simplethings.util.IFeatureRegistry;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.ShapedOreRecipe;

import static bluemonster122.mods.simplethings.util.ModelHelpers.registerBlockModelAsItem;

public class CobbleGenRegistry implements IFeatureRegistry {
    public static final CobbleGenRegistry INSTANCE = new CobbleGenRegistry();

    @Override
    public void registerBlocks() {
        GameRegistry.register(cobblestone_generator);
    }

    @Override
    public void registerItems() {
        GameRegistry.register(cobblestone_generator.createItemBlock());
    }

    @Override
    public void registerRecipes() {
        //@formatter:off

        GameRegistry.addRecipe(new ShapedOreRecipe(
                new ItemStack(cobblestone_generator),
                "PPP",
                "WCL",
                "PPP",
                'W', new ItemStack(Items.WATER_BUCKET, 1),
                'C', "cobblestone",
                'L', new ItemStack(Items.LAVA_BUCKET, 1),
                'P', new ItemStack(Items.IRON_PICKAXE, 1)
        ));

        //@formatter:on
    }

    @Override
    public void registerTileEntities() {
        GameRegistry.registerTileEntityWithAlternatives(TileCobblestoneGenerator.class, cobblestone_generator.getRegistryName().toString(), "tileCobblestoneGenerator");
    }

    @Override
    public void registerEvents() {

    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerRenders() {
        registerBlockModelAsItem(cobblestone_generator);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerClientEvents() {

    }

    private CobbleGenRegistry() {
    }

    public static BlockST cobblestone_generator = new BlockCobblestoneGenerator();
}
