package lhg.forgemods.simplyrandom.core;

import net.minecraft.block.Block;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Base block class for Simply Random. It should provide common helper methods blocks, and potentially provide
 * some additional functionality.
 */
public abstract class SRBlock extends Block
{
    public SRBlock(Properties properties)
    {
        super(properties);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable IBlockReader worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn)
    {
        getTooltip().add(tooltip);
        super.addInformation(stack, worldIn, tooltip, flagIn);
    }

    protected abstract Tooltip getTooltip();
}
