package lhg.forgemods.simplyrandom.core;

import lhg.forgemods.simplyrandom.cobblemaker.CobblestoneMakerTileEntity;
import lhg.forgemods.simplyrandom.treefarm.TreeFarmTileEntity;
import net.minecraft.item.BlockItem;
import net.minecraftforge.registries.ObjectHolder;

/**
 * Object holders for all objects added to the registry
 */
public class ModObjects
{
    @ObjectHolder("simplyrandom:cobblestone_maker") public static final SRBlock COBBLESTONE_MAKER_BLOCK = null;
    @ObjectHolder("simplyrandom:cobblestone_maker") public static final BlockItem COBBLESTONE_MAKER_ITEM = null;
    @ObjectHolder("simplyrandom:cobblestone_maker") public static final SRTileEntityType<CobblestoneMakerTileEntity> COBBLESTONE_MAKER_TILE = null;

    @ObjectHolder("simplyrandom:tree_farm") public static final SRBlock TREE_FARM_BLOCK = null;
    @ObjectHolder("simplyrandom:tree_farm") public static final BlockItem TREE_FARM_ITEM = null;
    @ObjectHolder("simplyrandom:tree_farm") public static final SRTileEntityType<TreeFarmTileEntity> TREE_FARM_TILE = null;
}
