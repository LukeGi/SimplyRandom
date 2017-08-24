package bluemonster.simplerandomstuff.core;

public class BatteryMachine extends Battery {
    public BatteryMachine(int capacity, int maxIn) {
        super(capacity, maxIn, 0);
    }

    public int useEnergy(int amount, boolean simulate) {
        int energyUsed = Math.min(amount, energy);
        if (!simulate) energy -= energyUsed;
        return energyUsed;
    }

    @Override
    public boolean canExtract() {
        return false;
    }
}
