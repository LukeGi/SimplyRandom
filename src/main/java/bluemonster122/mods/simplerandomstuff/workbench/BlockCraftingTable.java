package bluemonster122.mods.simplerandomstuff.workbench;

import bluemonster122.mods.simplerandomstuff.SRS;
import bluemonster122.mods.simplerandomstuff.core.block.BlockSRS;
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

public class BlockCraftingTable
  extends BlockSRS
  implements ITileEntityProvider
{
  public BlockCraftingTable()
  {
    this("crafting_table");
  }
  
  public BlockCraftingTable(String name)
  {
    super(name, Material.WOOD);
    setHardness(2.5F);
  }
  
  @Override
  public boolean onBlockActivated(
    World worldIn,
    BlockPos pos,
    IBlockState state,
    EntityPlayer playerIn,
    EnumHand hand,
    EnumFacing facing,
    float hitX,
    float hitY,
    float hitZ
  )
  {
    if (!worldIn.isRemote && !playerIn.isSneaking())
    {
      playerIn.openGui(SRS.INSTANCE, 0, worldIn, pos.getX(), pos.getY(), pos.getZ());
    }
    return true;
  }
  
  @Nullable
  @Override
  public TileEntity createNewTileEntity(World worldIn, int meta)
  {
    return new TileCraftingTable();
  }
  
  public static class BlockCraftingTableAuto
    extends BlockCraftingTable
  {
    public BlockCraftingTableAuto()
    {
      super("crafting_table_auto");
    }
    
    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta)
    {
      return new TileCraftingTable.TileCraftingTableAuto();
    }
  }
}
