package bluemonster.simplerandomstuff.proxy;

import bluemonster.simplerandomstuff.SRS;
import bluemonster.simplerandomstuff.util.IFeatureRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ClientProxy
        implements IProxy {
    @Override
    public void preInit() {
        for (IFeatureRegistry fr : SRS.featureRegistries) {
            if (SRS.shouldLoad(fr)) {
                fr.registerClientEvents();
            }
        }
    }

    @Override
    public void init() {
        /* NO OPERATION */
    }
}
