package bluemonster122.mods.simplethings.tileentity.core;

import net.minecraftforge.items.ItemStackHandler;

public interface IHaveInventory {
    /**
     * Get the Inventory
     *
     * @return the Inventory Object (a <code>net.minecraftforge.item.ItemStackHandler</code>
     */
    ItemStackHandler getInventory();
}
