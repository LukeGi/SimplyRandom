package bluemonster122.mods.simplethings.tileentity;

import bluemonster122.mods.simplethings.handler.ConfigurationHandler;
import bluemonster122.mods.simplethings.tileentity.core.IMachine;
import bluemonster122.mods.simplethings.tileentity.core.TileEntityST;
import bluemonster122.mods.simplethings.util.AreaType;
import com.google.common.collect.ImmutableSet;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class TileTreeFarm extends TileEntityST implements ITickable, IMachine {
    public static Set<Block> ALLOWED_FARMING_BLOCKS = ImmutableSet.of(Blocks.DIRT, Blocks.FARMLAND, Blocks.GRASS);
    private final Predicate<BlockPos> isWood = p -> getWorld().getBlockState(p).getMaterial().equals(
            Material.WOOD) && getWorld().getBlockState(p).getBlock() instanceof BlockLog;
    private final Predicate<BlockPos> isLeaves = p -> getWorld().getBlockState(p).getMaterial().equals(
            Material.LEAVES) && getWorld().getBlockState(p).getBlock() instanceof BlockLeaves;
    private AreaType farmedArea = AreaType.SMALL;
    private ItemStackHandler inventory = new ItemStackHandler(72);
    private EnergyStorage battery = new EnergyStorage(1000000);
    private Stack<BlockPos> toBreak = new Stack<>();
    private long workToTime;

    @Override
    public void update() {
        if (getWorld().isRemote) {
            return;
        }
        if (farmedArea == null) {
            return;
        }
        workToTime = System.currentTimeMillis() + 25;
        if (getWorld().getTotalWorldTime() % 10 == 0 && toBreak.isEmpty()) {
            List<BlockPos> farmSpots = farmedArea.getArea().stream().map(p -> getPos().add(p)).collect(
                    Collectors.toList());
            List<BlockPos> air = farmSpots.stream()
                    .filter(p -> getWorld().getBlockState(p).getBlock().isReplaceable(getWorld(), p))
                    .filter(this::isValidBlock)
                    .collect(Collectors.toList());
            if (!air.isEmpty()) {
                fillAnAir(air);
            }
            farmSpots.stream().filter(p -> !toBreak.contains(p)).filter(isWood).forEach(p -> toBreak.push(p));
        }
        int i = 0;
        while (!toBreak.isEmpty() && work() && i < 1 + toBreak.size() / 100.0 && breakAndScan(toBreak.peek())) {
            i++;
        }
    }

    private void fillAnAir(List<BlockPos> air) {
        if (getBattery().getEnergyStored() < ConfigurationHandler.tree_farm_place_energy) {
            return;
        }
        int i = 0;
        BlockPos blockPos = air.get(i);
        while (!getWorld().getBlockState(blockPos).getBlock().isReplaceable(getWorld(), blockPos)) {
            if (i < air.size()) {
                blockPos = air.get(i++);
            }
        }
        for (int j = 0; j < getInventory().getSlots(); j++) {
            ItemStack stack = getInventory().getStackInSlot(j);
            if (stack != ItemStack.EMPTY && stack.getItem() instanceof ItemBlock) {
                Block block = ((ItemBlock) stack.getItem()).getBlock();
                if (block == Blocks.SAPLING) {
                    BlockPlanks.EnumType type = BlockPlanks.EnumType.byMetadata(stack.getItemDamage());
                    getWorld().setBlockState(
                            blockPos, block.getDefaultState().withProperty(BlockSapling.TYPE, type), 3);
                    getInventory().extractItem(j, 1, false);
                    getBattery().extractEnergy(ConfigurationHandler.tree_farm_place_energy, false);
                }
            }
        }
        sendUpdate();
    }

    private boolean work() {
        return System.currentTimeMillis() < workToTime;
    }

    private boolean breakAndScan(BlockPos blockPos) {
        if (getBattery().getEnergyStored() < ConfigurationHandler.tree_farm_break_energy) {
            return false;
        }
        IBlockState blockState = getWorld().getBlockState(blockPos);
        Block block = blockState.getBlock();
        List<ItemStack> drops = block.getDrops(getWorld(), blockPos, blockState, 0);
        if (drops.isEmpty() || ItemHandlerHelper.insertItem(getInventory(), drops.get(0), true) == ItemStack.EMPTY) {
            toBreak.pop();
            getWorld().destroyBlock(blockPos, false);
            drops.forEach(itemStack -> ItemHandlerHelper.insertItem(getInventory(), itemStack, false));
            getBattery().extractEnergy(ConfigurationHandler.tree_farm_break_energy, false);

            for (BlockPos offset : AreaType.AROUND.getArea()) {
                for (int i = -1; i <= 1; i++) {
                    BlockPos current = blockPos.add(offset.getX(), offset.getY() + i, offset.getZ());
                    if (!toBreak.contains(current) && isLeaves.or(isWood).test(current)) {
                        toBreak.push(current);
                    }
                }
            }
            sendUpdate();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public EnergyStorage getBattery() {
        return battery;
    }

    @Override
    public ItemStackHandler getInventory() {
        return inventory;
    }

    public void breakSaplings() {
        if (farmedArea == null) {
            return;
        }
        farmedArea.getArea().stream().map(getPos()::add).filter(
                b -> getWorld().getBlockState(b).getBlock() instanceof BlockBush).forEach(
                blockPos -> getWorld().destroyBlock(blockPos, true));
    }

    private boolean isValidBlock(BlockPos down) {
        return ALLOWED_FARMING_BLOCKS.contains(getWorld().getBlockState(down.down()).getBlock());
    }

    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        NBTTagCompound nbt = new NBTTagCompound();
        this.writeToNBTLight(nbt);
        return new SPacketUpdateTileEntity(getPos(), 0, nbt);
    }

    private void writeToNBTLight(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        this.readFromNBTLight(pkt.getNbtCompound());
    }

    private void readFromNBTLight(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
    }

    @Override
    @Nonnull
    public NBTTagCompound getUpdateTag() {
        NBTTagCompound nbt = super.getUpdateTag();
        writeToNBTLight(nbt);
        return nbt;
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound.setInteger("areaType", AreaType.getIndex(farmedArea));
        if (!toBreak.isEmpty())
            compound.setTag("toBreak", CollectionToNBT(toBreak));
        return super.writeToNBT(compound);
    }

    private NBTTagCompound CollectionToNBT(Collection<? extends BlockPos> list) {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setInteger("size", list.size());
        BlockPos[] arr = new BlockPos[list.size()];
        list.toArray(arr);
        for (int i = 0; i < list.size(); i++) {
            nbt.setLong(String.valueOf(i), arr[i].toLong());
        }
        return nbt;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        setFarmedArea(AreaType.getFromIndex(compound.getInteger("areaType")));
        if (compound.hasKey("toBreak"))
            NBTToList(compound.getCompoundTag("toBreak")).forEach(p -> toBreak.push(p));
        super.readFromNBT(compound);
    }

    public void setFarmedArea(AreaType setArea) {
        farmedArea = setArea;
    }

    private List<BlockPos> NBTToList(NBTTagCompound scanTag) {
        List<BlockPos> ret = new ArrayList<>();
        for (int i = 0; i < scanTag.getInteger("size"); i++) {
            ret.add(BlockPos.fromLong(scanTag.getLong(String.valueOf(i))));
        }
        return ret;
    }
}
