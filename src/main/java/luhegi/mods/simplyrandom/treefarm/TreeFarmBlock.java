package luhegi.mods.simplyrandom.treefarm;

import luhegi.mods.simplyrandom.basis.objects.BasisBlock;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nullable;

public class TreeFarmBlock extends BasisBlock {
    public TreeFarmBlock(Properties properties) {
        super(properties);
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
