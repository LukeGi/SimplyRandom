package bluemonster122.simplethings.block;

import bluemonster122.simplethings.reference.Names;
import bluemonster122.simplethings.tileentity.TileLightningRod;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;
@SuppressWarnings("deprecation")
public class BlockLightningRod extends SimpleBlockBase implements ITileEntityProvider
{
	protected static final AxisAlignedBB AABB = new AxisAlignedBB(0.375D, 0.0D, 0.375D, 0.625D, 1.0D, 0.625D);
	
	public BlockLightningRod()
	{
		super(Material.CIRCUITS, Names.LIGHTNING_ROD);
	}
	
	@Override
	public boolean isFullCube(IBlockState state)
	{
		return false;
	}
	
	@Override
	public boolean isOpaqueCube(IBlockState state)
	{
		return false;
	}
	
	@Override
	public boolean isFullBlock(IBlockState state)
	{
		return false;
	}
	
	@Override
	public boolean isFullyOpaque(IBlockState state)
	{
		return false;
	}
	
	@Override
	public boolean isNormalCube(IBlockState state, IBlockAccess world, BlockPos pos)
	{
		return false;
	}
	
	@Nullable
	@Override
	public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos)
	{
		return AABB;
	}
	
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
	{
		return AABB;
	}
	
	@Nullable
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta)
	{
		return new TileLightningRod();
	}
}
