package luhegi.mods.simplyrandom.basis.objects;

import luhegi.mods.simplyrandom.basis.tile.ICap;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class BasisTile<T extends BasisTile<T>> extends TileEntity {
    private final List<ICap<?>> caps = new ArrayList<>();

    public BasisTile(BasisTileType<T> type) {
        super(type);
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        caps.forEach(cap -> cap.save(compound));
        return super.write(compound);
    }

    @Override
    public void read(CompoundNBT compound) {
        caps.forEach(cap -> cap.load(compound));
        super.read(compound);
    }

    public void addCap(ICap<?> cap) {
        caps.add(cap);
    }

    @Override
    protected void invalidateCaps() {
        super.invalidateCaps();
        caps.stream().map(ICap::getCapability).forEach(LazyOptional::invalidate);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return caps.stream()
                .map(c -> c.getCapability(cap, side))
                .filter(LazyOptional::isPresent)
                .findFirst()
                .orElse(super.getCapability(cap, side))
                .cast();
    }
}
