package bluemonster122.mods.simplethings.tileentity.core;

import bluemonster122.mods.simplethings.tileentity.TilePowerCable;
import com.google.common.collect.ImmutableMap;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public abstract class TileEntityST extends TileEntity {
    private Set<Consumer<NBTTagCompound>> minWrites = getMinWrites();
    private Set<Consumer<NBTTagCompound>> allWrites = getAllWrites();
    private Set<Consumer<NBTTagCompound>> minReads = getMinReads();
    private Set<Consumer<NBTTagCompound>> allReads = getAllReads();

    /* SAVE TO DISK METHODS */

    public NBTTagCompound writeNBTLegacy(NBTTagCompound tag) {
        return super.writeToNBT(tag);
    }

    public void readNBTLegacy(NBTTagCompound tag) {
        super.readFromNBT(tag);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        allWrites.forEach(c -> c.accept(tag));
        return tag;
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        allReads.forEach(c -> c.accept(tag));
    }

    public abstract Set<Consumer<NBTTagCompound>> getAllWrites();

    public abstract Set<Consumer<NBTTagCompound>> getAllReads();

    /* CAPABILITY METHODS */

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
        Map<Capability, Supplier<Capability>> caps = getCaps();
        return caps.keySet().contains(capability) || super.hasCapability(capability, facing);
    }

    @SuppressWarnings("unchecked")
    @Nullable
    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
        Map<Capability, Supplier<Capability>> caps = getCaps();
        for (ImmutableMap.Entry cap : caps.entrySet()) {
            if (capability.equals(cap.getKey())) {
                return (T) cap.getValue();
            }
        }
        return super.getCapability(capability, facing);
    }

    public abstract Map<Capability, Supplier<Capability>> getCaps();

    /* CLIENT SYNC METHODS */

    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        return new SPacketUpdateTileEntity(getPos(), 0, getUpdateTag());
    }

    @Override
    public void handleUpdateTag(NBTTagCompound tag) {
        minReads.forEach(c -> c.accept(tag));
    }

    @Override
    @Nonnull
    public NBTTagCompound getUpdateTag() {
        NBTTagCompound tag = new NBTTagCompound();
        minWrites.forEach(c -> c.accept(tag));
        return tag;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity nbt) {
        handleUpdateTag(nbt.getNbtCompound());
    }

    public abstract Set<Consumer<NBTTagCompound>> getMinWrites();

    public abstract Set<Consumer<NBTTagCompound>> getMinReads();

    //@formatter:off

    // TODO: Takeout network methods from this class, put into TileEntityEnergy
    private List<BlockPos> networkCache = new ArrayList<>();
    protected void attemptDischarge() {
        if (!(this instanceof IProvidePower)) return;
        IEnergyStorage thisBattery = ((IHaveBattery) this).getBattery();

        List<BlockPos> network = scanNetwork();
        List<TileEntity> tiles = network.stream().map(getWorld()::getTileEntity).collect(Collectors.toList());
        for (TileEntity tile : tiles) {
            IEnergyStorage battery = tile.getCapability(CapabilityEnergy.ENERGY, null);
            if (battery != null) {
                thisBattery.extractEnergy(battery.receiveEnergy(thisBattery.getEnergyStored(), false), false);
                if (tile instanceof TileEntityST) {
                    ((TileEntityST) tile).sendUpdate();
                }
                if (thisBattery.getEnergyStored() <= 0) {
                    break;
                }
            }
        }
    }

    private List<BlockPos> scanNetwork() {
        flush();
        List<BlockPos> visited = new ArrayList<>();
        Stack<BlockPos> toVisit = new Stack<>();
        toVisit.add(getPos());

        while (!toVisit.isEmpty()) {
            BlockPos blockPos = toVisit.pop();
            TileEntity tileEntity = getWorld().getTileEntity(blockPos);
            if (tileEntity != null && (tileEntity.hasCapability(CapabilityEnergy.ENERGY, null) || tileEntity instanceof TilePowerCable)) {
                IEnergyStorage battery = tileEntity.getCapability(CapabilityEnergy.ENERGY, null);
                if (!networkCache.contains(blockPos) && battery != null && battery.canReceive()) {
                    networkCache.add(blockPos);
                }
                for (EnumFacing value : EnumFacing.VALUES) {
                    BlockPos element = blockPos.offset(value);
                    if (!getWorld().isAirBlock(element) && !toVisit.contains(element) && !visited.contains(element)) {
                        toVisit.add(element);
                    }
                    visited.add(element);
                }
            }
        }
        return networkCache;
    }

    protected void sendUpdate() {
        getWorld().notifyBlockUpdate(
                getPos(), getWorld().getBlockState(getPos()), getWorld().getBlockState(getPos()), 3);
    }

    private void flush() {
        networkCache = networkCache.stream().filter(blockPos -> getWorld().getTileEntity(blockPos) != null).filter(blockPos -> getWorld().getTileEntity(blockPos).hasCapability(CapabilityEnergy.ENERGY, null)).filter(blockPos -> getWorld().getTileEntity(blockPos).getCapability(CapabilityEnergy.ENERGY, null).canReceive()).collect(Collectors.toList());
    }

    public void dropInventory() {
        if (getWorld().isRemote) {
            return;
        }
        if (this instanceof IHaveInventory) {
            for (int i = 0; i < ((IHaveInventory) this).getInventory().getSlots(); ++i) {
                ItemStack itemstack = ((IHaveInventory) this).getInventory().getStackInSlot(i);
                if (itemstack != ItemStack.EMPTY) {
                    InventoryHelper.spawnItemStack(
                            getWorld(), getPos().getX(), getPos().getY(), getPos().getZ(), itemstack);
                }
            }
        }
    }
    //@formatter:on
}
