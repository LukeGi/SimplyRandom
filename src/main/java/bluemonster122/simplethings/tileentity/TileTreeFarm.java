package bluemonster122.simplethings.tileentity;

import bluemonster122.simplethings.util.EnergyContainer;
import bluemonster122.simplethings.handler.ConfigurationHandler;
import com.google.common.collect.ImmutableSet;
import net.minecraft.block.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import java.util.*;

public class TileTreeFarm extends TileEnergy implements ITickable
{
    public static boolean usePower = true;
    private Map<BlockPos, SaplingGrowthSimulator> saplings = new HashMap<>();
    private ItemStackHandler inventory = new ItemStackHandler(72);
    private static Set<BlockPos> farmed = ImmutableSet.of(new BlockPos(-3, 0, -3), new BlockPos(-2, 0, -3), new BlockPos(-1, 0, -3), new BlockPos(0, 0, -3), new BlockPos(1, 0, -3), new BlockPos(2, 0, -3), new BlockPos(3, 0, -3), new BlockPos(-3, 0, -2), new BlockPos(-2, 0, -2), new BlockPos(-1, 0, -2), new BlockPos(0, 0, -2), new BlockPos(1, 0, -2), new BlockPos(2, 0, -2), new BlockPos(3, 0, -2), new BlockPos(-3, 0, -1), new BlockPos(-2, 0, -1), new BlockPos(-1, 0, -1), new BlockPos(0, 0, -1), new BlockPos(1, 0, -1), new BlockPos(2, 0, -1), new BlockPos(3, 0, -1), new BlockPos(-3, 0, 0), new BlockPos(-2, 0, 0), new BlockPos(-1, 0, 0), new BlockPos(1, 0, 0), new BlockPos(2, 0, 0), new BlockPos(3, 0, 0), new BlockPos(-3, 0, 1), new BlockPos(-2, 0, 1), new BlockPos(-1, 0, 1), new BlockPos(0, 0, 1), new BlockPos(1, 0, 1), new BlockPos(2, 0, 1), new BlockPos(3, 0, 1), new BlockPos(-3, 0, 2), new BlockPos(-2, 0, 2), new BlockPos(-1, 0, 2), new BlockPos(0, 0, 2), new BlockPos(1, 0, 2), new BlockPos(2, 0, 2), new BlockPos(3, 0, 2), new BlockPos(-3, 0, 3), new BlockPos(-2, 0, 3), new BlockPos(-1, 0, 3), new BlockPos(0, 0, 3), new BlockPos(1, 0, 3), new BlockPos(2, 0, 3), new BlockPos(3, 0, 3));
    public static Set<Block> ALLOWED_FARMING_BLOCKS = ImmutableSet.of(Blocks.DIRT, Blocks.FARMLAND, Blocks.GRASS);

    public TileTreeFarm()
    {

    }

    @Override
    public EnergyContainer makeNewBattery()
    {
        return new EnergyContainer(1000000, 10000, 0);
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing)
    {
        if (capability.equals(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY))
        {
            return true;
        }
        if (usePower)
        {
            return super.hasCapability(capability, facing);
        } else
        {
            return false;
        }
    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing)
    {
        if (capability.equals(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY))
        {
            return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(inventory);
        }
        if (usePower)
        {
            return super.getCapability(capability, facing);
        } else
        {
            return null;
        }
    }

    private boolean setupInternalFarm()
    {
        boolean flag = false;
        for (BlockPos saplingPos : farmed)
        {
            IBlockState block = getWorld().getBlockState(pos.add(saplingPos));
            if (saplings.get(pos.add(saplingPos)) == null && block.getBlock().equals(Blocks.SAPLING))
            {
                BlockPlanks.EnumType type = block.getValue(BlockSapling.TYPE);
                boolean isMega = getWorld().rand.nextInt(10) == 2 && (type == BlockPlanks.EnumType.SPRUCE || type == BlockPlanks.EnumType.JUNGLE);
                saplings.put(pos.add(saplingPos), new SaplingGrowthSimulator(type, isMega, SaplingGrowthSimulator.getMinHeight(type, isMega), SaplingGrowthSimulator.getChanceHeight(type, isMega, getWorld().rand), SaplingGrowthSimulator.getAddChanceHeight(type, isMega, getWorld().rand)));
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
                IItemHandler trialInv = new ItemStackHandler(inventory.getSlots());
                int height = sapling.isMega() ? 15 : 7;
                for (int i = 1; i < height; i++)
                {
                    canHarvest &= getWorld().isAirBlock(current.up(i));
                }
                canHarvest &= !usePower || (getEnergyStored() >= (drops.size() * ConfigurationHandler.tree_farm_break_energy));
                for (int i = 0; i < inventory.getSlots() && canHarvest; i++)
                {
                    ItemStack stackInSlot = inventory.getStackInSlot(i);
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
                        ItemHandlerHelper.insertItem(inventory, stack.copy(), false);
                        if (usePower)
                        {
                            extractEnergy(ConfigurationHandler.tree_farm_break_energy, false);
                        }
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
            IBlockState state = worldObj.getBlockState(pos);
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
            } else if (worldObj.isAirBlock(pos))
            {
                farm.remove();
            }
        }
    }

    @Override
    public void update()
    {
        findGrowers();
        getWorld().notifyBlockUpdate(pos, getWorld().getBlockState(getPos()), getWorld().getBlockState(getPos()), 3);
        if (getWorld().getTotalWorldTime() % 60 != 0) return;
        plantSaplings();
        setupInternalFarm();
        if (!saplings.isEmpty())
            tickFarms();
    }

    private void plantSaplings()
    {
        if (!getWorld().isRemote && (getEnergyStored() >= ConfigurationHandler.tree_farm_place_energy || !usePower) && inventory != null)
        {
            for (BlockPos p : farmed)
            {
                BlockPos pos = this.pos.add(p);
                if (worldObj.isAirBlock(pos) && isValidBlock(pos.down()))
                {
                    for (int i = 0; i < inventory.getSlots(); i++)
                    {
                        ItemStack stack = inventory.getStackInSlot(i);
                        if (stack != ItemStack.field_190927_a && stack.getItem() instanceof ItemBlock)
                        {
                            Block block = ((ItemBlock) stack.getItem()).block;
                            if (block == Blocks.SAPLING)
                            {
                                BlockPlanks.EnumType type = BlockPlanks.EnumType.byMetadata(stack.getItemDamage());
                                worldObj.setBlockState(pos, block.getDefaultState().withProperty(BlockSapling.TYPE, type), 3);
                                inventory.extractItem(i, 1, false);
                                if (usePower) {
                                    extractEnergy(ConfigurationHandler.tree_farm_place_energy, false);
                                }
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
        return ALLOWED_FARMING_BLOCKS.contains(worldObj.getBlockState(down).getBlock());
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
        compound.setTag("inventory", inventory.serializeNBT());
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
            inventory.deserializeNBT((NBTTagCompound) compound.getTag("inventory"));
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
        if (worldObj.isRemote)
        {
            return;
        }
        for (int i = 0; i < inventory.getSlots(); ++i)
        {
            ItemStack itemstack = inventory.getStackInSlot(i);
            if (itemstack != ItemStack.field_190927_a)
            {
                InventoryHelper.spawnItemStack(worldObj, pos.getX(), pos.getY(), pos.getZ(), itemstack);
            }
        }
    }

    public void breakSaplings()
    {
        if (worldObj.isRemote)
        {
            return;
        }
        for (BlockPos blockPos : saplings.keySet())
        {
            worldObj.destroyBlock(blockPos, true);
        }
    }

    public static class SaplingGrowthSimulator
    {
        BlockPlanks.EnumType TYPE;
        boolean harvest = false;
        boolean isMega;
        int minHeight;
        int chanceHeight;
        int additionalChanceHeight;

        public SaplingGrowthSimulator(BlockPlanks.EnumType TYPE, boolean isMega, int minHeight, int chanceHeight, int additionalChanceHeight)
        {
            this.TYPE = TYPE;
            this.isMega = isMega && (TYPE == BlockPlanks.EnumType.SPRUCE || TYPE == BlockPlanks.EnumType.JUNGLE);
            this.minHeight = minHeight;
            this.chanceHeight = chanceHeight;
            this.additionalChanceHeight = additionalChanceHeight;
        }

        public List<ItemStack> getTreeGrown(Random random)
        {
            List<ItemStack> ret = new ArrayList<>();

            // ---------------- HANDLES THE LOGS ----------------
            int logBlocks = minHeight * (TYPE == BlockPlanks.EnumType.DARK_OAK ? 4 : megaModifier()) + (chanceHeight > 0 ? random.nextInt(chanceHeight) : 1) * (TYPE == BlockPlanks.EnumType.DARK_OAK ? 4 : megaModifier()) + (additionalChanceHeight > 0 ? random.nextInt(additionalChanceHeight) : 0);
            IBlockState logState = getLogState();

            for (; logBlocks >= 0; logBlocks--)
            {
                logState.getBlock().getDrops(null, null, logState, 0).forEach(s -> ret.add(s));
            }

            // ---------------- HANDLES THE LEAVES ----------------
            IBlockState leavesState = getLeavesState();
            for (int i = 30 + random.nextInt(20) * megaModifier(); i >= 0; i--)
            {
                leavesState.getBlock().getDrops(null, null, leavesState, 0).forEach(s -> ret.add(s));
                if ((TYPE == BlockPlanks.EnumType.DARK_OAK || TYPE == BlockPlanks.EnumType.OAK) && random.nextInt(200) == 0)
                {
                    ret.add(new ItemStack(Items.APPLE, 1));
                }
            }

            return ret;
        }

        private IBlockState getLogState()
        {
            switch (TYPE)
            {
                case BIRCH:
                    return Blocks.LOG.getDefaultState().withProperty(BlockOldLog.VARIANT, TYPE);
                case SPRUCE:
                    return Blocks.LOG.getDefaultState().withProperty(BlockOldLog.VARIANT, TYPE);
                case JUNGLE:
                    return Blocks.LOG.getDefaultState().withProperty(BlockOldLog.VARIANT, TYPE);
                case DARK_OAK:
                    return Blocks.LOG2.getDefaultState().withProperty(BlockNewLog.VARIANT, TYPE);
                case ACACIA:
                    return Blocks.LOG2.getDefaultState().withProperty(BlockNewLog.VARIANT, TYPE);
                case OAK:
                default:
                    return Blocks.LOG.getDefaultState().withProperty(BlockOldLog.VARIANT, TYPE);
            }
        }

        private IBlockState getLeavesState()
        {
            switch (TYPE)
            {
                case BIRCH:
                    return Blocks.LEAVES.getDefaultState().withProperty(BlockOldLeaf.VARIANT, TYPE);
                case SPRUCE:
                    return Blocks.LEAVES.getDefaultState().withProperty(BlockOldLeaf.VARIANT, TYPE);
                case JUNGLE:
                    return Blocks.LEAVES.getDefaultState().withProperty(BlockOldLeaf.VARIANT, TYPE);
                case DARK_OAK:
                    return Blocks.LEAVES2.getDefaultState().withProperty(BlockNewLeaf.VARIANT, TYPE);
                case ACACIA:
                    return Blocks.LEAVES2.getDefaultState().withProperty(BlockNewLeaf.VARIANT, TYPE);
                case OAK:
                default:
                    return Blocks.LEAVES.getDefaultState().withProperty(BlockOldLeaf.VARIANT, TYPE);
            }
        }

        private int megaModifier()
        {
            return isMega ? 4 : 1;
        }

        public static int getMinHeight(BlockPlanks.EnumType type, boolean isMega)
        {
            switch (type)
            {
                case OAK:
                default:
                    return 4;
                case BIRCH:
                    return 3;
                case SPRUCE:
                    return isMega ? 12 : 6;
                case JUNGLE:
                    return isMega ? 15 : 7;
                case DARK_OAK:
                    return 6;
                case ACACIA:
                    return 5;
            }
        }

        public static int getChanceHeight(BlockPlanks.EnumType type, boolean isMega, Random rand)
        {
            switch (type)
            {
                case OAK:
                default:
                    return rand.nextInt(12) == 4 ? rand.nextInt(3) : rand.nextInt(8);
                case BIRCH:
                    return rand.nextInt(4);
                case SPRUCE:
                    return rand.nextInt(5);
                case JUNGLE:
                    return rand.nextInt(7);
                case DARK_OAK:
                    return rand.nextInt(3);
                case ACACIA:
                    return rand.nextInt(3);
            }
        }

        public static int getAddChanceHeight(BlockPlanks.EnumType type, boolean isMega, Random rand)
        {
            switch (type)
            {
                case OAK:
                default:
                    return rand.nextInt(6);
                case BIRCH:
                    return rand.nextInt(3);
                case SPRUCE:
                    return isMega ? rand.nextInt(2) : 0;
                case JUNGLE:
                    return isMega ? rand.nextInt(18) : 2;
                case DARK_OAK:
                    return rand.nextInt(3) + rand.nextInt(4);
                case ACACIA:
                    return rand.nextInt(4);
            }
        }

        public int getType()
        {
            return TYPE.ordinal();
        }

        public BlockPlanks.EnumType getTYPE()
        {
            return TYPE;
        }

        public boolean isMega()
        {
            return isMega;
        }

        public void setHarvest(boolean b)
        {
            harvest = b;
        }

        public boolean shouldHarvest()
        {
            return harvest;
        }
    }
}
