package luhegi.simplyrandom.util;

import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.CapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

public final class EnergyHandlerHelper {
    private EnergyHandlerHelper() {
    }

    public static LazyOptional<IEnergyStorage> get(CapabilityProvider<?> capabilityProvider) {
        return capabilityProvider.getCapability(CapabilityEnergy.ENERGY);
    }

    public static LazyOptional<IEnergyStorage> get(CapabilityProvider<?> capabilityProvider, Direction dir) {
        return capabilityProvider.getCapability(CapabilityEnergy.ENERGY, dir);
    }
}
