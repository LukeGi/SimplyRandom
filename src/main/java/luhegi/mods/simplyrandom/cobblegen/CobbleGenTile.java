package luhegi.mods.simplyrandom.cobblegen;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class CobbleGenTile extends TileEntity {
    public final CobbleGenEnergyInventory inventory = new CobbleGenEnergyInventory(this::cobbleTaken);
    public final LazyOptional<IItemHandler> inventoryCap = LazyOptional.of(() -> inventory);
    public final LazyOptional<IEnergyStorage> energyCap = LazyOptional.of(() -> inventory);
    public CobbleGenTile() {
        super(CobblestoneGenerator.INSTANCE.TILE_TYPE);
    }

    private void cobbleTaken(int amount) {
        markDirty();
        if (amount > 0) {
            world.playEvent(1501, pos, 0);
        }
    }

    @Override
    protected void invalidateCaps() {
        super.invalidateCaps();
        inventoryCap.invalidate();
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        inventory.write(compound);
        return super.write(compound);
    }

    @Override
    public void read(CompoundNBT compound) {
        inventory.read(compound);
        super.read(compound);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.equals(cap)) {
            return inventoryCap.cast();
        }
        if (CapabilityEnergy.ENERGY.equals(cap))
            return energyCap.cast();
        return super.getCapability(cap, side);
    }
}
