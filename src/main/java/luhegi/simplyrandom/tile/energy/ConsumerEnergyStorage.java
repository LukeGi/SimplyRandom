package luhegi.simplyrandom.tile.energy;

import net.minecraftforge.energy.IEnergyStorage;

public class ConsumerEnergyStorage implements IEnergyStorage {
    private final int capacity;
    private final int maxReceive;
    private int stored = 0;

    public ConsumerEnergyStorage(int capacity) {
        this(capacity, capacity);
    }

    public ConsumerEnergyStorage(int capacity, int maxReceive) {
        this.capacity = capacity;
        this.maxReceive = maxReceive;
    }

    public boolean hasEnergy(int amount) {
        return stored > amount;
    }

    public void removeEnergy(int amount) {
        stored -= amount;
    }

    public void setStored(int amount) {
        this.stored = amount;
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        int accepted = Math.min(Math.min(maxReceive, this.maxReceive), capacity - stored);
        if (!simulate) {
            stored += accepted;
        }
        return accepted;
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        return 0;
    }

    @Override
    public int getEnergyStored() {
        return stored;
    }

    @Override
    public int getMaxEnergyStored() {
        return capacity;
    }

    @Override
    public boolean canExtract() {
        return false;
    }

    @Override
    public boolean canReceive() {
        return true;
    }
}
