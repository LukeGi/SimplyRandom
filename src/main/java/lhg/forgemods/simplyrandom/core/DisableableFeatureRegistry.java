package lhg.forgemods.simplyrandom.core;

import com.google.common.collect.Maps;
import lhg.forgemods.simplyrandom.cobblemaker.CobblestoneMaker;
import lhg.forgemods.simplyrandom.miner.Miner;
import lhg.forgemods.simplyrandom.treefarm.TreeFarm;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec.Builder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

public class DisableableFeatureRegistry
{
    /**
     * This is the storage place for all features
     */
    public static final Map<ResourceLocation, DisableableFeature> FEATURE_REGISTRY = Maps.newHashMap();
    /* Features */
    public static final CobblestoneMaker cobblestoneMaker = new CobblestoneMaker();
    public static final TreeFarm treeFarm = new TreeFarm();
    public static final Miner miner = new Miner();
    /**
     * LOGGER
     */
    private static final Logger LOGGER = LogManager.getLogger();

    public static void init()
    {
        LOGGER.info("Features being registered");
    }

    /**
     * Adds configs to the config spec for each feature
     *
     * @param spec server config spec
     */
    public static void constructConfigs(Builder spec)
    {
        for (DisableableFeature value : FEATURE_REGISTRY.values())
        {
            spec.push(value.name().getPath());
            value.enabled = spec.comment("Set to false to disable this block.")
                    .translation("simplyrandom.config.common.enabled")
                    .worldRestart()
                    .define("enabled", true);
            value.constructConfig(spec);
            spec.pop();
        }
    }
}
