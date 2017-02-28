package bluemonster122.mods.simplethings.tileentity;

import bluemonster122.mods.simplethings.tileentity.core.TileEntityGenerator;
import net.minecraft.init.Blocks;

public class TileGeneratorFire extends TileEntityGenerator {
    private EnergyStorageGenerator battery = new EnergyStorageGenerator(1000);

    @Override
    protected int getTickTimer() {
        return 1;
    }

    @Override
    protected boolean generateCondition() {
        return getWorld().getBlockState(getPos().up(2)).getBlock().equals(Blocks.FIRE);
    }

    @Override
    protected void generatePower() {
        getBatteryGenerator().generate(1);
    }

    @Override
    protected EnergyStorageGenerator getBatteryGenerator() {
        return battery;
    }
}
