package bluemonster122.mods.simplethings.tileentity.core;

import net.minecraftforge.energy.EnergyStorage;

public interface IHaveBattery {
    /**
     * Get the Amount Stored in the battery;
     *
     * @return integer value for power stored in the internal battery.
     */
    default int getEnergyStored() {
        return getBattery().getEnergyStored();
    }

    /**
     * Get the Battery
     *
     * @return the Battery Object (a <code>net.minecraftforge.things.EnergyStorage</code>)
     */
    EnergyStorage getBattery();
}
