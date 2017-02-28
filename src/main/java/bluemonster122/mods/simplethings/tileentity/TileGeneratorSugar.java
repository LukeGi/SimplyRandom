package bluemonster122.mods.simplethings.tileentity;

import bluemonster122.mods.simplethings.handler.ConfigurationHandler;
import bluemonster122.mods.simplethings.tileentity.core.TileEntityGenerator;
import net.minecraft.nbt.NBTTagCompound;

public class TileGeneratorSugar extends TileEntityGenerator {
    private EnergyStorageGenerator battery = new EnergyStorageGenerator(10000);
    private int burntime = 0;

    public void addToBurnTime(int time) {
        burntime += time;
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound.setInteger("burntime", burntime);
        return super.writeToNBT(compound);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        burntime = compound.getInteger("burntime");
        super.readFromNBT(compound);
    }

    @Override
    protected int getTickTimer() {
        return 1;
    }

    @Override
    protected boolean generateCondition() {
        return burntime > 0;
    }

    @Override
    protected void generatePower() {
        burntime--;
        getBatteryGenerator().generate(ConfigurationHandler.energy_from_sugar);
    }

    @Override
    protected EnergyStorageGenerator getBatteryGenerator() {
        return battery;
    }
}