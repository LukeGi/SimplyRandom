package lhg.forgemods.simplyrandom.cobblemaker;

import lhg.forgemods.simplyrandom.core.DisableableFeature;
import lhg.forgemods.simplyrandom.core.RLProvider;
import lhg.forgemods.simplyrandom.core.SRBlock;
import lhg.forgemods.simplyrandom.core.SRTileEntityType;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec.Builder;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.registries.ObjectHolder;

/**
 * Cobblestone Maker Feature
 */
public class CobblestoneMaker extends DisableableFeature
{
    @ObjectHolder("simplyrandom:cobblestone_maker") public static final SRBlock COBBLESTONE_MAKER_BLOCK = null;
    @ObjectHolder("simplyrandom:cobblestone_maker") public static final BlockItem COBBLESTONE_MAKER_ITEM = null;
    @ObjectHolder("simplyrandom:cobblestone_maker") public static final SRTileEntityType<CobblestoneMakerTileEntity> COBBLESTONE_MAKER_TILE = null;
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
        register(event.getRegistry(), NAME, new BlockItem(COBBLESTONE_MAKER_BLOCK, DEAFULT_ITEM_PROPS.maxStackSize(1)));
    }

    @Override
    public void onRegisterTileEntityType(Register<TileEntityType<?>> event)
    {
        register(event.getRegistry(), NAME, new SRTileEntityType<>(() -> new CobblestoneMakerTileEntity(COBBLESTONE_MAKER_TILE), COBBLESTONE_MAKER_BLOCK));
    }

    @Override
    public ResourceLocation name()
    {
        return RLProvider.get(NAME);
    }
}
