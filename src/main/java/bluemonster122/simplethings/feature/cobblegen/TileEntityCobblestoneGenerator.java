package bluemonster122.simplethings.feature.cobblegen;

import bluemonster122.simplethings.energy.EnergyContainer;
import bluemonster122.simplethings.energy.TileEntityEnergy;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class TileEntityCobblestoneGenerator extends TileEntityEnergy implements ITickable
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
        } else if (FeatureCobblestoneGenerator.COBBLE_GEN_REQ_POWER)
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
            return (T) inventory;
        } else if (FeatureCobblestoneGenerator.COBBLE_GEN_REQ_POWER)
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
        if (!FeatureCobblestoneGenerator.COBBLE_GEN_REQ_POWER)
        {
            ItemStack stack = inventory.getStackInSlot(0);
            int genAmount = stack == null ? 64 : 64 - stack.stackSize;
            if (genAmount > 0)
            {
                inventory.insertItem(0, new ItemStack(Blocks.COBBLESTONE, genAmount), false);
            }
        }
    }
}
