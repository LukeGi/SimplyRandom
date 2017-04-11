package bluemonster122.mods.simplethings.cobblegen;

import bluemonster122.mods.simplethings.handler.ConfigurationHandler;
import bluemonster122.mods.simplethings.tileentity.core.TileEntityST;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;

import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class TileCobblestoneGenerator extends TileEntityST implements ITickable {
    /**
     * Inventory
     */
    private ItemStackHandler inventory = new ItemStackHandler(1);
    /**
     * Battery
     */
    private EnergyStorage battery = createBattery();

    @Override
    public Set<Consumer<NBTTagCompound>> getAllWrites() {
        return getMinWrites();
    }

    @Override
    public Set<Consumer<NBTTagCompound>> getAllReads() {
        return getMinReads();
    }

    @Override
    public Map<Capability, Supplier<Capability>> getCaps() {
        return ImmutableMap.of(
                CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(inventory),
                CapabilityEnergy.ENERGY, CapabilityEnergy.ENERGY.cast(battery)
        );
    }

    @Override
    public Set<Consumer<NBTTagCompound>> getMinWrites() {
        return ImmutableSet.of(super::writeNBTLegacy, this::writeInventory, this::writeEnergy);
    }

    @Override
    public Set<Consumer<NBTTagCompound>> getMinReads() {
        return ImmutableSet.of(super::readNBTLegacy, this::readInventory, this::readEnergy);
    }

    public NBTTagCompound writeInventory(NBTTagCompound tag) {
        NBTTagCompound nbtTagCompound = inventory.serializeNBT();
        tag.setTag("inventory", nbtTagCompound);
        return tag;
    }

    public NBTTagCompound readInventory(NBTTagCompound tag) {
        NBTTagCompound nbtTagCompound = tag.getCompoundTag("inventory");
        inventory.deserializeNBT(nbtTagCompound);
        return tag;
    }

    public NBTTagCompound writeEnergy(NBTTagCompound tag) {
        tag.setInteger("storedEnergy", battery.getEnergyStored());
        return tag;
    }

    public NBTTagCompound readEnergy(NBTTagCompound tag) {
        battery = createBattery();
        battery.receiveEnergy(tag.getInteger("storedEnergy"), false);
        return tag;
    }

    private EnergyStorage createBattery() {
        return new EnergyStorage(1000);
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
            if (ConfigurationHandler.cobblestone_generator_req_power > 0)
                spaceLeft = Math.min(64 - stackSize, battery.getEnergyStored() / ConfigurationHandler.cobblestone_generator_req_power);
            else
                spaceLeft = 64 - stackSize;
            ItemHandlerHelper.insertItem(inventory, new ItemStack(Blocks.COBBLESTONE, spaceLeft), false);
            battery.extractEnergy(spaceLeft * ConfigurationHandler.cobblestone_generator_req_power, false);
        }
    }
}
