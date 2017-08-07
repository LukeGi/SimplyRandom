package bluemonster122.mods.simplerandomstuff.core.energy;

public interface IHaveBattery {
    /**
     * Gets the Tile's current battery.
     *
     * @return The Tile's current battery.
     */
    BatteryST getBattery();

    /**
     * Sets the given BatteryST to be the Tile's Battery.
     *
     * @param battery new Battery.
     */
    void setBattery(BatteryST battery);

    /**
     * Creates a new Battery for the Tile.
     *
     * @return a new Battery for the Tile.
     */
    BatteryST createBattery();
}
