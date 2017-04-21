package bluemonster122.mods.simplethings.generators;

import bluemonster122.mods.simplethings.core.energy.BatteryST;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ITickable;

public class TileGeneratorFire extends TileGenerator implements ITickable {

    /**
     * Creates a new Battery for the Tile.
     *
     * @return a new Battery for the Tile.
     */
    @Override
    public BatteryST createBattery( ) {
        return new BatteryST(1000);
    }

    @Override
    public NBTTagCompound writeChild(NBTTagCompound tag) {
        return tag;
    }

    @Override
    public NBTTagCompound readChild(NBTTagCompound tag) {
        return tag;
    }

    /**
     * Like the old updateEntity(), except more generic.
     */
    @Override
    public void update( ) {
        if (!getWorld().isRemote) {
            if (getWorld().getBlockState(getPos().up(2)).getBlock().equals(Blocks.FIRE)) {
                if (battery.receiveEnergy(FRGenerators.Fire_RF, true) > 0) {
                    battery.receiveEnergy(FRGenerators.Fire_RF, false);
                }
            }
        }
    }
}
