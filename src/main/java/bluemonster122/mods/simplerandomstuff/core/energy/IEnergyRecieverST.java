package bluemonster122.mods.simplerandomstuff.core.energy;

import cofh.api.energy.IEnergyReceiver;
import net.minecraft.util.EnumFacing;

public interface IEnergyRecieverST
  extends IEnergyReceiver, IHaveBattery
{
  /**
   * Returns TRUE if the TileEntity can connect on a given side.
   */
  @Override
  default boolean canConnectEnergy(EnumFacing from)
  {
    return true;
  }
  
  /**
   * Returns the amount of energy currently stored.
   */
  @Override
  default int getEnergyStored(EnumFacing from)
  {
    return getBattery().getEnergyStored();
  }
  
  /**
   * Returns the maximum amount of energy that can be stored.
   */
  @Override
  default int getMaxEnergyStored(EnumFacing from)
  {
    return getBattery().getMaxEnergyStored();
  }
  
  /**
   * Add energy to an IEnergyReceiver, internal distribution is left entirely to the IEnergyReceiver.
   *
   * @param from
   *   Orientation the energy is received from.
   * @param maxReceive
   *   Maximum amount of energy to receive.
   * @param simulate
   *   If TRUE, the charge will only be simulated.
   *
   * @return Amount of energy that was (or would have been, if simulated) received.
   */
  @Override
  default int receiveEnergy(EnumFacing from, int maxReceive, boolean simulate)
  {
    return getBattery().receiveEnergy(maxReceive, simulate);
  }
}
