package luhegi.mods.simplyrandom.basis.config;

import luhegi.mods.simplyrandom.SimplyRandom;
import luhegi.mods.simplyrandom.basis.setup.ModSetupManager;
import net.minecraftforge.common.ForgeConfig;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static net.minecraftforge.fml.loading.LogMarkers.FORGEMOD;

public class BasisConfig {
    private static final Logger LOGGER = LogManager.getLogger(BasisConfig.class);

    public static class Client {
        public Client(ForgeConfigSpec.Builder spec) {
            ModSetupManager.INSTANCE.onClientConfig(spec);
        }
    }

    public static class Server {
        public Server(ForgeConfigSpec.Builder spec) {
            ModSetupManager.INSTANCE.onServerConfig(spec);
        }
    }


    public static final ForgeConfigSpec clientSpec;
    public static final Client CLIENT;

    static {
        final Pair<Client, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Client::new);
        clientSpec = specPair.getRight();
        CLIENT = specPair.getLeft();
    }

    public static final ForgeConfigSpec serverSpec;
    public static final Server SERVER;

    static {
        final Pair<Server, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Server::new);
        serverSpec = specPair.getRight();
        SERVER = specPair.getLeft();
    }

    public static void onLoad(final ModConfig.Loading configEvent) {
        LOGGER.debug(SimplyRandom.LOG_MARKER, "Loaded Simply Random's config file {}", configEvent.getConfig().getFileName());
    }

    public static void onFileChange(final ModConfig.Reloading configEvent) {
        LOGGER.debug(SimplyRandom.LOG_MARKER, "Simply Random's config just got changed on the file system!");
    }
}
