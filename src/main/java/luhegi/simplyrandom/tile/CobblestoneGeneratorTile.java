package luhegi.simplyrandom.tile;

import com.google.common.collect.ImmutableSet;
import luhegi.simplyrandom.SimplyRandom;
import luhegi.simplyrandom.tile.energy.ConsumerEnergyStorage;
import luhegi.simplyrandom.util.LazyOptionalHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class CobblestoneGeneratorTile extends TileEntity {
    private CobblestoneGeneratorInventory inventory = new CobblestoneGeneratorInventory(this);
    private ConsumerEnergyStorage battery = new ConsumerEnergyStorage(SimplyRandom.config.cobblestone_generator_capacity.get());

    public CobblestoneGeneratorTile() {
        super(SimplyRandom.instance.cobblestone_generator_tile.get());
    }

    private int getCobbleLeft() {
        if (SimplyRandom.config.cobblestone_generator_cost.get() > 0) {
            return battery.getEnergyStored() / SimplyRandom.config.cobblestone_generator_cost.get();
        }
        return Integer.MAX_VALUE;
    }

    private boolean removeCobble(int amount) {
        if (SimplyRandom.config.cobblestone_generator_cost.get() > 0) {
            int energyCost = SimplyRandom.config.cobblestone_generator_cost.get() * amount;
            if (battery.hasEnergy(energyCost)) {
                battery.removeEnergy(energyCost);
                return true;
            } else {
                return false;
            }
        }
        return true;
    }

    private LazyOptional<IItemHandler> cobbleHandler = LazyOptional.empty();

    private LazyOptional<IItemHandler> getCobbleInventory() {
        if (!cobbleHandler.isPresent()) {
            cobbleHandler = LazyOptional.of(() -> inventory);
        }
        return cobbleHandler;
    }

    private LazyOptional<IEnergyStorage> energyHandler = LazyOptional.empty();

    private LazyOptional<IEnergyStorage> getEnergyHandler() {
        if (SimplyRandom.config.cobblestone_generator_cost.get() > 0) {
            return LazyOptional.empty();
        }
        if (!energyHandler.isPresent()) {
            energyHandler = LazyOptional.of(() -> battery);
        }

        return energyHandler;
    }

    @Override
    protected void invalidateCaps() {
        super.invalidateCaps();
        cobbleHandler.invalidate();
        energyHandler.invalidate();
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return LazyOptionalHelper.findFirst(ImmutableSet.of(
                () -> CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.orEmpty(cap, getCobbleInventory()),
                () -> CapabilityEnergy.ENERGY.orEmpty(cap, getEnergyHandler()),
                () -> super.getCapability(cap, side)
        ));
    }

    private static class CobblestoneGeneratorInventory implements IItemHandler {
        private CobblestoneGeneratorTile tile;

        CobblestoneGeneratorInventory(CobblestoneGeneratorTile tile) {
            this.tile = tile;
        }

        @Override
        public int getSlots() {
            return tile.getCobbleLeft() / new ItemStack(Items.COBBLESTONE).getMaxStackSize();
        }

        @Nonnull
        @Override
        public ItemStack getStackInSlot(int slot) {
            return slot < getSlots() ? new ItemStack(Items.COBBLESTONE, Math.min(64, tile.getCobbleLeft())) : ItemStack.EMPTY;
        }

        @Nonnull
        @Override
        public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
            if (Tags.Items.COBBLESTONE.contains(stack.getItem())) {
                return ItemStack.EMPTY;
            }
            return stack;
        }

        @Nonnull
        @Override
        public ItemStack extractItem(int slot, int amount, boolean simulate) {
            ItemStack copy = getStackInSlot(slot).copy();
            if (copy.getCount() > amount) {
                copy.setCount(amount);
            }
            if (!simulate) {
                tile.removeCobble(amount);
            }

            return copy;
        }

        @Override
        public int getSlotLimit(int slot) {
            return getStackInSlot(slot).getMaxStackSize();
        }

        @Override
        public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
            return Tags.Items.COBBLESTONE.contains(stack.getItem());
        }
    }
}
