package lhg.forgemods.simplyrandom.cobblemaker;

import com.google.common.collect.ImmutableSet;
import lhg.forgemods.simplyrandom.core.SRBlock;
import lhg.forgemods.simplyrandom.core.Tooltip;
import lhg.forgemods.simplyrandom.core.Tooltip.Builder;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.common.ToolType;

import javax.annotation.Nullable;

/**
 * Cobblestone Maker Block
 */
public class CobblestoneMakerBlock extends SRBlock
{
    private static Tooltip tooltip;

    public CobblestoneMakerBlock()
    {
        super(Properties.create(Material.ROCK).hardnessAndResistance(2F, 6F));
    }

    @Override
    public boolean hasTileEntity(BlockState state)
    {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world)
    {
        return new CobblestoneMakerTileEntity(CobblestoneMaker.TILE_TYPE);
    }

    @Override
    protected Tooltip getTooltip()
    {
        if (tooltip == null)
        {
            tooltip = new Builder()
                    .setBasicLines(ImmutableSet.of(new TranslationTextComponent("simplyrandom.tooltip.cobblemaker")))
                    .createTooltip();
        }
        return tooltip;
    }

    @Nullable
    @Override
    public ToolType getHarvestTool(BlockState state)
    {
        return ToolType.PICKAXE;
    }

    @Override
    public int getHarvestLevel(BlockState state)
    {
        return 1;
    }
}
