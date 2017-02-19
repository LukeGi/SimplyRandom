package bluemonster122.simplethings.tileentity;

import bluemonster122.simplethings.handler.ConfigurationHandler;
import bluemonster122.simplethings.tileentity.things.IMachine;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ITickable;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;

public class TileCobblestoneGenerator extends TileEntityST implements ITickable, IMachine
{
    /**
     * Inventory
     */
    private ItemStackHandler inventory = new ItemStackHandler(1);
    /**
     * Battery
     */
    private EnergyStorage battery = new EnergyStorage(1000);

    @Override
    public void update()
    {
        ItemStack stackInSlot = getInventory().getStackInSlot(0);
        int stackSize = stackInSlot.func_190916_E();
        if (stackSize < 64)
        {
            int spaceLeft = 64 - stackSize;
            while (ConfigurationHandler.cobblestone_generator_req_power * spaceLeft > getBattery().getEnergyStored())
            {
                spaceLeft--;
            }
            ItemHandlerHelper.insertItem(getInventory(), new ItemStack(Blocks.COBBLESTONE, spaceLeft), false);
            extractPower(spaceLeft, false);
        }
    }

    @Override
    public ItemStackHandler getInventory()
    {
        return inventory;
    }

    @Override
    public EnergyStorage getBattery()
    {
        return battery;
    }
}
