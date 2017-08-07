package bluemonster122.mods.simplerandomstuff.core.block;

import bluemonster122.mods.simplerandomstuff.core.energy.BatteryST;
import bluemonster122.mods.simplerandomstuff.core.energy.IHaveBattery;
import com.google.common.collect.ImmutableMap;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;

public abstract class TileST
  extends TileEntity
{

    /* SAVE TO DISK METHODS */
  
  public NBTTagCompound writeNBTLegacy(NBTTagCompound tag)
  {
    return super.writeToNBT(tag);
  }
  
  public void readNBTLegacy(NBTTagCompound tag)
  {
    super.readFromNBT(tag);
  }
  
  @Override
  public void readFromNBT(NBTTagCompound tag)
  {
    readGeneral(tag);
  }
  
  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound tag)
  {
    writeGeneral(tag);
    return tag;
  }
  
  @Override
  public SPacketUpdateTileEntity getUpdatePacket()
  {
    return new SPacketUpdateTileEntity(getPos(), 0, getUpdateTag());
  }

    /* CAPABILITY METHODS */
  
  @Override
  @Nonnull
  public NBTTagCompound getUpdateTag()
  {
    NBTTagCompound tag = new NBTTagCompound();
    writeGeneral(tag);
    return tag;
  }
  
  @Override
  @SideOnly(Side.CLIENT)
  public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity nbt)
  {
    handleUpdateTag(nbt.getNbtCompound());
  }
  
  @Override
  public void handleUpdateTag(NBTTagCompound tag)
  {
    readGeneral(tag);
  }

    /* CLIENT SYNC METHODS */
  
  @Override
  public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing)
  {
    Map<Capability, Capability> caps = getCaps();
    return caps.keySet()
               .contains(capability) || super.hasCapability(capability, facing);
  }
  
  @SuppressWarnings("unchecked")
  @Nullable
  @Override
  public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing)
  {
    Map<Capability, Capability> caps = getCaps();
    for (ImmutableMap.Entry cap : caps.entrySet())
    {
      if (capability.equals(cap.getKey()))
      {
        return (T) cap.getValue();
      }
    }
    return super.getCapability(capability, facing);
  }
  
  public abstract Map<Capability, Capability> getCaps();
  
  public NBTTagCompound writeInventory(NBTTagCompound tag)
  {
    if (this instanceof IHaveInventory)
    {
      ItemStackHandler inventory      = ((IHaveInventory) this).getInventory();
      NBTTagCompound   nbtTagCompound = inventory.serializeNBT();
      tag.setTag("inventory", nbtTagCompound);
    }
    return tag;
  }
  
  public NBTTagCompound readInventory(NBTTagCompound tag)
  {
    if (this instanceof IHaveInventory)
    {
      ItemStackHandler inventory      = ((IHaveInventory) this).createInventory();
      NBTTagCompound   nbtTagCompound = tag.getCompoundTag("inventory");
      inventory.deserializeNBT(nbtTagCompound);
      ((IHaveInventory) this).setInventory(inventory);
    }
    return tag;
  }
  
  public NBTTagCompound writeEnergy(NBTTagCompound tag)
  {
    if (this instanceof IHaveBattery)
    {
      BatteryST battery = ((IHaveBattery) this).getBattery();
      tag.setInteger("storedEnergy", battery.getEnergyStored());
    }
    return tag;
  }
  
  public NBTTagCompound readEnergy(NBTTagCompound tag)
  {
    if (this instanceof IHaveBattery)
    {
      BatteryST battery = ((IHaveBattery) this).createBattery();
      battery.receiveEnergy(tag.getInteger("storedEnergy"), false);
      ((IHaveBattery) this).setBattery(battery);
    }
    return tag;
  }
  
  public NBTTagCompound writeTank(NBTTagCompound tag)
  {
    if (this instanceof IHaveTank)
    {
      FluidTank tank = ((IHaveTank) this).getTank();
      tank.writeToNBT(tag);
    }
    return tag;
  }
  
  public NBTTagCompound readTank(NBTTagCompound tag)
  {
    if (this instanceof IHaveTank)
    {
      FluidTank tank = ((IHaveTank) this).createTank();
      tank.readFromNBT(tag);
      ((IHaveTank) this).setTank(tank);
    }
    return tag;
  }
  
  public NBTTagCompound writeGeneral(NBTTagCompound tag)
  {
    writeNBTLegacy(tag);
    writeEnergy(tag);
    writeInventory(tag);
    writeTank(tag);
    writeChild(tag);
    readFromNBT(tag);
    return tag;
  }
  
  public NBTTagCompound readGeneral(NBTTagCompound tag)
  {
    readNBTLegacy(tag);
    readEnergy(tag);
    readInventory(tag);
    readTank(tag);
    readChild(tag);
    return tag;
  }
  
  public abstract NBTTagCompound writeChild(NBTTagCompound tag);
  
  public abstract NBTTagCompound readChild(NBTTagCompound tag);
  
  protected void sendUpdate()
  {
    IBlockState state = getWorld().getBlockState(getPos());
    getWorld().notifyBlockUpdate(getPos(), state, state, 3);
    markDirty();
  }
}
