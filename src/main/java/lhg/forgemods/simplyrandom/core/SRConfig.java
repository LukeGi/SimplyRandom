package lhg.forgemods.simplyrandom.core;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.Builder;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.config.ModConfig.ConfigReloading;
import net.minecraftforge.fml.config.ModConfig.Loading;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;

/**
 * This is where all the configs are stored
 */
public class SRConfig
{
    public static final Server SERVER;
    public static final ForgeConfigSpec SERVER_SPEC;

    static
    {
        final Pair<Server, ForgeConfigSpec> serverSpecPair = new Builder().configure(Server::new);
        SERVER = serverSpecPair.getLeft();
        SERVER_SPEC = serverSpecPair.getRight();
    }

    @SubscribeEvent
    public static void onLoad(final Loading configEvent)
    {
        LogManager.getLogger().debug("Loaded forge config file {}", configEvent.getConfig().getFileName());
    }

    @SubscribeEvent
    public static void onFileChange(final ConfigReloading configEvent)
    {
        LogManager.getLogger().fatal("Simply Random config just got changed on the file system!");

    }

    /**
     * Server Config
     */
    public static class Server
    {

        public Server(final Builder spec)
        {
            spec.comment("Server/World specific configs.").push("server");
            DisableableFeatureRegistry.constructConfigs(spec);
            spec.pop();
        }
    }
}
