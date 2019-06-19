package lhg.forgemods.simplyrandom.cobblemaker;

import lhg.forgemods.simplyrandom.core.SRTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.EnergyStorage;

import static lhg.forgemods.simplyrandom.core.DisableableFeature.cobblestoneMaker;

/**
 * Cobblestone Maker Tile Entitiy
 */
public class CobblestoneMakerTileEntity extends SRTileEntity<CobblestoneMakerTileEntity>
{
    /**
     * Battery, sometimes is null
     */
    private EnergyStorage battery;
    /**
     * Inventory
     */
    private CobblestoneMakerInventory inventory;

    /**
     * Constructor
     */
    public CobblestoneMakerTileEntity(TileEntityType<CobblestoneMakerTileEntity> type)
    {
        super(type);
        if (cobblestoneMaker.energyPerCobble.get() > 0)
        {
            battery = new EnergyStorage(32000);
        }
        inventory = new CobblestoneMakerInventory(this);
    }

    @Override
    protected void populateCaps()
    {
        if (!energyCapability.isPresent())
        {
            energyCapability = battery == null ? LazyOptional.empty() : LazyOptional.of(() -> battery);
        }
        if (!itemCapability.isPresent())
        {
            itemCapability = inventory == null ? LazyOptional.empty() : LazyOptional.of(() -> inventory);
        }
    }

    /**
     * This will return the amount of cobble that this block can produce right now.
     *
     * @return the amount of cobble this can produce from a slot, whether it is using power or not.
     */
    public int getCobbleCount()
    {
        markDirty();
        if (cobblestoneMaker.energyPerCobble.get() > 0 && battery != null)
        {
            return Math.min(64, battery.getEnergyStored() / Math.min(cobblestoneMaker.energyPerCobble.get(), 1));
        }
        return 64;
    }

    /**
     * This will consume {@code energy} from {@link #battery} if it exists.
     *
     * @param energy The amount of energy to consume
     */
    public void consume(int energy)
    {
        if (cobblestoneMaker.energyPerCobble.get() > 0 && battery != null)
        {
            markDirty();
            battery.extractEnergy(energy, false);
        }
    }
}
