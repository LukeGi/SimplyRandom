package bluemonster122.mods.simplerandomstuff.tanks;

import bluemonster122.mods.simplerandomstuff.core.block.ItemBlockEnum;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

public class ItemBlockTank
  extends ItemBlockEnum
{
  public ItemBlockTank(BlockTank block)
  {
    super(block);
    setMaxStackSize(1);
  }
  
  @SideOnly(Side.CLIENT)
  @Override
  public void addInformation(
    ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn
  )
  {
    NBTTagCompound tagCompound = stack.getTagCompound();
    if (tagCompound != null && tagCompound.hasKey("FluidName") && tagCompound.hasKey("Amount"))
    {
      tooltip.add(String.format("Contents: %s", tagCompound.getString("FluidName")));
      tooltip.add(String.format("Amount: %s/%s", tagCompound.getInteger("Amount"), (8 << stack.getMetadata()) * 1000));
    }
  }
}
