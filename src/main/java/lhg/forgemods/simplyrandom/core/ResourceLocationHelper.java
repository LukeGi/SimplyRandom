package lhg.forgemods.simplyrandom.core;

import net.minecraft.util.ResourceLocation;

import java.util.HashMap;

public final class ResourceLocationHelper
{
    /**
     * This is a ResourceLocation Cache for the mod, to attempt a decrease in memory usage
     */
    private static final HashMap<String, ResourceLocation> NAMES = new HashMap<>();

    private ResourceLocationHelper()
    {
    }

    /**
     * This method attempts to use the ResourceLocation cache if the parameter {@code name} has already
     * been passed to it before.
     *
     * @param name usually the Path half of a resource location
     * @return a valid ResourceLocation
     */
    public static ResourceLocation getOrCreateName(String name)
    {
        if (!name.startsWith("simplyrandom:"))
        {
            name = "simplyrandom:" + name;
        }
        return NAMES.computeIfAbsent(name, ResourceLocation::new);
    }
}
