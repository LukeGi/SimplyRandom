package lhg.forgemods.simplyrandom.cobblemaker;

import lhg.forgemods.simplyrandom.core.SRConfig;
import lhg.forgemods.simplyrandom.core.SRConfig.CobblestoneMakerConfig;
import lhg.forgemods.simplyrandom.core.SRTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.EnergyStorage;

/**
 * Cobblestone Maker Tile Entitiy
 */
public class CobblestoneMakerTileEntity extends SRTileEntity<CobblestoneMakerTileEntity>
{
    /**
     * Config Reference
     */
    private static final CobblestoneMakerConfig CONFIG = SRConfig.SERVER.cobblestoneMakerConfig;
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
        if (CONFIG.energyPerCobble.get() > 0)
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
        if (CONFIG.energyPerCobble.get() > 0 && battery != null)
        {
            return Math.min(64, battery.getEnergyStored() / Math.min(CONFIG.energyPerCobble.get(), 1));
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
        if (CONFIG.energyPerCobble.get() > 0 && battery != null)
        {
            battery.extractEnergy(energy, false);
        }
    }
}
