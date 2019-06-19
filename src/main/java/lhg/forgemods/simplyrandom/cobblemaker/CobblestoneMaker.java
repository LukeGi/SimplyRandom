package lhg.forgemods.simplyrandom.cobblemaker;

import lhg.forgemods.simplyrandom.SimplyRandom;
import lhg.forgemods.simplyrandom.core.DisableableFeature;
import lhg.forgemods.simplyrandom.core.ModObjects;
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
 * Cobblestone Maker Feature
 */
public class CobblestoneMaker extends DisableableFeature
{
    private static final String NAME = "cobblestone_maker";
    public IntValue energyPerCobble;

    @Override
    protected void constructConfig(Builder spec)
    {
        this.energyPerCobble = spec.comment("This will decide how much energy is required to create a cobblestone in the cobblestone maker. A value of 0 makes it free.")
                .translation("simplyrandom.config.cobblestone_maker.energyPerCobble")
                .worldRestart()
                .defineInRange("energyPerCobble", 0, 0, Integer.MAX_VALUE);
    }

    @Override
    public void onRegisterBlocks(Register<Block> event)
    {
        register(event.getRegistry(), NAME, new CobblestoneMakerBlock());
    }

    @Override
    public void onRegisterItems(Register<Item> event)
    {
        Block blockIn = ModObjects.COBBLESTONE_MAKER_BLOCK;
        Properties builder = new Properties().group(SimplyRandom.itemGroup).maxStackSize(1);
        register(event.getRegistry(), NAME, new BlockItem(blockIn, builder));
    }

    @Override
    public void onRegisterTileEntityType(Register<TileEntityType<?>> event)
    {
        register(event.getRegistry(), NAME, new SRTileEntityType<>(() -> new CobblestoneMakerTileEntity(ModObjects.COBBLESTONE_MAKER_TILE), ModObjects.COBBLESTONE_MAKER_BLOCK));
    }

    @Override
    public ResourceLocation name()
    {
        return getOrCreateName(NAME);
    }
}
