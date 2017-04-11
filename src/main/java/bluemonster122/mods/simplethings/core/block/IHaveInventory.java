package bluemonster122.mods.simplethings.core.block;

import net.minecraftforge.items.ItemStackHandler;

public interface IHaveInventory {

    /**
     * Gets the Tile's current Inventory.
     * @return The Tile's current Inventory.
     */
    ItemStackHandler getInventory();

    /**
     * Creates a new Inventory for the Tile.
     * @return a new Inventory for the Tile.
     */
    ItemStackHandler createInventory();

    /**
     * Sets the given ItemStackHandler to be the Tile's Inventory.
     * @param inventory new Inventory.
     */
    void setInventory(ItemStackHandler inventory);
}
