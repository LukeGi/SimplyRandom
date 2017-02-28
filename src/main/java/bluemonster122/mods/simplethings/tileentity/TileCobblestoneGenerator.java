package bluemonster122.mods.simplethings.tileentity;

import bluemonster122.mods.simplethings.handler.ConfigurationHandler;
import bluemonster122.mods.simplethings.tileentity.core.IMachine;
import bluemonster122.mods.simplethings.tileentity.core.TileEntityST;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ITickable;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;

public class TileCobblestoneGenerator extends TileEntityST implements ITickable, IMachine {
    /**
     * Inventory
     */
    private ItemStackHandler inventory = new ItemStackHandler(1);
    /**
     * Battery
     */
    private EnergyStorage battery = new EnergyStorage(1000);

    @Override
    public void update() {
        ItemStack stackInSlot = getInventory().getStackInSlot(0);
        int stackSize = stackInSlot.getCount();
        if (stackSize < 64) {
            int spaceLeft = 64 - stackSize;
            while (ConfigurationHandler.cobblestone_generator_req_power * spaceLeft > getBattery().getEnergyStored()) {
                spaceLeft--;
            }
            ItemHandlerHelper.insertItem(getInventory(), new ItemStack(Blocks.COBBLESTONE, spaceLeft), false);
            extractPower(spaceLeft * ConfigurationHandler.cobblestone_generator_req_power, false);
        }
    }

    @Override
    public ItemStackHandler getInventory() {
        return inventory;
    }

    @Override
    public EnergyStorage getBattery() {
        return battery;
    }
}
