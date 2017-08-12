package bluemonster.simplerandomstuff.generators;

import bluemonster.simplerandomstuff.core.energy.BatteryST;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ITickable;

public class TileGeneratorSugar
        extends TileGenerator
        implements ITickable {
    private int burntime = 0;

    public void addBurnTime(int burntime) {
        this.burntime += burntime;
    }

    /**
     * Creates a new Battery for the Tile.
     *
     * @return a new Battery for the Tile.
     */
    @Override
    public BatteryST createBattery() {
        return new BatteryST(1000);
    }

    @Override
    public NBTTagCompound writeChild(NBTTagCompound tag) {
        tag.setInteger("burntime", burntime);
        return tag;
    }

    @Override
    public NBTTagCompound readChild(NBTTagCompound tag) {
        burntime = tag.getInteger("burntime");
        return tag;
    }

    /**
     * Like the old updateEntity(), except more generic.
     */
    @Override
    public void update() {
        super.update();
        if (!getWorld().isRemote) {
            if (burntime > 0) {
                if (battery.receiveEnergy(FRGenerators.Sugar_RF, true) == FRGenerators.Sugar_RF) {
                    battery.receiveEnergy(FRGenerators.Sugar_RF, false);
                    burntime--;
                }
            }
            sendUpdate();
        }
    }
}