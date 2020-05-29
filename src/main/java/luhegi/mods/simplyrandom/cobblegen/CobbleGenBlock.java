package luhegi.mods.simplyrandom.cobblegen;

import luhegi.mods.simplyrandom.basis.objects.BasisBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nullable;

public class CobbleGenBlock extends BasisBlock {
    public CobbleGenBlock(Block.Properties properties) {
        super(properties);
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new CobbleGenTile();
    }
}
