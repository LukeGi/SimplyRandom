package bluemonster.simplerandomstuff.core;

import cofh.redstoneflux.impl.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;

public class Battery extends EnergyStorage implements IEnergyStorage {
    public Battery(int capacity) {
        super(capacity);
    }

    public Battery(int capacity, int maxTransfer) {
        super(capacity, maxTransfer);
    }

    public Battery(int capacity, int maxReceive, int maxExtract) {
        super(capacity, maxReceive, maxExtract);
    }

    @Override
    public boolean canExtract() {
        return true;
    }

    @Override
    public boolean canReceive() {
        return true;
    }
}
