package bluemonster122.simplethings.tileentity;

import bluemonster122.simplethings.handler.ConfigurationHandler;
import bluemonster122.simplethings.util.CapabilityHelper;
import cofh.api.energy.EnergyStorage;
import com.google.common.collect.ImmutableSet;
import net.minecraft.block.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import java.util.*;

public class TileTreeFarm extends TileEntity implements ITickable, IItemHandler, IEnergyStorage
{
    private static Set<BlockPos> farmed = ImmutableSet.of(new BlockPos(-3, 0, -3), new BlockPos(-2, 0, -3), new BlockPos(-1, 0, -3), new BlockPos(0, 0, -3), new BlockPos(1, 0, -3), new BlockPos(2, 0, -3), new BlockPos(3, 0, -3), new BlockPos(-3, 0, -2), new BlockPos(-2, 0, -2), new BlockPos(-1, 0, -2), new BlockPos(0, 0, -2), new BlockPos(1, 0, -2), new BlockPos(2, 0, -2), new BlockPos(3, 0, -2), new BlockPos(-3, 0, -1), new BlockPos(-2, 0, -1), new BlockPos(-1, 0, -1), new BlockPos(0, 0, -1), new BlockPos(1, 0, -1), new BlockPos(2, 0, -1), new BlockPos(3, 0, -1), new BlockPos(-3, 0, 0), new BlockPos(-2, 0, 0), new BlockPos(-1, 0, 0), new BlockPos(1, 0, 0), new BlockPos(2, 0, 0), new BlockPos(3, 0, 0), new BlockPos(-3, 0, 1), new BlockPos(-2, 0, 1), new BlockPos(-1, 0, 1), new BlockPos(0, 0, 1), new BlockPos(1, 0, 1), new BlockPos(2, 0, 1), new BlockPos(3, 0, 1), new BlockPos(-3, 0, 2), new BlockPos(-2, 0, 2), new BlockPos(-1, 0, 2), new BlockPos(0, 0, 2), new BlockPos(1, 0, 2), new BlockPos(2, 0, 2), new BlockPos(3, 0, 2), new BlockPos(-3, 0, 3), new BlockPos(-2, 0, 3), new BlockPos(-1, 0, 3), new BlockPos(0, 0, 3), new BlockPos(1, 0, 3), new BlockPos(2, 0, 3), new BlockPos(3, 0, 3));
    public static Set<Block> ALLOWED_FARMING_BLOCKS = ImmutableSet.of(Blocks.DIRT, Blocks.FARMLAND, Blocks.GRASS);

    private Map<BlockPos, SaplingGrowthSimulator> saplings = new HashMap<>();
    private ItemStackHandler inventory = new ItemStackHandler(72);
    private EnergyStorage energy = new EnergyStorage(1000000);

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing)
    {
        return CapabilityHelper.hasCapability(this, capability);
    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing)
    {
        return CapabilityHelper.getCapability(this, capability);
    }

    private boolean setupInternalFarm()
    {
        boolean flag = false;
        for (BlockPos saplingPos : farmed)
        {
            IBlockState block = getWorld().getBlockState(getPos().add(saplingPos));
            if (saplings.get(getPos().add(saplingPos)) == null && block.getBlock().equals(Blocks.SAPLING))
            {
                BlockPlanks.EnumType type = block.getValue(BlockSapling.TYPE);
                boolean isMega = getWorld().rand.nextInt(10) == 2 && (type == BlockPlanks.EnumType.SPRUCE || type == BlockPlanks.EnumType.JUNGLE);
                saplings.put(getPos().add(saplingPos), new SaplingGrowthSimulator(type, isMega, SaplingGrowthSimulator.getMinHeight(type, isMega), SaplingGrowthSimulator.getChanceHeight(type, isMega, getWorld().rand), SaplingGrowthSimulator.getAddChanceHeight(type, isMega, getWorld().rand)));
                block.getBlock().setTickRandomly(false);
                flag = true;
            }
        }
        return flag;
    }

    private void tickFarms()
    {
        Iterator<BlockPos> farm = saplings.keySet().iterator();
        while (farm.hasNext())
        {
            BlockPos current = farm.next();
            SaplingGrowthSimulator sapling = saplings.get(current);
            if (sapling.shouldHarvest())
            {
                List<ItemStack> drops = sapling.getTreeGrown(getWorld().rand);
                boolean canHarvest = true;
                IItemHandler trialInv = new ItemStackHandler(getSlots());
                int height = sapling.isMega() ? 15 : 7;
                for (int i = 1; i < height; i++)
                {
                    canHarvest &= getWorld().isAirBlock(current.up(i));
                }
                canHarvest &= getEnergyStored() >= (drops.size() * ConfigurationHandler.tree_farm_break_energy);
                for (int i = 0; i < getSlots() && canHarvest; i++)
                {
                    ItemStack stackInSlot = getStackInSlot(i);
                    if (stackInSlot != ItemStack.field_190927_a)
                    {
                        trialInv.insertItem(i, stackInSlot.copy(), false);
                    }
                }
                for (int i = 0; i < drops.size() && canHarvest; i++)
                {
                    ItemStack drop = drops.get(i).copy();
                    canHarvest = ItemHandlerHelper.insertItem(trialInv, drop, false) == ItemStack.field_190927_a;
                }
                if (canHarvest)
                {
                    farm.remove();
                    drops.forEach(stack ->
                    {
                        ItemHandlerHelper.insertItem(getInventory(), stack.copy(), false);
                        extractEnergy(ConfigurationHandler.tree_farm_break_energy, false);
                    });
                    getWorld().setBlockToAir(current);
                }
            }
        }
    }

    private void findGrowers()
    {
        Iterator<BlockPos> farm = saplings.keySet().iterator();
        while (farm.hasNext())
        {
            BlockPos pos = farm.next();
            IBlockState state = getWorld().getBlockState(pos);
            Block block = state.getBlock();
            if (block instanceof BlockSapling)
            {
                if (!state.getValue(BlockSapling.TYPE).equals(saplings.get(pos).getTYPE()))
                {
                    farm.remove();
                } else if (state.getValue(BlockSapling.STAGE) != 0)
                {
                    saplings.get(pos).setHarvest(true);
                }
            } else if (getWorld().isAirBlock(pos))
            {
                farm.remove();
            }
        }
    }

    @Override
    public void update()
    {
        findGrowers();
        getWorld().notifyBlockUpdate(getPos(), getWorld().getBlockState(getPos()), getWorld().getBlockState(getPos()), 3);
        if (getWorld().getTotalWorldTime() % 60 != 0) return;
        plantSaplings();
        setupInternalFarm();
        if (!saplings.isEmpty())
            tickFarms();
    }

    private void plantSaplings()
    {
        if (!getWorld().isRemote && getEnergyStored() >= ConfigurationHandler.tree_farm_place_energy)
        {
            for (BlockPos p : farmed)
            {
                BlockPos pos = getPos().add(p);
                if (getWorld().isAirBlock(pos) && isValidBlock(pos.down()))
                {
                    for (int i = 0; i < getInventory().getSlots(); i++)
                    {
                        ItemStack stack = getStackInSlot(i);
                        if (stack != ItemStack.field_190927_a && stack.getItem() instanceof ItemBlock)
                        {
                            Block block = ((ItemBlock) stack.getItem()).getBlock();
                            if (block == Blocks.SAPLING)
                            {
                                BlockPlanks.EnumType type = BlockPlanks.EnumType.byMetadata(stack.getItemDamage());
                                getWorld().setBlockState(pos, block.getDefaultState().withProperty(BlockSapling.TYPE, type), 3);
                                extractItem(i, 1, false);
                                extractEnergy(ConfigurationHandler.tree_farm_place_energy, false);
                                break;
                            }
                        }
                    }
                }
            }
        }
    }

    private boolean isValidBlock(BlockPos down)
    {
        return ALLOWED_FARMING_BLOCKS.contains(getWorld().getBlockState(down).getBlock());
    }

    @Override
    public SPacketUpdateTileEntity getUpdatePacket()
    {
        NBTTagCompound nbt = new NBTTagCompound();
        this.writeToNBT(nbt);

        return new SPacketUpdateTileEntity(getPos(), 0, nbt);

    }

    @Override
    @SideOnly(Side.CLIENT)
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt)
    {
        this.readFromNBT(pkt.getNbtCompound());
    }

    @Override
    @Nonnull
    public NBTTagCompound getUpdateTag()
    {
        NBTTagCompound nbt = super.getUpdateTag();
        writeToNBT(nbt);
        return nbt;
    }

    @Override
    @Nonnull
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);
        compound.setTag("inventory", getInventory().serializeNBT());
        compound.setInteger("saplingsize", saplings.keySet().size());
        int id = 0;
        for (BlockPos pos : saplings.keySet())
        {
            SaplingGrowthSimulator slot = saplings.get(pos);
            compound.setLong("sapPos" + id, pos.toLong());
            compound.setInteger("sapType" + id, slot.getType());
            compound.setBoolean("sapGrow" + id++, slot.shouldHarvest());
        }
        return compound;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);
        Random random = new Random();
        if (compound.hasKey("inventory"))
        {
            getInventory().deserializeNBT((NBTTagCompound) compound.getTag("inventory"));
        }
        int saplingsize = compound.getInteger("saplingsize");
        for (int i = 0; i < saplingsize; i++)
        {
            BlockPos pos = BlockPos.fromLong(compound.getLong("sapPos" + i));
            BlockPlanks.EnumType type = BlockPlanks.EnumType.byMetadata(compound.getInteger("sapType" + i));
            boolean isMega = random.nextInt(10) == 2 && (type == BlockPlanks.EnumType.SPRUCE || type == BlockPlanks.EnumType.JUNGLE);
            SaplingGrowthSimulator slot = new SaplingGrowthSimulator(type, isMega, SaplingGrowthSimulator.getMinHeight(type, isMega), SaplingGrowthSimulator.getChanceHeight(type, isMega, random), SaplingGrowthSimulator.getAddChanceHeight(type, isMega, random));
            slot.setHarvest(compound.getBoolean("sapGrow" + i));
            saplings.put(pos, slot);
        }
    }

    public void dropInventory()
    {
        if (getWorld().isRemote)
        {
            return;
        }
        for (int i = 0; i < getSlots(); ++i)
        {
            ItemStack itemstack = getStackInSlot(i);
            if (itemstack != ItemStack.field_190927_a)
            {
                InventoryHelper.spawnItemStack(getWorld(), getPos().getX(), getPos().getY(), getPos().getZ(), itemstack);
            }
        }
    }

    public void breakSaplings()
    {
        if (getWorld().isRemote)
        {
            return;
        }
        for (BlockPos blockPos : saplings.keySet())
        {
            getWorld().destroyBlock(blockPos, true);
        }
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate)
    {
        return getEnergy().receiveEnergy(maxReceive, simulate);
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate)
    {
        return getEnergy().extractEnergy(maxExtract, simulate);
    }

    @Override
    public int getEnergyStored()
    {
        return getEnergy().getEnergyStored();
    }

    @Override
    public int getMaxEnergyStored()
    {
        return getEnergy().getMaxEnergyStored();
    }

    @Override
    public boolean canExtract()
    {
        return false;
    }

    @Override
    public boolean canReceive()
    {
        return true;
    }

    @Override
    public int getSlots()
    {
        return getInventory().getSlots();
    }

    @Nonnull
    @Override
    public ItemStack getStackInSlot(int slot)
    {
        return getInventory().getStackInSlot(slot);
    }

    @Nonnull
    @Override
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate)
    {
        return getInventory().insertItem(slot, stack, simulate);
    }

    @Nonnull
    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate)
    {
        return getInventory().extractItem(slot, amount, simulate);
    }

    public ItemStackHandler getInventory()
    {
        return inventory;
    }

    public EnergyStorage getEnergy()
    {
        return energy;
    }
}
