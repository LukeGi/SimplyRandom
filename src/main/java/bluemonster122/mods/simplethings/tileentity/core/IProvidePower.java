package bluemonster122.mods.simplethings.tileentity.core;

public interface IProvidePower extends IHaveBattery {
    /**
     * Forward method to internal battery.
     */
    default int extractPower(int amount, boolean fake) {
        return getBattery().extractEnergy(amount, fake);
    }
}
