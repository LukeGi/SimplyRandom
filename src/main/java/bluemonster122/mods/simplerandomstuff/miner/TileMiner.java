package bluemonster122.mods.simplerandomstuff.miner;

import bluemonster122.mods.simplerandomstuff.client.renderer.BoxRender;
import bluemonster122.mods.simplerandomstuff.core.IHaveGui;
import bluemonster122.mods.simplerandomstuff.core.energy.BatteryST;
import bluemonster122.mods.simplerandomstuff.util.Ticker;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.oredict.OreDictionary;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.List;

public class TileMiner extends TileEntity implements ITickable, IHaveGui {
    private boolean canWork = false;
    private ItemStackHandler inventory = new ItemStackHandler(27);
    private BatteryST battery = new BatteryST(5000);
    private BlockPos currentPosition;
    private NonNullList<ItemStack> ores = generateOresList();
    private NonNullList<ItemStack> drops = NonNullList.create();
    private Ticker ticker = new Ticker(10);
    private BoxRender render = null;

    private static NonNullList<ItemStack> generateOresList( ) {
        NonNullList<ItemStack> ores = OreDictionary.EMPTY_LIST;
        for (String s : OreDictionary.getOreNames()) {
            if (s.startsWith("ore")) {
                ores.addAll(OreDictionary.getOres(s, false));
            }
        }
        return ores;
    }

    @Override
    public void update( ) {
        if (currentPosition == null) {
            currentPosition = getPos();
        }
        if (world.isRemote) {
            //noinspection MethodCallSideOnly
            updateClient();
        } else {
            updateServer();
        }
    }

    private void updateServer( ) {
        ticker.tick();
        if (ticker.time_up()) {
            checkIfCanWork();
            if (canWork) {
                if (currentPosition == getPos()) {
                    moveToStartingPosition();
                    canWork = false;
                    return;
                }
                if (currentPosition != null && currentPosition != getPos()) {
                    if (canDestroy()) {
                        destroyBlock();
                    } else {
                        moveScanner();
                    }
                }
            } else {
                checkIfCanWork();
            }
            ticker = new Ticker(getWorld().rand.nextInt(3));
            IBlockState state = getWorld().getBlockState(getPos());
            getWorld().notifyBlockUpdate(getPos(), state, state, 3);
            markDirty();
        }
    }

    @SideOnly(Side.CLIENT)
    private void updateClient( ) {
        if (render != null) render.cleanUp();
        if (currentPosition != getPos()) {
            render = BoxRender.create(new Color(200, 30, 30, 30), new Vec3d(currentPosition), new Vec3d(currentPosition.add(1, 1, 1)), BoxRender.BoxMode.NORMAL);
            render.show();
        }

    }

    private void moveScanner( ) {
        currentPosition = getNextPosition(currentPosition);
        //SimpleRandomStuff.INSTANCE.logger.info(currentPosition.toString());
    }

    private void destroyBlock( ) {
        if (battery.getEnergyStored() >= FRMiner.BREAK_POWER && canDestroy()) {
            IBlockState blockState = getWorld().getBlockState(currentPosition);
            List<ItemStack> dropsList = blockState.getBlock().getDrops(getWorld(), currentPosition, blockState, 0);
            dropsList.addAll(drops);
            if (getWorld().destroyBlock(currentPosition, false)) {
                drops.addAll(dropsList);
            }
        }
    }

    private boolean canDestroy( ) {
        if (battery.getEnergyStored() >= FRMiner.SCAN_POWER) {
            if (getWorld().isAirBlock(currentPosition)) return false;
            IBlockState blockState = getWorld().getBlockState(currentPosition);
            Block block = blockState.getBlock();
            ItemStack stack = new ItemStack(block, 1, block.getMetaFromState(blockState));
            for (ItemStack ore : ores) {
                if (ItemStack.areItemStacksEqual(ore, stack)) {
                    return true;
                }
            }
        }
        return false;
    }

    private void moveToStartingPosition( ) {
        int currentPositionX = currentPosition.getX();
        int currentPositionZ = currentPosition.getZ();
        currentPosition = new BlockPos(currentPositionX - currentPositionX % 15, currentPosition.getY() - 1, currentPositionZ - currentPositionZ % 15);
    }

    private void checkIfCanWork( ) {
        if (getNextPosition(currentPosition).getY() <= 0 || battery.getEnergyStored() <= FRMiner.SCAN_POWER) {
            canWork = false;
            return;
        }

        for (int i = drops.size() - 1; i >= 0; i--) {
            ItemStack stack = ItemHandlerHelper.insertItem(inventory, drops.remove(i), false);
            if (stack != ItemStack.EMPTY) drops.add(stack);
        }

        canWork = drops.isEmpty();
    }

    private BlockPos getNextPosition(BlockPos currentPosition) {
        int x = currentPosition.getX();
        int y = currentPosition.getY();
        int z = currentPosition.getZ();
        if (Math.abs(x + 1) % 16 > 0) {
            return new BlockPos(x + 1, y, z);
        } else {
            x = x - 15;
        }
        if (Math.abs(z + 1) % 16 > 0) {
            return new BlockPos(x, y, z + 1);
        } else {
            z = z - 15;
        }
        if (y - 1 > 0) {
            return new BlockPos(x, y - 1, z);
        } else {
            canWork = false;
            return getPos();
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        if (tag.hasKey("currentPosition")) currentPosition = BlockPos.fromLong(tag.getLong("currentPosition"));
        else currentPosition = getPos();
        ticker.readFromNBT(tag);
        battery.setEnergy(tag.getInteger("energyStored"));
        inventory.deserializeNBT(tag.getCompoundTag("inventory"));
        if (tag.hasKey("drops")) {
            ItemStackHandler handler = new ItemStackHandler(20);
            handler.deserializeNBT(tag.getCompoundTag("drops"));
            for (int i = 0; i < handler.getSlots(); i++) {
                drops.add(handler.getStackInSlot(i));
            }
        }
        super.readFromNBT(tag);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        if (currentPosition != null && currentPosition != getPos())
            tag.setLong("currentPosition", currentPosition.toLong());
        ticker.writeToNBT(tag);
        tag.setInteger("energyStored", battery.getEnergyStored());
        tag.setTag("inventory", inventory.serializeNBT());
        ItemStackHandler handler = new ItemStackHandler(drops.size());
        if (!drops.isEmpty()) {
            for (ItemStack drop : drops) {
                ItemHandlerHelper.insertItem(handler, drop, false);
            }
            tag.setTag("drops", handler.serializeNBT());
        }
        return super.writeToNBT(tag);
    }

    @Nullable
    @Override
    public SPacketUpdateTileEntity getUpdatePacket( ) {
        return new SPacketUpdateTileEntity(getPos(), 0, getUpdateTag());
    }

    @Override
    public NBTTagCompound getUpdateTag( ) {
        return writeToNBT(new NBTTagCompound());
    }

    @Override
    public void invalidate( ) {
        if (render != null) //noinspection MethodCallSideOnly
            render.cleanUp();
        super.invalidate();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity nbt) {
        handleUpdateTag(nbt.getNbtCompound());
    }

    @Override
    public void handleUpdateTag(NBTTagCompound tag) {
        readFromNBT(tag);
    }

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
        if (capability.equals(CapabilityEnergy.ENERGY)) {
            return true;
        } else if (capability.equals(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)) {
            return true;
        }
        return super.hasCapability(capability, facing);
    }

    @Nullable
    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
        if (capability.equals(CapabilityEnergy.ENERGY)) {
            return CapabilityEnergy.ENERGY.cast(battery);
        } else if (capability.equals(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)) {
            return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(inventory);
        }
        return super.getCapability(capability, facing);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public Gui createGui(InventoryPlayer player, World world, BlockPos pos) {
        return new GuiMiner(player, this);
    }

    @Override
    public Container createContainer(InventoryPlayer player, World world, BlockPos pos) {
        return new ContainerMiner(player, this);
    }
}
