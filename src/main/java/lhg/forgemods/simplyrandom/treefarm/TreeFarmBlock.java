package lhg.forgemods.simplyrandom.treefarm;

import lhg.forgemods.simplyrandom.core.SRBlock;
import lhg.forgemods.simplyrandom.core.Tooltip;
import lhg.forgemods.simplyrandom.core.Tooltip.Builder;
import net.minecraft.block.BlockState;
import net.minecraft.block.SaplingBlock;
import net.minecraft.block.material.Material;
import net.minecraft.item.DirectionalPlaceContext;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.BlockTags;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

/**
 * This is the Tree Farm block
 */
public class TreeFarmBlock extends SRBlock
{
    public TreeFarmBlock()
    {
        super(Properties.create(Material.IRON).hardnessAndResistance(5F, 6F));
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
        return new TreeFarmTileEntity(TreeFarm.TILE_TYPE);
    }

    @Override
    protected Tooltip getTooltip()
    {
        return new Builder().createTooltip();
    }

    @Override
    public boolean canSustainPlant(BlockState state, IBlockReader world, BlockPos pos, Direction facing, net.minecraftforge.common.IPlantable plantable)
    {
        if (facing == Direction.UP)
        {
            final BlockPos up = pos.up();
            BlockState plant = plantable.getPlant(world, up);
            final BlockState aboveState = world.getBlockState(up);
            // Do this to prevent the vanilla Huge Trees from overriding this block when they grow
            if (aboveState.isReplaceable(new DirectionalPlaceContext((World) world, up, Direction.DOWN, ItemStack.EMPTY, Direction.DOWN)))
            { // This is a thorough check which should catch all saplings
                return BlockTags.SAPLINGS.contains(plant.getBlock()) || plant.getBlock() instanceof SaplingBlock;
            } else
            { // This prevents an above sapling uprooting when it is updated
                return BlockTags.SAPLINGS.contains(aboveState.getBlock());
            }

        }
        return false;
    }
}
