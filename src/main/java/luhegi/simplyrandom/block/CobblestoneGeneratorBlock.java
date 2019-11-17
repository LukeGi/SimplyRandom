package luhegi.simplyrandom.block;

import luhegi.simplyrandom.tile.CobblestoneGeneratorTile;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Random;

public class CobblestoneGeneratorBlock extends Block {
    public CobblestoneGeneratorBlock(Properties properties) {
        super(properties);
    }

    @Override
    public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand) {
        if (worldIn.isAirBlock(pos.down())) {
            double d0 = (double) pos.getX() + 0.5D;
            double d1 = pos.getY();
            double d2 = (double) pos.getZ() + 0.5D;

            for (int i = 0; i < 4; i++) {
                if (rand.nextBoolean())
                    worldIn.addParticle(ParticleTypes.DRIPPING_LAVA, d0 + (rand.nextDouble() - 0.5D), d1, d2 + (rand.nextDouble() - 0.5D), 0.0D, 0.0D, 0.0D);
            }
            for (int i = 0; i < 4; i++) {
                if (rand.nextBoolean())
                    worldIn.addParticle(ParticleTypes.DRIPPING_WATER, d0 + (rand.nextDouble() - 0.5D), d1, d2 + (rand.nextDouble() - 0.5D), 0.0D, 0.0D, 0.0D);
            }
        }
        super.animateTick(stateIn, worldIn, pos, rand);
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new CobblestoneGeneratorTile();
    }
}
