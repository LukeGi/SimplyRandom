package luhegi.mods.simplyrandom.basis.tile;

import luhegi.mods.simplyrandom.SimplyRandom;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class BasisItemHandler extends ItemStackHandler implements ICap<IItemHandler> {
    public static final String NBT_KEY_INVENTORY = SimplyRandom.ID + ":inventory";
    private final LazyOptional<IItemHandler> capability = LazyOptional.of(() -> this);
    private List<SlotFilter> filters = new ArrayList<>();

    public BasisItemHandler() {
    }

    public BasisItemHandler(int size) {
        super(size);
    }

    public BasisItemHandler(NonNullList<ItemStack> stacks) {
        super(stacks);
    }

    @Override
    public IItemHandler getInstance() {
        return this;
    }

    @Override
    public LazyOptional<IItemHandler> getCapability() {
        return capability;
    }

    @Override
    public Capability<IItemHandler> getType() {
        return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY;
    }

    @Override
    public void save(CompoundNBT nbt) {
        nbt.put(NBT_KEY_INVENTORY, serializeNBT());
    }

    @Override
    public void load(CompoundNBT nbt) {
        deserializeNBT(nbt.getCompound(NBT_KEY_INVENTORY));
    }

    @Override
    public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
        return filters.stream().filter(slotFilter -> slotFilter.slot == slot)
                .allMatch(slotFilter -> slotFilter.validityTest.test(stack))
                && super.isItemValid(slot, stack);
    }

    public BasisItemHandler withSlotCheck(SlotFilter filter) {
        filters.add(filter);
        return this;
    }

    public static class SlotFilter {
        private final int slot;
        private final Predicate<ItemStack> validityTest;

        public SlotFilter(int slot, Predicate<ItemStack> validityTest) {
            this.slot = slot;
            this.validityTest = validityTest;
        }


    }
}
