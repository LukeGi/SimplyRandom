package bluemonster122.mods.simplethings.generators;

import bluemonster122.mods.simplethings.core.block.BlockST;
import bluemonster122.mods.simplethings.util.IFeatureRegistry;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import static bluemonster122.mods.simplethings.util.ModelHelpers.registerIEnumMeta;

public class FRGenerators implements IFeatureRegistry {
    public static final FRGenerators INSTANCE = new FRGenerators();

    @Override
    public void registerBlocks() {
        GameRegistry.register(generators);
    }

    @Override
    public void registerItems() {
        GameRegistry.register(generators.createItemBlock());
    }

    @Override
    public void registerRecipes() {

    }

    @Override
    public void registerTileEntities() {
        GameRegistry.registerTileEntity(TileGeneratorSugar.class, "simplethings:sugar_generator");
        GameRegistry.registerTileEntity(TileGeneratorFire.class, "simplethings:fire_generator");
    }

    @Override
    public void loadConfigs(Configuration configuration) {
        Sugar_RF = configuration.getInt("Sugar Generator's rate (RF/t)", "generators", 10, 1, Integer.MAX_VALUE, "Set to any number larger than 0.");
        Sugar_Burntime = configuration.getInt("Sugar Generator's burntime per sugar", "generators", 10, 1, Integer.MAX_VALUE, "Set to any number larger than 0.");
        Fire_RF = configuration.getInt("Fire Generator's rate (RF/t)", "generators", 1, 1, Integer.MAX_VALUE, "Set to any number larger than 0.");
    }

    @Override
    public void registerEvents() {

    }

    @Override
    public void registerOreDict() {

    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerRenders() {
        registerIEnumMeta(generators, BlockGenerator.Types.VARIANTS);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerClientEvents() {

    }

    private FRGenerators() {
    }
    public static int Fire_RF = 1;
    public static int Sugar_Burntime = 10;
    public static int Sugar_RF = 10;
    public static BlockST generators = new BlockGenerator();
}
