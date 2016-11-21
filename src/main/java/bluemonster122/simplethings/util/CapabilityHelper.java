package bluemonster122.simplethings.util;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import java.util.function.Predicate;

public class CapabilityHelper
{
    private static Predicate<Capability<?>> isItemHandler = capability -> capability.equals(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY);
    private static Predicate<Capability<?>> isEnergyHandler = capability -> capability.equals(CapabilityEnergy.ENERGY);

    @SuppressWarnings("unchecked")
    public static <T> T getCapability(TileEntity tileEntity, Capability<T> capability)
    {
        if (isItemHandler.test(capability) && tileEntity instanceof IItemHandler)
            return (T) CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast((IItemHandler) tileEntity);
        else if (isEnergyHandler.test(capability) && tileEntity instanceof IEnergyStorage)
            return (T) CapabilityEnergy.ENERGY.cast((IEnergyStorage) tileEntity);
        else
            return null;
    }

    public static boolean hasCapability(TileEntity tileEntity, Capability<?> capability)
    {
        if (isItemHandler.test(capability) && tileEntity instanceof IItemHandler) return true;
        else if (isEnergyHandler.test(capability) && tileEntity instanceof IEnergyStorage) return true;
        else return false;
    }
}
