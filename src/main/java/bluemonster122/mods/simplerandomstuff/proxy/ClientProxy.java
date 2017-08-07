package bluemonster122.mods.simplerandomstuff.proxy;

import bluemonster122.mods.simplerandomstuff.SRS;
import bluemonster122.mods.simplerandomstuff.util.IFeatureRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import static bluemonster122.mods.simplerandomstuff.SRS.featureRegistries;

@SideOnly(Side.CLIENT)
public class ClientProxy implements IProxy {
    @Override
    public void preInit( ) {
        for (IFeatureRegistry fr : featureRegistries) {
            if (SRS.shouldLoad(fr)) {
                fr.registerClientEvents();
            }
        }
    }

    @Override
    public void init( ) {
        /* NO OPERATION */
    }
}
