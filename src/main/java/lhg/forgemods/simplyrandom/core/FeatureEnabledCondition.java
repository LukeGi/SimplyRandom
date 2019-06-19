package lhg.forgemods.simplyrandom.core;

import com.google.gson.JsonObject;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.IConditionSerializer;

import java.util.function.BooleanSupplier;

/**
 * A recipe condition which checks if a feature is enabled
 */
public class FeatureEnabledCondition implements IConditionSerializer
{
    @Override
    public BooleanSupplier parse(JsonObject json)
    {
        final DisableableFeature feature = DisableableFeatureRegistry.FEATURE_REGISTRY.get(new ResourceLocation(json.get("feature_name").getAsString()));
        return () -> feature != null && feature.enabled();
    }
}
