package bluemonster122.mods.simplethings.treefarm;

import bluemonster122.mods.simplethings.SimpleThings;
import bluemonster122.mods.simplethings.client.renderer.BoxRender;
import bluemonster122.mods.simplethings.handler.ConfigurationHandler;
import bluemonster122.mods.simplethings.tileentity.core.TileEntityST;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.oredict.OreDictionary;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class TileTreeFarm extends TileEntityST implements ITickable {
    private static final Vec3i[] farmedPositions = new Vec3i[]{new Vec3i(-3, 0, -3), new Vec3i(-3, 0, -2), new Vec3i(-2, 0, -3), new Vec3i(-2, 0, -2), new Vec3i(-3, 0, 3), new Vec3i(-3, 0, 2), new Vec3i(-2, 0, 3), new Vec3i(-2, 0, 2), new Vec3i(3, 0, -3), new Vec3i(3, 0, -2), new Vec3i(2, 0, -3), new Vec3i(2, 0, -2), new Vec3i(3, 0, 3), new Vec3i(3, 0, 2), new Vec3i(2, 0, 3), new Vec3i(2, 0, 2)};
    static NonNullList<ItemStack> sapling = OreDictionary.getOres("sapling");
    static List<Item> validItems = new ArrayList<>();

    static {
        for (ItemStack stack : sapling) {
            validItems.add(stack.getItem());
        }
    }

    public ItemStackHandler inventory = new ItemStackHandler(72);
    public EnergyStorage battery = createBattery();
    private int currentPos = -1;
    private int nextTime = 0;
    private TreeChoppa farmer = new TreeChoppa(this);
    private BoxRender render;

    @Override
    public Set<Consumer<NBTTagCompound>> getAllWrites() {
        HashSet<Consumer<NBTTagCompound>> set = new HashSet<>();
        set.addAll(getMinWrites());
        set.add(this::writeTimer);
        return set;
    }

    @Override
    public Set<Consumer<NBTTagCompound>> getAllReads() {
        HashSet<Consumer<NBTTagCompound>> set = new HashSet<>();
        set.addAll(getMinReads());
        set.add(this::readTimer);
        return set;
    }

    @Override
    public Map<Capability, Supplier<Capability>> getCaps() {
        return ImmutableMap.of(
                CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(inventory),
                CapabilityEnergy.ENERGY, CapabilityEnergy.ENERGY.cast(battery)
        );
    }

    @Override
    public Set<Consumer<NBTTagCompound>> getMinWrites() {
        return ImmutableSet.of(super::writeNBTLegacy, this::writeInventory, this::writeEnergy, this::writeScanner);
    }

    @Override
    public Set<Consumer<NBTTagCompound>> getMinReads() {
        return ImmutableSet.of(super::readNBTLegacy, this::readInventory, this::readEnergy, this::readScanner);
    }

    public NBTTagCompound writeInventory(NBTTagCompound tag) {
        NBTTagCompound nbtTagCompound = inventory.serializeNBT();
        tag.setTag("inventory", nbtTagCompound);
        return tag;
    }

    public NBTTagCompound readInventory(NBTTagCompound tag) {
        NBTTagCompound nbtTagCompound = tag.getCompoundTag("inventory");
        inventory.deserializeNBT(nbtTagCompound);
        return tag;
    }

    public NBTTagCompound writeEnergy(NBTTagCompound tag) {
        tag.setInteger("storedEnergy", battery.getEnergyStored());
        return tag;
    }

    public NBTTagCompound readEnergy(NBTTagCompound tag) {
        battery = createBattery();
        battery.receiveEnergy(tag.getInteger("storedEnergy"), false);
        return tag;
    }

    public NBTTagCompound writeScanner(NBTTagCompound tag) {
        tag.setInteger("scanner", currentPos);
        return tag;
    }

    public NBTTagCompound readScanner(NBTTagCompound tag) {
        currentPos = tag.getInteger("scanner");
        return tag;
    }

    public NBTTagCompound writeTimer(NBTTagCompound tag) {
        tag.setInteger("timer", nextTime);
        return tag;
    }

    public NBTTagCompound readTimer(NBTTagCompound tag) {
        nextTime = tag.getInteger("timer");
        return tag;
    }


    private EnergyStorage createBattery() {
        return new EnergyStorage(1000000);
    }

    /**
     * Like the old updateEntity(), except more generic.
     */
    @Override
    public void update() {
        if (getWorld().isRemote) {
            //noinspection MethodCallSideOnly
            updateClient();
        } else {
            updateServer();
        }
    }

    public void updateServer() {
        battery.receiveEnergy(1000, false);
        IBlockState state = world.getBlockState(pos);
        world.notifyBlockUpdate(pos, state, state, 3);
        nextTime--;
        if (nextTime > 0) return;
        currentPos++;
        if (currentPos == -1 || currentPos >= farmedPositions.length) {
            currentPos = 0;
        }
        BlockPos current = getPos().add(farmedPositions[currentPos]);
        int energyReq = farmer.scanTree(current, false) * ConfigurationHandler.tree_farm_break_energy;
        if (battery.getEnergyStored() >= energyReq) {
            battery.extractEnergy(energyReq, false);
            farmer.scanTree(current, true);
            if (world.isAirBlock(current))
                plantSapling();
        }
        nextTime = 5;
    }

    private void plantSapling() {
        sapling.forEach(i -> validItems.add(i.getItem()));
        for (int i = 0; i < inventory.getSlots(); i++) {
            ItemStack stack = inventory.getStackInSlot(i);
            Item item = stack.getItem();
            if (validItems.contains(item)) {
                if (item instanceof ItemBlock) {
                    Block block = ((ItemBlock) item).getBlock();
                    SimpleThings.logger.info(block.getStateFromMeta(stack.getMetadata()));
                    if (world.setBlockState(getPos().add(farmedPositions[currentPos]), block.getStateFromMeta(stack.getMetadata()), 3))
                        stack.shrink(1);
                }
            }
        }
    }

    @SideOnly(Side.CLIENT)
    public void updateClient() {
        if (this.currentPos < 0 || this.currentPos >= farmedPositions.length) return;
        if (render != null) render.cleanUp();
        BlockPos current = pos.add(farmedPositions[this.currentPos]);
        render = BoxRender.create(new Color(0, 255, 255, 50),
                new Vec3d(current.getX() - 0.0005f, current.getY() - 0.0005f, current.getZ() - 0.0005f),
                new Vec3d(current.getX() + 1.0005f, current.getY() + 1.0005f, current.getZ() + 1.0005f)
        );
        render.show();
    }
}
