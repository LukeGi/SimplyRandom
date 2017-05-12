package bluemonster122.mods.simplerandomstuff.generators;

import bluemonster122.mods.simplerandomstuff.core.block.TileST;
import bluemonster122.mods.simplerandomstuff.core.energy.BatteryST;
import bluemonster122.mods.simplerandomstuff.core.energy.IEnergyProviderST;
import com.google.common.collect.ImmutableMap;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

import java.util.Map;

public abstract class TileGenerator extends TileST implements IEnergyProviderST {

    public BatteryST battery = createBattery();

    /**
     * Gets the Tile's current battery.
     *
     * @return The Tile's current battery.
     */
    @Override
    public BatteryST getBattery( ) {
        return battery;
    }

    /**
     * Sets the given BatteryST to be the Tile's Battery.
     *
     * @param battery new Battery.
     */
    @Override
    public void setBattery(BatteryST battery) {
        this.battery = battery;
    }

    @Override
    public Map<Capability, Capability> getCaps( ) {
        return ImmutableMap.of(CapabilityEnergy.ENERGY, CapabilityEnergy.ENERGY.cast((IEnergyStorage) battery));
    }
}