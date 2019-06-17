package lhg.forgemods.simplyrandom.treefarm;

import lhg.forgemods.simplyrandom.SimplyRandom;
import lhg.forgemods.simplyrandom.core.DisableableFeature;
import lhg.forgemods.simplyrandom.core.ModObjects;
import lhg.forgemods.simplyrandom.core.SRConfig;
import lhg.forgemods.simplyrandom.core.SRTileEntityType;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.Item.Properties;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent.Register;

/**
 * This is the Tree Farm feature
 */
public class TreeFarm extends DisableableFeature
{
    private static final String NAME = "tree_farm";

    @Override
    public void onRegisterBlocks(Register<Block> event)
    {
        register(event.getRegistry(), NAME, new TreeFarmBlock());
    }

    @Override
    public void onRegisterItems(Register<Item> event)
    {
        Block blockIn = ModObjects.TREE_FARM_BLOCK;
        Properties builder = new Properties().group(SimplyRandom.itemGroup).maxStackSize(1);
        register(event.getRegistry(), NAME, new BlockItem(blockIn, builder));
    }

    @Override
    public void onRegisterTileEntityType(Register<TileEntityType<?>> event)
    {
        register(event.getRegistry(), NAME, new SRTileEntityType<>(() -> new TreeFarmTileEntity(ModObjects.TREE_FARM_TILE), ModObjects.TREE_FARM_BLOCK));
    }

    @Override
    public boolean enabled()
    {
        return SRConfig.SERVER.treeFarmConfig.enabled.get();
    }

    @Override
    public ResourceLocation name()
    {
        return getOrCreateName(NAME);
    }
}
