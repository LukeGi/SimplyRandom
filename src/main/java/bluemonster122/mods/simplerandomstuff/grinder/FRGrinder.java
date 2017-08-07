package bluemonster122.mods.simplerandomstuff.grinder;

import bluemonster122.mods.simplerandomstuff.core.block.BlockSRS;
import bluemonster122.mods.simplerandomstuff.reference.Names;
import bluemonster122.mods.simplerandomstuff.util.IFeatureRegistry;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class FRGrinder implements IFeatureRegistry {
    public static final FRGrinder INSTNACE = new FRGrinder();

    public static BlockSRS grinder = new BlockGrinder();

    @Override
    public void registerBlocks( ) {
        GameRegistry.register(grinder);
    }

    @Override
    public void registerItems( ) {

    }

    @Override
    public void registerRecipes( ) {

    }

    @Override
    public void registerTileEntities( ) {

    }

    @Override
    public void loadConfigs(Configuration configuration) {

    }

    @Override
    public void registerEvents( ) {

    }

    @Override
    public void registerOreDict( ) {

    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerRenders( ) {

    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerClientEvents( ) {

    }

    @Override
    public String getName( ) {
        return Names.Features.GRINDER;
    }

    private FRGrinder( ) {
    }
}
