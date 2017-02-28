package bluemonster122.mods.simplethings.tileentity;

import bluemonster122.mods.simplethings.tileentity.core.IAcceptPower;
import bluemonster122.mods.simplethings.tileentity.core.IProvidePower;
import bluemonster122.mods.simplethings.tileentity.core.TileEntityST;
import net.minecraft.util.ITickable;
import net.minecraftforge.energy.EnergyStorage;

public class TilePowerStorage extends TileEntityST implements ITickable, IAcceptPower, IProvidePower {
    private EnergyStorage battery = new EnergyStorage(1000000);

    @Override
    public void update() {
        this.attemptDischarge();
    }

    @Override
    public EnergyStorage getBattery() {
        return battery;
    }
}
