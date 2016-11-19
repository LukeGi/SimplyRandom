package bluemonster122.simplethings.tileentity;

import bluemonster122.simplethings.util.EnergyContainerGenerator;
import net.minecraft.init.Blocks;
import net.minecraft.util.ITickable;

public class TileEnergyGeneratorFire extends TileEnergyGenerator implements ITickable
{
    @Override
    public EnergyContainerGenerator makeNewBattery()
    {
        return new EnergyContainerGenerator(1000, 100);
    }

    @Override
    public void update()
    {
        if (worldObj.getBlockState(pos.up(2)).getBlock().equals(Blocks.FIRE))
        {
            getBattery().generate(1);
        }
        if (this.getEnergyStored() > 0)
        {
            super.checkForTakers();
        }
    }
}
