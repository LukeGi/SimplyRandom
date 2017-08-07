package bluemonster122.mods.simplerandomstuff.pump;

import bluemonster122.mods.simplerandomstuff.core.block.IHaveTank;
import bluemonster122.mods.simplerandomstuff.core.block.TileST;
import bluemonster122.mods.simplerandomstuff.util.Ticker;
import com.google.common.collect.ImmutableMap;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDynamicLiquid;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.*;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.wrappers.FluidBlockWrapper;

import javax.annotation.Nullable;
import java.util.*;

public class TileFloodGate extends TileST implements IHaveTank, ITickable {
    public FluidTank tank = createTank();
    private TreeMap<Integer, Deque<BlockPos>> layersToFill = new TreeMap<>();
    private Set<BlockPos> visited = new HashSet<>();
    private Deque<BlockPos> fluidBlocks = new LinkedList<>();
    private Ticker ticker = new Ticker((new Random()).nextInt(200));

    @Override
    public Map<Capability, Capability> getCaps( ) {
        return ImmutableMap.of(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast((IFluidHandler) tank));
    }

    @Override
    public NBTTagCompound writeChild(NBTTagCompound tag) {
        ticker.writeToNBT(tag);
        return tag;
    }

    @Override
    public NBTTagCompound readChild(NBTTagCompound tag) {
        ticker.readFromNBT(tag);
        return tag;
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
        return new FluidTank(Fluid.BUCKET_VOLUME);
    }

    @Override
    public void update( ) {
        if (getWorld().isRemote) {
            updateClient();
        } else {
            ticker.tick();
            if (ticker.time_up()) {
                updateServer();
                ticker.reset(10);
            }
        }
    }

    private void updateServer( ) {
        FluidStack fluidOut = tank.drain(Fluid.BUCKET_VOLUME, false);
        if (fluidOut != null) {
            if (getWorld().provider.getDimension() == -1) {
                tank.drain(Fluid.BUCKET_VOLUME, true);
                return;
            }
            if (ticker.time_up()) {
                if (layersToFill.isEmpty()) {
                    if (!visited.isEmpty()) ticker.reset(400);
                    refreshQueues();
                }
                BlockPos posToFill = getNextSpot(true);
                if (posToFill != null && placeFluidAt(posToFill)) {
                    ticker.reset(200);
                }
            }

        }
    }

    private boolean placeFluidAt(BlockPos fillPos) {
        if (isValidBlock(fillPos) || isFlowing(fillPos)) {
            boolean placed = false;
            Block b = tank.getFluid().getFluid().getBlock();

            if (b instanceof BlockFluidBase) {
                IFluidHandler handler = new FluidBlockWrapper((BlockFluidBase) b, getWorld(), fillPos);
                handler.fill(tank.getFluid(), true);
                placed = true;
            } else if (b instanceof BlockLiquid) {
                BlockDynamicLiquid block = BlockLiquid.getFlowingBlock(b.getDefaultState().getMaterial());
                world.setBlockState(fillPos, block.getDefaultState(), 3);
                placed = true;
            }

            if (placed) {
                tank.drain(Fluid.BUCKET_VOLUME, true);
                addAdjacentToQueues(fillPos);
                populateQueues();
            }

            return placed;
        }
        return false;
    }

    /**
     * This will get the next block position that the flood gate should fill with the fluid contained within the tank.
     *
     * @param remove_value this should be true, if you want the method to poll the list, it will remove the value from being usable again.
     * @return The next position that should be filled by the flodo gate.
     */
    @Nullable
    private BlockPos getNextSpot(boolean remove_value) {
        if (layersToFill.isEmpty()) {
            return null;
        }

        Deque<BlockPos> bottom_layer = layersToFill.firstEntry().getValue();

        if (bottom_layer == null) {
            return null;
        }

        if (bottom_layer.isEmpty()) {
            layersToFill.pollFirstEntry();
        }
        return remove_value ? bottom_layer.pollFirst() : bottom_layer.peekFirst();
    }

    private void refreshQueues( ) {
        visited.clear();
        layersToFill.clear();
        fluidBlocks.clear();

        addAdjacentToQueues(pos);

        populateQueues();
    }

    private void populateQueues( ) {
        if (tank.getFluid() == null) {
            return;
        }
        while (!fluidBlocks.isEmpty()) {
            Deque<BlockPos> fluidsToExpand = fluidBlocks;
            fluidBlocks = new LinkedList<>();

            for (BlockPos index : fluidsToExpand) {
                addAdjacentToQueues(index);
            }
        }
    }

    private void addAdjacentToQueues(BlockPos pos) {
        if (tank.getFluid() == null) {
            return;
        }
        for (EnumFacing face : EnumFacing.VALUES) {
            queueForFilling(pos.offset(face));
        }
    }

    private void queueForFilling(BlockPos pos) {
        if (pos.getY() < 0 || pos.getY() > 255 || pos.getY() > getPos().getY()) {
            return;
        }
        if (visited.add(pos)) {
            if (pos.getDistance(getPos().getX(), pos.getY(), getPos().getZ()) > 4096) {
                return;
            }
            IFluidHandler handler = FluidUtil.getFluidHandler(getWorld(), pos, null);
            if (handler != null) {
                FluidStack stack = handler.drain(Fluid.BUCKET_VOLUME, false);
                if (stack != null) {
                    if (stack.getFluid() == tank.getFluid().getFluid()) {
                        fluidBlocks.add(pos);
                    }
                }
            }
            if (isFlowing(pos)) {
                addToFill(pos);
                fluidBlocks.add(pos);
            }
            if (isValidBlock(pos)) {
                addToFill(pos);
            }
        }
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

    private void addToFill(BlockPos pos) {
        layersToFill.computeIfAbsent(pos.getY(), k -> new LinkedList<>()).addLast(pos);
    }

    private boolean isValidBlock(BlockPos pos) {
        IBlockState state = getWorld().getBlockState(pos);
        return (state.getMaterial().isReplaceable() && FluidUtil.getFluidHandler(getWorld(), pos, null) == null) || state.getMaterial() == Material.AIR;
    }

    private void updateClient( ) {
        /* NO OPERATION */
    }

    @Override
    public void invalidate( ) {
        super.invalidate();
        layersToFill.clear();
        fluidBlocks.clear();
        visited.clear();
    }
}
