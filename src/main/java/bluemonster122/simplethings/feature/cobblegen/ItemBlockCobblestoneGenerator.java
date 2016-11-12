package bluemonster122.simplethings.feature.cobblegen;

import bluemonster122.simplethings.SimpleThings;
import net.minecraft.item.ItemBlock;

public class ItemBlockCobblestoneGenerator extends ItemBlock
{
    public ItemBlockCobblestoneGenerator()
    {
        super(FeatureCobblestoneGenerator.COBBLESTONE_GENERATOR);
        setRegistryName(SimpleThings.MOD_ID, "cobblestonegenerator");
        setUnlocalizedName(getRegistryName().getResourcePath());
    }
}
