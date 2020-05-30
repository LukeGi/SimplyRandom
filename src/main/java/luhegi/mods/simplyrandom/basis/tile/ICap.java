package luhegi.mods.simplyrandom.basis.tile;

import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface ICap<C> extends ISaveable, ICapabilityProvider {
    C getInstance();

    LazyOptional<C> getCapability();

    Capability<C> getType();

    default String getName() {
        return getType().getName();
    }

    default boolean matches(Capability<?> cap) {
        return getType().equals(cap);
    }

    @Nonnull
    @Override
    default <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return matches(cap) ? getCapability().cast() : LazyOptional.empty();
    }
}
