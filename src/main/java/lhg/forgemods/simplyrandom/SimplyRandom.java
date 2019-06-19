package lhg.forgemods.simplyrandom;

import lhg.forgemods.simplyrandom.cobblemaker.CobblestoneMaker;
import lhg.forgemods.simplyrandom.core.DisableableFeatureRegistry;
import lhg.forgemods.simplyrandom.core.FeatureEnabledCondition;
import lhg.forgemods.simplyrandom.core.SRConfig;
import lhg.forgemods.simplyrandom.render.BoxRenderManager;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig.Type;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod("simplyrandom")
@SuppressWarnings("ConstantConditions")
public class SimplyRandom
{
    /**
     * Simply Random's Item Group
     */
    public static final ItemGroup itemGroup = new ItemGroup("simplyrandom")
    {
        @Override
        public ItemStack createIcon()
        {
            return new ItemStack(CobblestoneMaker.COBBLESTONE_MAKER_ITEM);
        }
    };
    /**
     * LOGGER
     */
    private static final Logger LOGGER = LogManager.getLogger();

    /**
     * Mod Constructor
     */
    public SimplyRandom()
    {
        ModLoadingContext.get().registerConfig(Type.SERVER, SRConfig.SERVER_SPEC);
        DisableableFeatureRegistry.init();
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onCommonSetup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onClientSetup);
    }

    private void onCommonSetup(final FMLCommonSetupEvent event)
    {
        LOGGER.info(">>> Simply Random will now Commence General Setup.");
        CraftingHelper.register(new ResourceLocation("simplyrandom:feature_enabled"), new FeatureEnabledCondition());
    }

    private void onClientSetup(final FMLClientSetupEvent event)
    {
        LOGGER.info(">>> Simply Random will now Commence Client Setup.");
        MinecraftForge.EVENT_BUS.register(BoxRenderManager.INSTANCE);
    }
}
