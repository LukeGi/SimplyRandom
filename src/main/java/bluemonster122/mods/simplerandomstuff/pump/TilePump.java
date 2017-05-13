package bluemonster122.mods.simplerandomstuff.pump;

import bluemonster122.mods.simplerandomstuff.SimpleRandomStuff;
import bluemonster122.mods.simplerandomstuff.client.renderer.BoxRender;
import bluemonster122.mods.simplerandomstuff.core.block.IHaveTank;
import bluemonster122.mods.simplerandomstuff.core.energy.BatteryST;
import bluemonster122.mods.simplerandomstuff.core.energy.IEnergyRecieverST;
import bluemonster122.mods.simplerandomstuff.util.Ticker;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.fluids.BlockFluidBase;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.*;

public class TilePump extends TileEntity implements ITickable, IEnergyRecieverST, IHaveTank {
    private FluidTank tank = createTank();
    private BatteryST battery = createBattery();
    private TreeMap<Integer, Deque<BlockPos>> layersToPump = new TreeMap<>();
    private Set<BlockPos> visited = new HashSet<>();
    private Deque<BlockPos> transitionBlocks = new LinkedList<>();
    private boolean canWork = false;
    private boolean hitFluid = false;
    private boolean restartFlag = false;
    private int currentLayer = -1;
    private Ticker ticker = new Ticker((new Random()).nextInt(200));
    private BoxRender pipe;

    @Override
    public BatteryST getBattery( ) {
        return battery;
    }

    @Override
    public void setBattery(BatteryST battery) {
        this.battery = battery;
    }

    @Override
    public BatteryST createBattery( ) {
        return new BatteryST(100);
    }

    @Override
    public FluidTank getTank( ) {
        return tank;
    }

    @Override
    public void setTank(FluidTank tank) {
        this.tank = tank;
    }

    @Override
    public FluidTank createTank( ) {
        return new FluidTank(Fluid.BUCKET_VOLUME) {
            @Override
            public boolean canFill( ) {
                return false;
            }
        };
    }

    @Override
    public void update( ) {
        if (getWorld().isRemote) updateClient();
        else {
            ticker.tick();
            if (ticker.time_up()) updateServer();
            IBlockState state = getWorld().getBlockState(getPos());
            getWorld().notifyBlockUpdate(getPos(), state, state, 3);
            markDirty();
        }
    }

    private void updateServer( ) {
        battery.setEnergy(100);

        canWork = battery.getEnergyStored() > FRPump.INSTANCE.getPumpEnergy();
        if (currentLayer == -1) currentLayer = getPos().getY();

        if (restartFlag) {
            currentLayer = getPos().getY();
            hitFluid = false;
            restartFlag = false;
        }

        if (canWork) {
            if (tank.getFluidAmount() == 0) {
                // Scan
                if (layersToPump.isEmpty()) {
                    currentLayer--;
                    hitFluid = false;
                    BlockPos currentScanPos = new BlockPos(getPos().getX(), currentLayer, getPos().getZ());
                    IBlockState state = getWorld().getBlockState(currentScanPos);
                    Block block = state.getBlock();

                    SimpleRandomStuff.INSTANCE.logger.info(currentScanPos);
                    if (isPumpable(currentScanPos)) {
                        hitFluid = true;
                        refreshQueues(currentScanPos, state, block);
                        addToPump(currentScanPos);
                        ticker.reset(10);
                        return;
                    }
                    if (getWorld().isAirBlock(currentScanPos)) ticker.reset(200);
                    else {
                        ticker.reset(150);
                        hitFluid = true;
                        restartFlag = true;
                    }
                } else {
                    //Pump
                    hitFluid = true;
                    currentLayer = layersToPump.firstKey();
                    BlockPos currentScanPos = getNextSpot(true);
                    if (currentScanPos != null) {
                        addAdjacentToQueues(currentScanPos);
                        if (isPumpable(currentScanPos)) {
                            IFluidHandler block = FluidUtil.getFluidHandler(getWorld(), currentScanPos, EnumFacing.UP);
                            tank.fillInternal(block.drain(Fluid.BUCKET_VOLUME, true), true);
                        }
                        ticker.reset(100);
                    }
                }
            } else {
                BlockPos up = getPos().up();
                TileEntity tileEntity = getWorld().getTileEntity(up);
                if (tileEntity != null && tileEntity.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, EnumFacing.DOWN)) {
                    IFluidHandler fluidHandler = tileEntity.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, EnumFacing.DOWN);
                    if (fluidHandler != null) {
                        FluidUtil.tryFluidTransfer(fluidHandler, tank, Fluid.BUCKET_VOLUME, true);
                        IBlockState state = getWorld().getBlockState(up);
                        getWorld().notifyBlockUpdate(up, state, state, 11);
                        tileEntity.markDirty();
                    }
                }
            }
        } else {
            ticker.reset(500);
        }
    }

    @Nullable
    private BlockPos getNextSpot(boolean remove_value) {
        if (layersToPump.isEmpty()) {
            return null;
        }

        Deque<BlockPos> top_layer = layersToPump.lastEntry().getValue();

        if (top_layer == null) {
            return null;
        }

        if (top_layer.isEmpty()) {
            layersToPump.pollLastEntry();
            return getNextSpot(remove_value);
        }
        return remove_value ? top_layer.pollFirst() : top_layer.peekFirst();
    }

    private void refreshQueues(BlockPos currentScanPos, IBlockState state, Block block) {
        visited.clear();
        layersToPump.clear();
        transitionBlocks.clear();

        addAdjacentToQueues(currentScanPos);

        populateQueues();
    }

    private void populateQueues( ) {
        if (tank.getFluid() == null) {
            return;
        }
        while (!transitionBlocks.isEmpty()) {
            Deque<BlockPos> fluidsToExpand = transitionBlocks;
            transitionBlocks = new LinkedList<>();

            for (BlockPos index : fluidsToExpand) {
                addAdjacentToQueues(index);
            }
        }
    }

    private void addAdjacentToQueues(BlockPos currentScanPos) {
        if (tank.getFluid() != null) {
            return;
        }
        for (EnumFacing face : EnumFacing.VALUES) {
            queueForPumping(currentScanPos.offset(face));
        }
    }

    private void queueForPumping(BlockPos pos) {
        if (pos.getY() < 0 || pos.getY() > 255) {
            return;
        }
        if (visited.add(pos)) {
            if (pos.getDistance(getPos().getX(), pos.getY(), getPos().getZ()) > 4096) {
                return;
            }
            IFluidHandler handler = FluidUtil.getFluidHandler(getWorld(), pos, null);
            if (handler != null) transitionBlocks.add(pos);
            if (isFlowing(pos)) {
                transitionBlocks.add(pos);
            }
            if (isPumpable(pos)) {
                addToPump(pos);
            }
        }
    }

    private void addToPump(BlockPos pos) {
        layersToPump.computeIfAbsent(pos.getY(), k -> new LinkedList<>()).addLast(pos);
    }

    private boolean isFlowing(BlockPos pos) {
        IBlockState state = getWorld().getBlockState(pos);
        Block block = state.getBlock();
        if (block instanceof BlockLiquid) {
            return state.getValue(BlockLiquid.LEVEL) != 0;
        } else if (block instanceof BlockFluidBase) {
            return state.getValue(BlockFluidBase.LEVEL) != 0;
        }
        return false;
    }

    private boolean isPumpable(BlockPos currentScanPos) {
        IBlockState state = getWorld().getBlockState(currentScanPos);
        return state.getBlock() instanceof BlockFluidBase || state.getBlock() instanceof BlockLiquid;
    }

    @SideOnly(Side.CLIENT)
    private void updateClient( ) {
        if (pipe != null) pipe.cleanUp();
        if (currentLayer != getPos().getY() && currentLayer != -1 && !hitFluid) {
            pipe = BoxRender.create(new Color(40, 40, 40, 150), new Vec3d(getPos().getX() + 0.35f, getPos().getY() + 0.01f, getPos().getZ() + 0.35f), new Vec3d(getPos().getX() + 0.65f, currentLayer + .5f - ticker.percent_gone(), getPos().getZ() + 0.65f), BoxRender.BoxMode.ENDLESS);
            pipe.show();
        } else if (hitFluid) {
            pipe = BoxRender.create(new Color(40, 40, 40, 150), new Vec3d(getPos().getX() + 0.35f, getPos().getY() + 0.01f, getPos().getZ() + 0.35f), new Vec3d(getPos().getX() + 0.65f, currentLayer + .5f, getPos().getZ() + 0.65f), BoxRender.BoxMode.ENDLESS);
            pipe.show();
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        currentLayer = tag.getInteger("currentLayer");
        canWork = tag.getBoolean("canWork");
        hitFluid = tag.getBoolean("hitFluid");
        battery.setEnergy(tag.getInteger("energy"));
        ticker.readFromNBT(tag);
        tank.readFromNBT(tag);
        super.readFromNBT(tag);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        tag.setInteger("currentLayer", currentLayer);
        tag.setBoolean("canWork", canWork);
        tag.setBoolean("hitFluid", hitFluid);
        tag.setInteger("energy", battery.getEnergyStored());
        tank.writeToNBT(tag);
        ticker.writeToNBT(tag);
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
        if (pipe != null) pipe.cleanUp();
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
        if (capability.equals(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)) {
            return true;
        } else if (capability.equals(CapabilityEnergy.ENERGY)) {
            return true;
        } else {
            return super.hasCapability(capability, facing);
        }
    }

    @Nullable
    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
        if (capability.equals(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)) {
            return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(getTank());
        } else if (capability.equals(CapabilityEnergy.ENERGY)) {
            return CapabilityEnergy.ENERGY.cast(getBattery());
        } else {
            return super.getCapability(capability, facing);
        }
    }
}
