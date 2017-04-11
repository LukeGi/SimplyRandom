package bluemonster122.mods.simplethings.block;

import bluemonster122.mods.simplethings.SimpleThings;
import bluemonster122.mods.simplethings.tileentity.TileTest;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockTest extends BlockST implements ITileEntityProvider {
    public BlockTest() {
        super("test_block", Material.CLOTH);
        setHardness(1);
        setResistance(1000);
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        if (hasTileEntity(state)) {
            TileEntity tileEntity = worldIn.getTileEntity(pos);
            if (tileEntity != null && tileEntity instanceof TileTest) {
                TileTest testTile = (TileTest) tileEntity;
                if (testTile.getWorld().isRemote) {
                    //noinspection MethodCallSideOnly
                    testTile.cleanUp();
                }
            }
            worldIn.removeTileEntity(pos);
        }
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (worldIn.isRemote) {
            try {
                //noinspection MethodCallSideOnly
                ((TileTest) worldIn.getTileEntity(pos)).genBoxRender();
            } catch (Exception e) {
                SimpleThings.logger.info("Error adding box to be drawn");
                return false;
            }
        }
        return true;
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileTest();
    }
}
