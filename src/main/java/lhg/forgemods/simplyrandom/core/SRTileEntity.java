package lhg.forgemods.simplyrandom.core;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * The base for all TileEntities in the mod, handles provision and storage of item and energy capabilities.
 *
 * @param <T> Specific Tile Type
 */
public abstract class SRTileEntity<T extends SRTileEntity<T>> extends TileEntity
{
    @CapabilityInject(IItemHandler.class)
    private static Capability<IItemHandler> ITEM_HANDLER_CAPABILITY = null;
    @CapabilityInject(IEnergyStorage.class)
    private static Capability<IEnergyStorage> ENERGY_CAPABILITY = null;
    protected LazyOptional<IEnergyStorage> energyCapability = LazyOptional.empty();
    protected LazyOptional<IItemHandler> itemCapability = LazyOptional.empty();

    public SRTileEntity(TileEntityType<T> type)
    {
        super(type);
    }

    @Override
    protected void invalidateCaps()
    {
        super.invalidateCaps();
        energyCapability.invalidate();
        itemCapability.invalidate();
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side)
    {
        populateCaps();
        this.markDirty();
        if (ENERGY_CAPABILITY == cap && energyCapability.isPresent())
        {
            return energyCapability.cast();
        } else if (ITEM_HANDLER_CAPABILITY == cap && itemCapability.isPresent())
        {
            return itemCapability.cast();
        } else
        {
            return super.getCapability(cap, side);
        }
    }

    @Override
    public void read(CompoundNBT compound)
    {
        populateCaps();
        energyCapability.ifPresent(cap -> ENERGY_CAPABILITY.readNBT(cap, null, compound.get("[SR]energy")));
        itemCapability.ifPresent(cap -> ITEM_HANDLER_CAPABILITY.readNBT(cap, null, compound.get("[SR]items")));
        super.read(compound);
    }

    @Override
    public CompoundNBT write(CompoundNBT compound)
    {
        populateCaps();
        energyCapability.ifPresent(cap -> compound.put("[SR]energy", ENERGY_CAPABILITY.writeNBT(cap, null)));
        itemCapability.ifPresent(cap -> compound.put("[SR]items", ITEM_HANDLER_CAPABILITY.writeNBT(cap, null)));
        return super.write(compound);
    }

    /**
     * If you are using one of the capabilities provided by {@link SRTileEntity SRTileEntitiy}, you should populate it's
     * {@link LazyOptional optional} counterpart if it is not already valid.
     */
    protected abstract void populateCaps();
}
