package bluemonster122.simplethings.tileentity;

import bluemonster122.simplethings.util.EnergyContainerGenerator;
import net.minecraft.init.Items;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import static bluemonster122.simplethings.handler.ConfigurationHandler.energy_from_sugar;
import static bluemonster122.simplethings.handler.ConfigurationHandler.sugar_burn_time;

public class TileEnergyGeneratorSugar extends TileEnergyGenerator implements ITickable
{
    private int burntime;
    private ItemStackHandler inventory = new ItemStackHandler(1);

    @Override
    public EnergyContainerGenerator makeNewBattery()
    {
        return new EnergyContainerGenerator(10000, 100);
    }

    @Override
    public void update()
    {
        // If we have nothing left to burn
        if (burntime == 0)
        {
            // If we have enough spare room in the generator's buffer to burn a sugar without waste
            if (getEnergyStored() + energy_from_sugar <= getMaxEnergyStored())
            {
                // If the inventory contains a stack of sugar.
                if (inventory.getStackInSlot(0).getItem().equals(Items.SUGAR))
                {
                    // We take a single sugar from the stack.
                    inventory.extractItem(0, 1, false);
                    // Adds to the burntime
                    burntime = sugar_burn_time;
                }
            }
        } // or if we are still burning sugar
        else if (burntime > 0)
        {
            // burn some sugar
            burntime--;
            // add the energy created to the battery
            getBattery().generate(energy_from_sugar / sugar_burn_time);
        } // or if for some reason the burnime is negative
        else
        {
            // set the burntime to be 0, for the sugar checks.
            burntime = 0;
        }

        if (getEnergyStored() > 0) {
            super.checkForTakers();
        }
        // TODO: 11/19/2016 make it require an air block on 3 sides, and 15 air blocks in a 2 block radius.
        // TODO: 11/19/2016 make it emit a lot of smoke particles if not enough air
        // TODO: 11/19/2016 make it emit flames, ender and smoke particles
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing)
    {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
        {
            return true;
        } else
        {
            return super.hasCapability(capability, facing);
        }
    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing)
    {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
        {
            return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(inventory);
        } else
        {
            return super.getCapability(capability, facing);
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        compound.setInteger("[burntime]", burntime);
        compound.setTag("[inventory]", inventory.serializeNBT());
        return super.writeToNBT(compound);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        burntime = compound.getInteger("[burntime]");
        inventory.deserializeNBT((NBTTagCompound) compound.getTag("[inventory]"));
        super.readFromNBT(compound);
    }
}
