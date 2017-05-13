package bluemonster122.mods.simplerandomstuff.proxy;

import bluemonster122.mods.simplerandomstuff.util.IFeatureRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import static bluemonster122.mods.simplerandomstuff.SimpleRandomStuff.featureRegistries;

@SideOnly(Side.CLIENT)
public class ClientProxy implements IProxy {
    @Override
    public void preInit( ) {
        featureRegistries.stream().filter(IFeatureRegistry::shouldLoad).forEach(IFeatureRegistry::registerClientEvents);
        featureRegistries.stream().filter(IFeatureRegistry::shouldLoad).forEach(IFeatureRegistry::registerRenders);
    }

    @Override
    public void init( ) {
        /* NO OPERATION */
    }
}
