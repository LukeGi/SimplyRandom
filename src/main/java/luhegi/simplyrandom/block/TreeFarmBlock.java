package luhegi.simplyrandom.block;

import luhegi.simplyrandom.tile.TreeFarmTile;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tags.BlockTags;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;

import javax.annotation.Nullable;

public class TreeFarmBlock extends Block {
    public TreeFarmBlock(Properties properties) {
        super(properties);
    }

    @Override
    public boolean canSustainPlant(BlockState state, IBlockReader world, BlockPos pos, Direction facing, IPlantable plantable) {
        BlockPos up = pos.up();
        BlockState above = world.getBlockState(up);
        BlockState plant = plantable.getPlant(world, up);
        if (BlockTags.SAPLINGS.contains(plant.getBlock())) {
            return world.getBlockState(up).isAir(world, up) || BlockTags.SAPLINGS.contains(above.getBlock());
        }
        return super.canSustainPlant(state, world, pos, facing, plantable);
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new TreeFarmTile();
    }
}
