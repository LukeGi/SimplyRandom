package luhegi.simplyrandom.util;

import net.minecraftforge.common.util.LazyOptional;

import java.util.Set;
import java.util.function.Supplier;

public class LazyOptionalHelper {
    public static <U> LazyOptional<U> findFirst(Set<Supplier<LazyOptional<?>>> los) {
        for (Supplier<LazyOptional<?>> sup : los) {
            LazyOptional<?> lo = sup.get();
            if (lo.isPresent()) {
                return lo.cast();
            }
        }
        return LazyOptional.empty();
    }
}
