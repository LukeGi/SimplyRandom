package bluemonster122.mods.simplerandomstuff.tanks;

import bluemonster122.mods.simplerandomstuff.core.block.IHaveTank;
import bluemonster122.mods.simplerandomstuff.core.block.TileST;
import com.google.common.collect.ImmutableMap;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

import java.util.Map;

public class TileTank
  extends TileST
  implements ITickable, IHaveTank
{
  public int tier       = 0;
  
  public FluidTank tank = createTank();
  
  /**
   * Gets the Tile's current Tank.
   *
   * @return The Tile's current Tank.
   */
  @Override
  public FluidTank getTank()
  {
    return tank;
  }
  
  /**
   * Sets the given ItemStackHandler to be the Tile's Tank.
   *
   * @param tank
   *   new Inventory.
   */
  @Override
  public void setTank(FluidTank tank)
  {
    this.tank = tank;
  }
  
  /**
   * Creates a new Tank for the Tile.
   *
   * @return a new Tank for the Tile.
   */
  @Override
  public FluidTank createTank()
  {
    try
    {
      tier = (byte) world.getBlockState(pos)
                         .getValue(BlockTank.VARIANT)
                         .getMeta();
    }
    catch (Exception ignore)
    {
    }
    return new FluidTank((8 << tier) * 1000)
    {
      @Override
      protected void onContentsChanged()
      {
        IBlockState state = getWorld().getBlockState(getPos());
        getWorld().notifyBlockUpdate(getPos(), state, state, 3);
        markDirty();
        super.onContentsChanged();
      }
    };
  }
  
  public boolean attemptPushDown()
  {
    TileEntity tile = getWorld().getTileEntity(getPos().down());
    if (tile == null) return false;
    if (tile.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, EnumFacing.UP))
    {
      IFluidHandler handler = tile.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, EnumFacing.UP);
      if (handler == null) return false;
      int fillAmount = handler.fill(tank.drain(tank.getFluidAmount(), false), true);
      if (fillAmount > 0)
      {
        tank.drain(fillAmount, true);
        IBlockState state = getWorld().getBlockState(getPos());
        getWorld().notifyBlockUpdate(getPos(), state, state, 3);
        markDirty();
        return true;
      }
    }
    return false;
  }
  
  @Override
  public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate)
  {
    return oldState.getBlock() != newSate.getBlock();
  }
  
  @Override
  public Map<Capability, Capability> getCaps()
  {
    return ImmutableMap.of(
      CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY,
      CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast((IFluidHandler) tank)
    );
  }
  
  @Override
  public NBTTagCompound writeChild(NBTTagCompound tag)
  {
    return tag;
  }
  
  @Override
  public NBTTagCompound readChild(NBTTagCompound tag)
  {
    return tag;
  }
  
  @Override
  public void update()
  {
    if (attemptPushDown())
    {
    
    }
  }
}
