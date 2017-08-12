package bluemonster.simplerandomstuff.overlayoverhaul;

import bluemonster.simplerandomstuff.reference.Names;
import bluemonster.simplerandomstuff.util.IFeatureRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;

public class FROverlays
        implements IFeatureRegistry {
    public static final FROverlays INSTANCE = new FROverlays();
    private boolean useCustom = false;
    private boolean showLevelUp = false;

    @Override
    public void registerBlocks(IForgeRegistry<Block> registry) {
        /* NO OPERATION */
    }

    @Override
    public void registerItems(IForgeRegistry<Item> registry) {
        /* NO OPERATION */
    }

    @Override
    public void registerTileEntities() {
        /* NO OPERATION */
    }

    @Override
    public void loadConfigs(Configuration configuration) {
        setUseCustom(configuration.getBoolean(
                Names.Features.Configs.OVERLAYS_USE_CUSTOM_OVERLAYS,
                Names.Features.OVERLAYS,
                true,
                "Set to false to disable custom overlay."
        ));
        setShowLevelUp(configuration.getBoolean(
                Names.Features.Configs.OVERLAYS_SHOW_LVLUPMSG,
                Names.Features.OVERLAYS,
                true,
                "Set to false to disable level up message."
        ));
    }

    @Override
    public void registerEvents() {
        /* NO OPERATION */
    }

    @Override
    public void registerOreDict() {
        /* NO OPERATION */
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerRenders() {
        /* NO OPERATION */
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerClientEvents() {
        MinecraftForge.EVENT_BUS.register(new OverlayOverriders());
    }

    @Override
    public String getName() {
        return Names.Features.OVERLAYS;
    }

    private FROverlays() {
    }

    public boolean shouldUseCustom() {
        return useCustom;
    }

    public void setUseCustom(boolean useCustom) {
        this.useCustom = useCustom;
    }

    public boolean shouldShowLevel() {
        return showLevelUp;
    }

    public void setShowLevelUp(boolean showLevelUp) {
        this.showLevelUp = showLevelUp;
    }
}
