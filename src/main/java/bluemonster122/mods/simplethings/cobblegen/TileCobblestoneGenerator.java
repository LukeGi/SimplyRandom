package bluemonster122.mods.simplethings.cobblegen;

import bluemonster122.mods.simplethings.core.block.IHaveInventory;
import bluemonster122.mods.simplethings.core.block.TileST;
import bluemonster122.mods.simplethings.core.energy.BatteryST;
import bluemonster122.mods.simplethings.core.energy.IEnergyRecieverST;
import com.google.common.collect.ImmutableMap;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;

import java.util.Map;
import java.util.function.Supplier;

public class TileCobblestoneGenerator extends TileST implements ITickable, IEnergyRecieverST, IHaveInventory {
    /**
     * Inventory
     */
    private ItemStackHandler inventory = createInventory();
    /**
     * Battery
     */
    private BatteryST battery = createBattery();

    @Override
    public Map<Capability, Supplier<Capability>> getCaps() {
        return ImmutableMap.of(
                CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(inventory),
                CapabilityEnergy.ENERGY, CapabilityEnergy.ENERGY.cast(battery)
        );
    }

    @Override
    public NBTTagCompound writeChild(NBTTagCompound tag) {
        return tag;
    }

    @Override
    public NBTTagCompound readChild(NBTTagCompound tag) {
        return tag;
    }

    /**
     * Like the old updateEntity(), except more generic.
     */
    @Override
    public void update() {
        ItemStack stackInSlot = inventory.getStackInSlot(0);
        int stackSize = stackInSlot.getCount();
        if (stackSize < 64) {
            int spaceLeft;
            if (FRCobbleGen.Cobble_RF > 0)
                spaceLeft = Math.min(64 - stackSize, battery.getEnergyStored() / FRCobbleGen.Cobble_RF);
            else
                spaceLeft = 64 - stackSize;
            ItemHandlerHelper.insertItem(inventory, new ItemStack(Blocks.COBBLESTONE, spaceLeft), false);
            battery.extractEnergy(spaceLeft * FRCobbleGen.Cobble_RF, false);
        }
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

    /**
     * Gets the Tile's current battery.
     *
     * @return The Tile's current battery.
     */
    @Override
    public BatteryST getBattery() {
        return battery;
    }

    /**
     * Creates a new Battery for the Tile.
     *
     * @return a new Battery for the Tile.
     */
    @Override
    public BatteryST createBattery() {
        return new BatteryST(1000);
    }

    /**
     * Gets the Tile's current Inventory.
     *
     * @return The Tile's current Inventory.
     */
    @Override
    public ItemStackHandler getInventory() {
        return inventory;
    }

    /**
     * Creates a new Inventory for the Tile.
     *
     * @return a new Inventory for the Tile.
     */
    @Override
    public ItemStackHandler createInventory() {
        return new ItemStackHandler(1);
    }

    /**
     * Sets the given ItemStackHandler to be the Tile's Inventory.
     *
     * @param inventory new Inventory.
     */
    @Override
    public void setInventory(ItemStackHandler inventory) {
        this.inventory = inventory;
    }
}
