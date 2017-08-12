package bluemonster.simplerandomstuff.core.energy;

public interface IHaveBattery {
    /**
     * Creates a new Battery for the Tile.
     *
     * @return a new Battery for the Tile.
     */
    BatteryST createBattery();

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
}
