package lhg.forgemods.simplyrandom.treefarm;

import lhg.forgemods.simplyrandom.SimplyRandom;
import lhg.forgemods.simplyrandom.core.DisableableFeature;
import lhg.forgemods.simplyrandom.core.ModObjects;
import lhg.forgemods.simplyrandom.core.RLProvider;
import lhg.forgemods.simplyrandom.core.SRTileEntityType;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.Item.Properties;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec.Builder;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;
import net.minecraftforge.event.RegistryEvent.Register;

/**
 * This is the Tree Farm feature
 */
public class TreeFarm extends DisableableFeature
{
    private static final String NAME = "tree_farm";
    public IntValue blockScanEnergy;
    public IntValue inventoryScanEnergy;
    public IntValue leafBreakEnergy;
    public IntValue logBreakEnergy;
    public IntValue maxPower;

    @Override
    protected void constructConfig(Builder spec)
    {
        this.blockScanEnergy = spec.comment("This will decide how much energy is required to scan a block in the world. A value of 0 makes it free.")
                .translation("simplyrandom.config.tree_farm.blockScanEnergy")
                .worldRestart()
                .defineInRange("blockScanEnergy", 0, 0, Integer.MAX_VALUE);
        this.inventoryScanEnergy = spec.comment("This will decide how much energy is required to scan a inventories for saplings. A value of 0 makes it free.")
                .translation("simplyrandom.config.tree_farm.inventoryScanEnergy")
                .worldRestart()
                .defineInRange("inventoryScanEnergy", 0, 0, Integer.MAX_VALUE);
        this.leafBreakEnergy = spec.comment("This will decide how much energy is required to break a leaf block. A value of 0 makes it free.")
                .translation("simplyrandom.config.tree_farm.leafBreakEnergy")
                .worldRestart()
                .defineInRange("leafBreakEnergy", 0, 0, Integer.MAX_VALUE);
        this.logBreakEnergy = spec.comment("This will decide how much energy is required to break a log block. A value of 0 makes it free.")
                .translation("simplyrandom.config.tree_farm.logBreakEnergy")
                .worldRestart()
                .defineInRange("logBreakEnergy", 0, 0, Integer.MAX_VALUE);
        this.maxPower = spec.comment("This will decide how much energy can be stored in a tree farm. A value of 0 means will mean you can't use it if you have setup any power requirements.")
                .translation("simplyrandom.config.tree_farm.maxPower")
                .worldRestart()
                .defineInRange("maxPower", 0, 0, Integer.MAX_VALUE);
    }

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
    public ResourceLocation name()
    {
        return RLProvider.get(NAME);
    }
}
