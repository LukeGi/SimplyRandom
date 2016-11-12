package bluemonster122.simplethings.feature.firegenerator;

import net.minecraft.item.ItemBlock;

public class ItemBlockFireGenerator extends ItemBlock
{
    public ItemBlockFireGenerator()
    {
        super(FeatureFireGenerator.FIRE_GENERATOR);
        setRegistryName(block.getRegistryName());
        setUnlocalizedName(block.getUnlocalizedName());
    }
}
