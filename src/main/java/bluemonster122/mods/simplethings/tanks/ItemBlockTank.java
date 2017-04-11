package bluemonster122.mods.simplethings.tanks;

import bluemonster122.mods.simplethings.core.block.ItemBlockEnum;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import java.util.List;

public class ItemBlockTank extends ItemBlockEnum {
    public ItemBlockTank(BlockTank block) {
        super(block);
        setMaxStackSize(1);
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
        NBTTagCompound tagCompound = stack.getTagCompound();
        if (tagCompound != null && tagCompound.hasKey("FluidName") && tagCompound.hasKey("Amount")) {
            tooltip.add(String.format("Contents: %s", tagCompound.getString("FluidName")));
            tooltip.add(String.format("Amount: %s/%s", tagCompound.getInteger("Amount"), (8 << stack.getMetadata()) * 1000));
        }
    }
}
