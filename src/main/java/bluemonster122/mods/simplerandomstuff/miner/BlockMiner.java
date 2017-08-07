package bluemonster122.mods.simplerandomstuff.miner;

import bluemonster122.mods.simplerandomstuff.SRS;
import bluemonster122.mods.simplerandomstuff.core.block.BlockSRS;
import bluemonster122.mods.simplerandomstuff.handler.GuiHandler;
import bluemonster122.mods.simplerandomstuff.reference.Names;
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

public class BlockMiner
  extends BlockSRS
  implements ITileEntityProvider
{
  public BlockMiner()
  {
    super(Names.Blocks.MINER, Material.IRON);
  }
  
  @Override
  public boolean onBlockActivated(
    World worldIn,
    BlockPos pos,
    IBlockState state,
    EntityPlayer playerIn,
    EnumHand hand,
    EnumFacing heldItem,
    float side,
    float hitX,
    float hitY
  )
  {
    playerIn.openGui(SRS.INSTANCE, GuiHandler.ID, worldIn, pos.getX(), pos.getY(), pos.getZ());
    return true;
  }
  
  @Nullable
  @Override
  public TileEntity createNewTileEntity(World worldIn, int meta)
  {
    return new TileMiner();
  }
}
