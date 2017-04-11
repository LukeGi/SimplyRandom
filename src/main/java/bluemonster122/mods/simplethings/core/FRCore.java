package bluemonster122.mods.simplethings.core;

import bluemonster122.mods.simplethings.util.IFeatureRegistry;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.ShapedOreRecipe;

import static bluemonster122.mods.simplethings.util.ModelHelpers.registerIEnumMeta;

public class FRCore implements IFeatureRegistry {

    public static final FRCore INSTANCE = new FRCore();

    @Override
    public void registerBlocks() {

    }

    @Override
    public void registerItems() {
        GameRegistry.register(misc);
    }

    @Override
    public void registerRecipes() {
        //@formatter:off

        GameRegistry.addRecipe(new ShapedOreRecipe(
                new ItemStack(misc, 1, ItemMisc.Types.WRENCH.getMeta()),
                "I I",
                "ISI",
                " S ",
                'S', "stick",
                'I', "ingotIron"
        ));

        //@formatter:on
    }

    @Override
    public void registerTileEntities() {

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
        registerIEnumMeta(misc, ItemMisc.Types.VARIANTS);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerClientEvents() {

    }

    private FRCore() {
    }
    public static ItemMisc misc = new ItemMisc();
}
