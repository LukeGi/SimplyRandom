package luhegi.mods.simplyrandom;

import luhegi.mods.simplyrandom.basis.setup.ModSetupManager;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

@Mod(SimplyRandom.ID)
public class SimplyRandom {
    public static final String ID = "simplyrandom";
    public static final String VERSION = "1.0.0";
    public static final String NAME = "Simply Random";
    public static final Marker LOG_MARKER = MarkerManager.getMarker(NAME);

    public static SimplyRandom INSTANCE;

    public SimplyRandom() {
        if (INSTANCE != null) throw new IllegalStateException("Mod was initialized twice.");
        INSTANCE = this;
        ModSetupManager.INSTANCE.onConstruct();
    }
}
