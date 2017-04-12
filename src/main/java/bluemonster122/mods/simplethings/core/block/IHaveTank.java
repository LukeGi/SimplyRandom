package bluemonster122.mods.simplethings.core.block;

import net.minecraftforge.fluids.FluidTank;

public interface IHaveTank {
    /**
     * Gets the Tile's current Tank.
     * @return The Tile's current Tank.
     */
    FluidTank getTank();

    /**
     * Creates a new Tank for the Tile.
     * @return a new Tank for the Tile.
     */
    FluidTank createTank();

    /**
     * Sets the given ItemStackHandler to be the Tile's Tank.
     * @param tank new Inventory.
     */
    void setTank(FluidTank tank);
}