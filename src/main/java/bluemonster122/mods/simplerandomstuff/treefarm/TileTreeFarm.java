package bluemonster122.mods.simplerandomstuff.treefarm;

import bluemonster122.mods.simplerandomstuff.SimpleRandomStuff;
import bluemonster122.mods.simplerandomstuff.client.renderer.BoxRender;
import bluemonster122.mods.simplerandomstuff.core.IHaveGui;
import bluemonster122.mods.simplerandomstuff.core.block.IHaveInventory;
import bluemonster122.mods.simplerandomstuff.core.block.TileST;
import bluemonster122.mods.simplerandomstuff.core.energy.BatteryST;
import bluemonster122.mods.simplerandomstuff.core.energy.IEnergyRecieverST;
import com.google.common.collect.ImmutableMap;
import net.minecraft.block.Block;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.oredict.OreDictionary;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static bluemonster122.mods.simplerandomstuff.treefarm.FRTreeFarm.INSTANCE;

public class TileTreeFarm extends TileST implements ITickable, IEnergyRecieverST, IHaveInventory, IHaveGui {
    private static final Vec3i[] farmedPositions = new Vec3i[]{new Vec3i(-3, 0, -3), new Vec3i(-3, 0, -2), new Vec3i(-2, 0, -3), new Vec3i(-2, 0, -2), new Vec3i(-3, 0, 3), new Vec3i(-3, 0, 2), new Vec3i(-2, 0, 3), new Vec3i(-2, 0, 2), new Vec3i(3, 0, -3), new Vec3i(3, 0, -2), new Vec3i(2, 0, -3), new Vec3i(2, 0, -2), new Vec3i(3, 0, 3), new Vec3i(3, 0, 2), new Vec3i(2, 0, 3), new Vec3i(2, 0, 2)};
    static NonNullList<ItemStack> sapling = OreDictionary.getOres("sapling");
    static List<Item> validItems = new ArrayList<>();

    static {
        for (ItemStack stack : sapling) {
            validItems.add(stack.getItem());
        }
    }

    /**
     * Inventory
     */
    public ItemStackHandler inventory = createInventory();
    /**
     * Battery
     */
    public BatteryST battery = createBattery();
    private int currentPos = -1;
    private int nextTime = 0;
    private TreeChoppa farmer = new TreeChoppa(this);
    private BoxRender render;

    @Override
    public Map<Capability, Capability> getCaps( ) {
        return ImmutableMap.of(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast((IItemHandler) inventory), CapabilityEnergy.ENERGY, CapabilityEnergy.ENERGY.cast((IEnergyStorage) battery));
    }

    @Override
    public NBTTagCompound writeChild(NBTTagCompound tag) {
        tag.setInteger("scanner", currentPos);
        tag.setInteger("timer", nextTime);
        return tag;
    }

    @Override
    public NBTTagCompound readChild(NBTTagCompound tag) {
        currentPos = tag.getInteger("scanner");
        nextTime = tag.getInteger("timer");
        return tag;
    }

    /**
     * Like the old updateEntity(), except more generic.
     */
    @Override
    public void update( ) {
        if (getWorld().isRemote) {
            //noinspection MethodCallSideOnly
            updateClient();
        } else {
            updateServer();
        }
    }

    public void updateServer( ) {
        sendUpdate();
        nextTime--;
        if (nextTime > 0) return;
        currentPos++;
        if (currentPos == -1 || currentPos >= farmedPositions.length) {
            currentPos = 0;
        }
        BlockPos current = getPos().add(farmedPositions[currentPos]);
        int energyReq = farmer.scanTree(current, false) * INSTANCE.getBreakEnergy();
        if (battery.getEnergyStored() >= energyReq) {
            battery.extractEnergy(energyReq, false);
            farmer.scanTree(current, true);
            if (world.isAirBlock(current)) plantSapling();
        }
        nextTime = 5;
    }

    @SuppressWarnings("deprecation")
    private void plantSapling( ) {
        sapling.forEach(i -> validItems.add(i.getItem()));
        for (int i = 0; i < inventory.getSlots(); i++) {
            ItemStack stack = inventory.getStackInSlot(i);
            Item item = stack.getItem();
            if (validItems.contains(item) && item instanceof ItemBlock) {
                Block block = ((ItemBlock) item).getBlock();
                SimpleRandomStuff.INSTANCE.logger.info(block.getStateFromMeta(stack.getMetadata()));
                if (world.setBlockState(getPos().add(farmedPositions[currentPos]), block.getStateFromMeta(stack.getMetadata()), 3))
                    stack.shrink(1);
            }
        }
    }

    @SideOnly(Side.CLIENT)
    public void updateClient( ) {
        if (this.currentPos < 0 || this.currentPos >= farmedPositions.length) return;
        if (render != null) render.cleanUp();
        BlockPos current = pos.add(farmedPositions[this.currentPos]);
        render = BoxRender.create(new Color(0, 255, 255, 50), new Vec3d(current.getX() - 0.0005f, current.getY() - 0.0005f, current.getZ() - 0.0005f), new Vec3d(current.getX() + 1.0005f, current.getY() + 1.0005f, current.getZ() + 1.0005f));
        render.show();
    }

    /**
     * Gets the battery of the Block
     *
     * @return The battery object.
     */
    @Override
    public BatteryST getBattery( ) {
        return battery;
    }

    /**
     * Sets the given BatteryST to be the Tile's Battery.
     *
     * @param battery new Battery.
     */
    @Override
    public void setBattery(BatteryST battery) {
        this.battery = battery;
    }

    /**
     * Creates a new Battery for the Tile.
     *
     * @return a new Battery for the Tile.
     */
    @Override
    public BatteryST createBattery( ) {
        return new BatteryST(1000000);
    }

    /**
     * Gets the Tile's current Inventory.
     *
     * @return The Tile's current Inventory.
     */
    @Override
    public ItemStackHandler getInventory( ) {
        return inventory;
    }

    /**
     * Creates a new Inventory for the Tile.
     *
     * @return a new Inventory for the Tile.
     */
    @Override
    public ItemStackHandler createInventory( ) {
        return new ItemStackHandler(72);
    }

    /**
     * Sets the given ItemStackHandler to be the Tile's Inventory.
     *
     * @param inventory new Inventory.
     */
    @Override
    public void setInventory(ItemStackHandler inventory) {
        this.inventory = inventory;
    }

    @Override
    public String getName( ) {
        return "simplerandomstuff:tree_farm";
    }

    @Override
    public Gui createGui(InventoryPlayer player, World world, BlockPos pos) {
        return new GuiTreeFarm(player, this);
    }

    @Override
    public Container createContainer(InventoryPlayer player, World world, BlockPos pos) {
        return new ContainerTreeFarm(player, this);
    }
}
