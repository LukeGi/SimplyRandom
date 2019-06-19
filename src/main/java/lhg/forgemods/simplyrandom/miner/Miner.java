package lhg.forgemods.simplyrandom.miner;

import lhg.forgemods.simplyrandom.core.DisableableFeature;
import lhg.forgemods.simplyrandom.core.RLProvider;
import lhg.forgemods.simplyrandom.core.SRBlock;
import lhg.forgemods.simplyrandom.core.SRTileEntityType;
import net.minecraft.block.Block;
import net.minecraft.block.Block.Properties;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec.Builder;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.registries.ObjectHolder;

public class Miner extends DisableableFeature
{
    @ObjectHolder("simplyrandom:miner") public static final SRBlock MINER_BLOCK = null;
    @ObjectHolder("simplyrandom:miner") public static final BlockItem MINER_ITEM = null;
    @ObjectHolder("simplyrandom:miner") public static final SRTileEntityType<MinerTileEntity> MINER_TILE = null;
    private static final String NAME = "miner";

    @Override
    protected void constructConfig(Builder spec)
    {

    }

    @Override
    public void onRegisterBlocks(Register<Block> event)
    {
        register(event.getRegistry(), NAME, new MinerBlock(Properties.create(Material.IRON).hardnessAndResistance(5F, 6F)));
    }

    @Override
    public void onRegisterItems(Register<Item> event)
    {
        register(event.getRegistry(), NAME, new BlockItem(MINER_BLOCK, DEAFULT_ITEM_PROPS.maxStackSize(1)));
    }

    @Override
    public void onRegisterTileEntityType(Register<TileEntityType<?>> event)
    {
        register(event.getRegistry(), NAME, new SRTileEntityType<>(() -> new MinerTileEntity(MINER_TILE), MINER_BLOCK));
    }

    @Override
    public ResourceLocation name()
    {
        return RLProvider.get(NAME);
    }
}
