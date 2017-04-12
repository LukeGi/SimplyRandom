package bluemonster122.mods.simplethings.pump;

import bluemonster122.mods.simplethings.SimpleThings;
import bluemonster122.mods.simplethings.core.block.BlockST;
import bluemonster122.mods.simplethings.core.block.IPickup;
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

public class BlockPump extends BlockST implements IPickup, ITileEntityProvider {
    public BlockPump() {
        super("pump", Material.IRON);
        setCreativeTab(SimpleThings.theTab);
        setHardness(5000f);
        setResistance(1f);
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (pickup(worldIn, pos, state, playerIn, hand)) {
            return true;
        }
        return false;
    }

    /**
     * Returns a new instance of a block's tile entity class. Called on placing the block.
     *
     * @param worldIn
     * @param meta
     */
    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TilePump();
    }
}
