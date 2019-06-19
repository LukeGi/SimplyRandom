package lhg.forgemods.simplyrandom.miner;

import lhg.forgemods.simplyrandom.SimplyRandom;
import lhg.forgemods.simplyrandom.core.DisableableFeature;
import lhg.forgemods.simplyrandom.core.ModObjects;
import lhg.forgemods.simplyrandom.core.RLProvider;
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

public class Miner extends DisableableFeature
{
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
        Block blockIn = ModObjects.MINER_BLOCK;
        Item.Properties builder = new Item.Properties().group(SimplyRandom.itemGroup).maxStackSize(1);
        register(event.getRegistry(), NAME, new BlockItem(blockIn, builder));
    }

    @Override
    public void onRegisterTileEntityType(Register<TileEntityType<?>> event)
    {
        register(event.getRegistry(), NAME, new SRTileEntityType<>(() -> new MinerTileEntity(ModObjects.MINER_TILE), ModObjects.MINER_BLOCK));
    }

    @Override
    public ResourceLocation name()
    {
        return RLProvider.get(NAME);
    }
}
