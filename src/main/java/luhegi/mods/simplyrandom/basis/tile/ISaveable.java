package luhegi.mods.simplyrandom.basis.tile;

import net.minecraft.nbt.CompoundNBT;

public interface ISaveable {
    void save(CompoundNBT nbt);

    void load(CompoundNBT nbt);
}
