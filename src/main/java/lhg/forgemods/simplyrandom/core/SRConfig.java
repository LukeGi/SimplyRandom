package lhg.forgemods.simplyrandom.core;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.common.ForgeConfigSpec.Builder;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;
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
        public final CobblestoneMakerConfig cobblestoneMakerConfig;
        public final TreeFarmConfig treeFarmConfig;
        public final MinerConfig minerConfig;

        public Server(final Builder spec)
        {
            spec.comment("Server/World specific configs.").push("server");
            this.cobblestoneMakerConfig = new CobblestoneMakerConfig(spec);
            this.treeFarmConfig = new TreeFarmConfig(spec);
            this.minerConfig = new MinerConfig(spec);
            spec.pop();
        }
    }

    /**
     * This contains all the configs related to the Tree Farm feature
     */
    public static class TreeFarmConfig
    {
        public final BooleanValue enabled;
        public final IntValue blockScanEnergy;
        public final IntValue inventoryScanEnergy;
        public final IntValue leafBreakEnergy;
        public final IntValue logBreakEnergy;
        public final IntValue maxPower;

        public TreeFarmConfig(Builder spec)
        {
            spec.push("tree_farm");
            this.enabled = spec.comment("Set to false to disable this block.")
                    .translation("simplyrandom.config.common.enabled")
                    .worldRestart()
                    .define("enabled", true);
            this.blockScanEnergy = spec.comment("This will decide how much energy is required to scan a block in the world. A value of 0 makes it free.")
                    .translation("simplyrandom.config.tree_farm.blockScanEnergy")
                    .worldRestart()
                    .defineInRange("blockScanEnergy", 0, 0, Integer.MAX_VALUE);
            this.inventoryScanEnergy = spec.comment("This will decide how much energy is required to scan a inventories for saplings. A value of 0 makes it free.")
                    .translation("simplyrandom.config.tree_farm.inventoryScanEnergy")
                    .worldRestart()
                    .defineInRange("inventoryScanEnergy", 0, 0, Integer.MAX_VALUE);
            this.leafBreakEnergy = spec.comment("This will decide how much energy is required to break a leaf block. A value of 0 makes it free.")
                    .translation("simplyrandom.config.tree_farm.leafBreakEnergy")
                    .worldRestart()
                    .defineInRange("leafBreakEnergy", 0, 0, Integer.MAX_VALUE);
            this.logBreakEnergy = spec.comment("This will decide how much energy is required to break a log block. A value of 0 makes it free.")
                    .translation("simplyrandom.config.tree_farm.logBreakEnergy")
                    .worldRestart()
                    .defineInRange("logBreakEnergy", 0, 0, Integer.MAX_VALUE);
            this.maxPower = spec.comment("This will decide how much energy can be stored in a tree farm. A value of 0 means will mean you can't use it if you have setup any power requirements.")
                    .translation("simplyrandom.config.tree_farm.maxPower")
                    .worldRestart()
                    .defineInRange("maxPower", 0, 0, Integer.MAX_VALUE);
            spec.pop();
        }
    }

    /**
     * This contains all the configs relating to the Cobblestone Maker feature
     */
    public static class CobblestoneMakerConfig
    {
        public final BooleanValue enabled;
        public final IntValue energyPerCobble;

        public CobblestoneMakerConfig(final Builder spec)
        {
            spec.push("cobblestone_maker");
            this.enabled = spec.comment("Set to false to disable this block.")
                    .translation("simplyrandom.config.common.enabled")
                    .worldRestart()
                    .define("enabled", true);
            this.energyPerCobble = spec.comment("This will decide how much energy is required to create a cobblestone in the cobblestone maker. A value of 0 makes it free.")
                    .translation("simplyrandom.config.cobblestone_maker.energyPerCobble")
                    .worldRestart()
                    .defineInRange("energyPerCobble", 0, 0, Integer.MAX_VALUE);
            spec.pop();
        }
    }

    /**
     * This contains all the configs relating to the Miner feature
     */
    public static class MinerConfig
    {
        public final BooleanValue enabled;

        public MinerConfig(final Builder spec)
        {
            spec.push("miner");
            this.enabled = spec.comment("Set to false to disable this block.")
                    .translation("simplyrandom.config.common.enabled")
                    .worldRestart()
                    .define("enabled", true);
            spec.pop();
        }
    }
}
