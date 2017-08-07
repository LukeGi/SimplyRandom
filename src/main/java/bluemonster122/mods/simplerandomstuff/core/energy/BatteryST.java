package bluemonster122.mods.simplerandomstuff.core.energy;

import cofh.api.energy.IEnergyStorage;
import net.minecraftforge.energy.EnergyStorage;

public class BatteryST
  extends EnergyStorage
  implements IEnergyStorage
{
  public BatteryST(int capacity)
  {
    super(capacity);
  }
  
  public BatteryST(int capacity, int maxTransfer)
  {
    super(capacity, maxTransfer);
  }
  
  public BatteryST(int capacity, int maxReceive, int maxExtract)
  {
    super(capacity, maxReceive, maxExtract);
  }
  
  public void setEnergy(int power)
  {
    this.energy = power;
  }
}
