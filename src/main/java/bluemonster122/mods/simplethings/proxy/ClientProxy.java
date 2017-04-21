package bluemonster122.mods.simplethings.proxy;

import bluemonster122.mods.simplethings.util.IFeatureRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import static bluemonster122.mods.simplethings.SimpleThings.featureRegistries;

@SideOnly(Side.CLIENT)
public class ClientProxy implements IProxy {
    @Override
    public void preInit( ) {
        featureRegistries.forEach(IFeatureRegistry::registerClientEvents);
        featureRegistries.forEach(IFeatureRegistry::registerRenders);
    }

    @Override
    public void init( ) {
        /* NO OPERATION */
    }
}
