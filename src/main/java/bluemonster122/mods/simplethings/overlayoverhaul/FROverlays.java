package bluemonster122.mods.simplethings.overlayoverhaul;

import bluemonster122.mods.simplethings.util.IFeatureRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class FROverlays implements IFeatureRegistry {
    public static final FROverlays INSTANCE = new FROverlays();
    public static boolean useCustom = false;
    public static boolean showLevelUp = false;

    @Override
    public void registerBlocks( ) {

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
        useCustom = configuration.getBoolean("Use Custom in game UI", "Overlay_Overhaul", true, "Set to false to disable custom overlay.");
        showLevelUp = configuration.getBoolean("Show Level up message", "Overlay_Overhaul", true, "Set to false to disable level up message.");
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
        MinecraftForge.EVENT_BUS.register(new OverlayOverriders());
    }

    private FROverlays( ) {
    }
}
