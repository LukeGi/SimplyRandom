package bluemonster122.simplethings.tileentity.things;

import net.minecraftforge.items.ItemStackHandler;

public interface IHaveInventory
{
    /**
     * Get the Inventory
     * @return the Inventory Object (a <code>net.minecraftforge.items.ItemStackHandler</code>
     */
    ItemStackHandler getInventory();
}
