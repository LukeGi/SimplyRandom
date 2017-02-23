package bluemonster122.simplethings.block;

import bluemonster122.simplethings.reference.Names;
import bluemonster122.simplethings.tileentity.TilePowerCable;
import bluemonster122.simplethings.util.ITileEntityProvider1;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import javax.annotation.Nullable;
public class BlockPowerCable extends SimpleBlockBase implements ITileEntityProvider1
{
	public BlockPowerCable()
	{
		super(Material.CIRCUITS, Names.POWER_CABLE);
	}
	
	@Nullable
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta)
	{
		return new TilePowerCable();
	}

	@Override
	public Class<? extends TileEntity> getTileClass()
	{
		return TilePowerCable.class;
	}
}
