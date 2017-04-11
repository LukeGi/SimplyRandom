package bluemonster122.mods.simplethings.util;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public interface IFeatureRegistry {
    void registerBlocks();

    void registerItems();

    void registerRecipes();

    void registerTileEntities();

    void registerEvents();

    @SideOnly(Side.CLIENT)
    void registerRenders();

    @SideOnly(Side.CLIENT)
    void registerClientEvents();
}
