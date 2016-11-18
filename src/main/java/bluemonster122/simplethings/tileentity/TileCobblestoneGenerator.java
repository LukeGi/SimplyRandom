package bluemonster122.simplethings.tileentity;

import bluemonster122.simplethings.util.EnergyContainer;
import bluemonster122.simplethings.handler.ConfigurationHandler;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;

public class TileCobblestoneGenerator extends TileEnergy implements ITickable
{
    public ItemStackHandler inventory = new ItemStackHandler(1);

    @Override
    public EnergyContainer makeNewBattery()
    {
        return new EnergyContainer(1000, 10, 0);
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing)
    {
        if (capability.equals(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY))
        {
            return true;
        } else if (ConfigurationHandler.cobblestone_generator_req_power > 0)
        {
            return super.hasCapability(capability, facing);
        } else
        {
            return false;
        }
    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing)
    {
        if (capability.equals(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY))
        {
            return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(inventory);
        } else if (ConfigurationHandler.cobblestone_generator_req_power > 0)
        {
            return super.getCapability(capability, facing);
        } else
        {
            return null;
        }
    }

    @Override
    public void update()
    {
        generateCobblestone();
    }

    private void generateCobblestone()
    {
        ItemStack stackInSlot = inventory.getStackInSlot(0);
        int stackSize = stackInSlot.func_190916_E();
        if (stackSize < 64) {
            int spaceLeft = 64 - stackSize;
            while (ConfigurationHandler.cobblestone_generator_req_power * spaceLeft > getEnergyStored()) {
                spaceLeft--;
            }
            ItemHandlerHelper.insertItem(inventory, new ItemStack(Blocks.COBBLESTONE, spaceLeft), false);
            extractEnergy(spaceLeft, false);
        }
    }
}
