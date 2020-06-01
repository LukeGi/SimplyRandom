package luhegi.mods.simplyrandom.basis.tile;

import luhegi.mods.simplyrandom.SimplyRandom;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;

import java.util.function.Predicate;

public class BasisEnergyStorage extends EnergyStorage implements ICap<IEnergyStorage> {
    public static final String NBT_KEY_ENERGY = SimplyRandom.ID + ":energy";
    private final LazyOptional<IEnergyStorage> capability = LazyOptional.of(() -> this);

    public BasisEnergyStorage(int capacity) {
        super(capacity);
    }

    public BasisEnergyStorage(int capacity, int maxTransfer) {
        super(capacity, maxTransfer);
    }

    public BasisEnergyStorage(int capacity, int maxReceive, int maxExtract) {
        super(capacity, maxReceive, maxExtract);
    }

    public BasisEnergyStorage(int capacity, int maxReceive, int maxExtract, int energy) {
        super(capacity, maxReceive, maxExtract, energy);
    }

    @Override
    public IEnergyStorage getInstance() {
        return this;
    }

    @Override
    public LazyOptional<IEnergyStorage> getCapability() {
        return capability;
    }

    @Override
    public Capability<IEnergyStorage> getType() {
        return CapabilityEnergy.ENERGY;
    }

    @Override
    public void save(CompoundNBT nbt) {
        nbt.putInt(NBT_KEY_ENERGY, energy);
    }

    @Override
    public void load(CompoundNBT nbt) {
        energy = nbt.getInt(NBT_KEY_ENERGY);
    }

    public void use(int amount) {
        energy -= amount;
    }

    public boolean isEmpty() {
        return energy <= 0;
    }

    public void add(int amount) {
        energy += amount;
    }
}
