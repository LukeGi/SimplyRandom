package bluemonster122.mods.simplethings.pump;

import bluemonster122.mods.simplethings.core.block.IHaveTank;
import bluemonster122.mods.simplethings.core.block.TileST;
import com.google.common.collect.ImmutableMap;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.IFluidBlock;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.wrappers.BlockLiquidWrapper;
import net.minecraftforge.fluids.capability.wrappers.BlockWrapper;
import net.minecraftforge.fluids.capability.wrappers.FluidBlockWrapper;

import java.util.*;

public class TileFloodGate extends TileST implements IHaveTank, ITickable {
    public FluidTank tank = createTank();
    TreeMap<Integer, Deque<BlockPos>> layersToFill = new TreeMap<>();
    Set<BlockPos> visited = new HashSet<>();
    Deque<BlockPos> fluidBlocks = new LinkedList<>();
    EnumSet<EnumFacing> blockFaces;

    @Override
    public Map<Capability, Capability> getCaps( ) {
        return ImmutableMap.of(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast((IFluidHandler) tank));
    }

    @Override
    public NBTTagCompound writeChild(NBTTagCompound tag) {
        return tag;
    }

    @Override
    public NBTTagCompound readChild(NBTTagCompound tag) {
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
        return new FluidTank(1000);
    }

    @Override
    public void update( ) {
        if (getWorld().isRemote) {
            updateClient();
        } else {
//            if (getWorld().getTotalWorldTime() % 20 == 0)
            updateServer();
            // TODO: remove
            tank.setFluid(new FluidStack(FluidRegistry.WATER, 1000));
        }
    }

    private void updateServer( ) {
        if (blockFaces == null) {
            blockFaces = EnumSet.noneOf(EnumFacing.class);
            for (EnumFacing value : EnumFacing.VALUES) {
                if (!isValidBlock(getPos().offset(value))){
                    blockFaces.add(value);
                }
            }
        }
        if (hasFluid()) {

        }
    }

    private void redoQueues( ) {
        visited.clear();
        layersToFill.clear();
        fluidBlocks.clear();

        queueAdjacent(pos);

        expandQueue();
    }

    private void expandQueue( ) {
        if (tank.getFluid() == null) {
            return;
        }
        while (!fluidBlocks.isEmpty()) {
            Deque<BlockPos> fluidsToExpand = fluidBlocks;
            fluidBlocks = new LinkedList<>();

            for (BlockPos index : fluidsToExpand) {
                queueAdjacent(index);
            }
        }
    }

    private void queueAdjacent(BlockPos pos) {
        if (tank.getFluid() == null) {
            return;
        }
        for (EnumFacing face : EnumFacing.VALUES) {
            if (face != EnumFacing.UP && !blockFaces.contains(face)) {
                queueForFilling(pos.offset(face));
            }
        }
    }

    private void queueForFilling(BlockPos pos) {
        if (pos.getY() < 0 || pos.getY() > 255) {
            return;
        }
        if (visited.add(pos)) {
            if ((pos.getX() - getPos().getX()) * (pos.getX() - getPos().getX()) + (pos.getZ() - getPos().getZ()) * (pos.getZ() - getPos().getZ()) > 4096) {
                return;
            }
            if (isValidBlock(pos)) layersToFill.get(pos.getY()).addLast(pos);
        }
    }

    private boolean isValidBlock(BlockPos pos) {
        IBlockState state = getWorld().getBlockState(pos);
        Block block = state.getBlock();
        IFluidHandler handler;
        if (block instanceof IFluidBlock) {
            handler = new FluidBlockWrapper((IFluidBlock) block, getWorld(), pos);
        } else if (block instanceof BlockLiquid) {
            handler = new BlockLiquidWrapper((BlockLiquid) block, getWorld(), pos);
        } else {
            handler = new BlockWrapper(block, getWorld(), pos);
        }
        FluidStack drain = handler.drain(Integer.MAX_VALUE, false);
        return !(drain != null && drain.amount >= 1000) && !(state.getMaterial().isSolid() || !state.getMaterial().isReplaceable() || state.getMaterial() != Material.AIR);
    }

    private boolean hasFluid( ) {
        return tank.getFluid() != null && tank.getFluidAmount() >= 1000;
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
