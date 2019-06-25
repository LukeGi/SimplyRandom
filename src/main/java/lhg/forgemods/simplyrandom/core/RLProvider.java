package lhg.forgemods.simplyrandom.core;

import net.minecraft.util.ResourceLocation;

import java.util.WeakHashMap;

public final class RLProvider
{
    /**
     * This is a ResourceLocation CapabilityCache for the mod, to attempt a decrease in memory usage
     */
    private static final WeakHashMap<String, ResourceLocation> NAMES = new WeakHashMap<>();

    private RLProvider()
    {
    }

    /**
     * This method attempts to use the ResourceLocation cache if the parameter {@code name} has already
     * been passed to it before.
     *
     * @param name usually the Path half of a resource location
     * @return a valid ResourceLocation
     */
    public static ResourceLocation get(String name)
    {
        if (!name.startsWith("simplyrandom:"))
        {
            name = "simplyrandom:" + name;
        }
        return NAMES.computeIfAbsent(name, ResourceLocation::new);
    }
}
