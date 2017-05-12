package bluemonster122.mods.simplerandomstuff.pump;

import bluemonster122.mods.simplerandomstuff.client.renderer.BoxRender;
import bluemonster122.mods.simplerandomstuff.core.block.IHaveTank;
import bluemonster122.mods.simplerandomstuff.core.block.TileST;
import bluemonster122.mods.simplerandomstuff.core.energy.BatteryST;
import bluemonster122.mods.simplerandomstuff.core.energy.IEnergyRecieverST;
import com.google.common.collect.ImmutableMap;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.IFluidBlock;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.wrappers.BlockLiquidWrapper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.awt.*;
import java.util.*;
import java.util.function.Predicate;

import static bluemonster122.mods.simplerandomstuff.pump.FRPump.INSTANCE;

public class TilePump extends TileST implements IEnergyRecieverST, IHaveTank, ITickable {
    private static final Predicate<IBlockState> isFluid = state -> state.getBlock() instanceof IFluidBlock || state.getBlock() instanceof BlockLiquid;
    private static final Predicate<IBlockState> isValid = state -> isFluid.or(s -> s.getMaterial().equals(Material.AIR)).test(state);
    public FluidTank tank = createTank();
    public BatteryST battery = createBattery();
    Deque<BlockPos> fluids = new ArrayDeque<>();
    private int probe = 0;
    private boolean hitFluid = false;
    private boolean canWork = false;
    private BoxRender pipe;

    @Override
    public Map<Capability, Capability> getCaps( ) {
        return ImmutableMap.of(CapabilityEnergy.ENERGY, CapabilityEnergy.ENERGY.cast((IEnergyStorage) battery), CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast((IFluidHandler) tank));
    }

    @Override
    public NBTTagCompound writeChild(NBTTagCompound tag) {
        tag.setInteger("probe", probe);
        tag.setBoolean("hit", hitFluid);
        tag.setBoolean("canWork", canWork);
        return tag;
    }

    @Override
    public NBTTagCompound readChild(NBTTagCompound tag) {
        probe = tag.getInteger("probe");
        hitFluid = tag.getBoolean("hit");
        canWork = tag.getBoolean("canWork");
        return tag;
    }

    /**
     * Gets the Tile's current battery.
     *
     * @return The Tile's current battery.
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
        return new BatteryST(100);
    }

    /**
     * Gets the Tile's current Tank.
     *
     * @return The Tile's current Tank.
     */
    @Override
    public FluidTank getTank( ) {
        return tank;
    }

    /**
     * Sets the given ItemStackHandler to be the Tile's Tank.
     *
     * @param tank new Inventory.
     */
    @Override
    public void setTank(FluidTank tank) {
        this.tank = tank;
    }

    /**
     * Creates a new Tank for the Tile.
     *
     * @return a new Tank for the Tile.
     */
    @Override
    public FluidTank createTank( ) {
        return new FluidTank(1000);
    }

    @Override
    public void update( ) {
        if (getWorld().isRemote) {
            //noinspection MethodCallSideOnly
            updateClient();
        } else {
            if (getWorld().getTotalWorldTime() % (hitFluid && canWork ? 10 : 100) == 0) updateServer();
            sendUpdate();
        }
    }

    private void updateServer( ) {
        battery.setEnergy(100);
        canWork = battery.getEnergyStored() >= INSTANCE.getPumpEnergy();
        if (!canWork) return;

        if (tank.getFluid() != null) {
            attemptPushFluid();
        } else {
            if (probe == 0) {
                probe++;
                return;
            }
            BlockPos down = getPos().down(probe);
            IBlockState state = getWorld().getBlockState(down);
            Block block = state.getBlock();
            if (!fluids.isEmpty()) {
                attemptPump();
            } else if (block instanceof IFluidBlock) {
                hitFluid = true;
                scanForBlock(down);
                // Pump fluid
            } else if (block instanceof BlockLiquid) {
                hitFluid = true;
                if (state.getMaterial().equals(Material.WATER) && hasInfinate(down)) {
                    fluids.add(down);
                    return;
                }
                scanForBlock(down);
            } else {
                hitFluid = !isValid.test(state);
                probe = isValid.test(state) ? probe + 1 : -1;
            }
        }
    }

    private boolean hasInfinate(BlockPos pos) {
        int count = 0;
        for (EnumFacing facing : EnumFacing.HORIZONTALS) {
            if (world.getBlockState(pos.offset(facing)).getMaterial().equals(Material.WATER)) {
                count++;
            }
        }
        return count > 1;
    }

    private void attemptPump( ) {
        BlockPos current = fluids.pop();
        IBlockState state = getWorld().getBlockState(current);
        Block block = state.getBlock();
        battery.extractEnergy(INSTANCE.getPumpEnergy(), false);
        if (block instanceof IFluidBlock) {
            IFluidBlock fluid = (IFluidBlock) block;
            if (fluid.canDrain(world, current)) {
                FluidStack fluidStack = fluid.drain(getWorld(), current, true);
                tank.fill(fluidStack, true);
            }
        } else if (block instanceof BlockLiquid) {
            BlockLiquidWrapper liquid = new BlockLiquidWrapper((BlockLiquid) block, getWorld(), current);
            tank.fill(liquid.drain(1000, true), true);
        }
    }

    private void attemptPushFluid( ) {
        BlockPos up = getPos().up();
        TileEntity tileEntity = getWorld().getTileEntity(up);
        if (tileEntity != null && tileEntity.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, EnumFacing.DOWN)) {
            IFluidHandler fluidHandler = tileEntity.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, EnumFacing.DOWN);
            if (fluidHandler != null) {
                FluidUtil.tryFluidTransfer(fluidHandler, tank, 1000, true);
                IBlockState state = getWorld().getBlockState(up);
                getWorld().notifyBlockUpdate(up, state, state, 11);
            }
        }
    }

    private void scanForBlock(BlockPos start) {
        ArrayList<BlockPos> visited = new ArrayList<>();
        Deque<BlockPos> toScan = new LinkedList<>();
        Block block = getWorld().getBlockState(start).getBlock();
        Predicate<Block> isValid = b -> b.equals(block);
        toScan.add(start);

        while (!toScan.isEmpty()) {
            BlockPos pos = toScan.pop();
            visited.add(pos);
            for (EnumFacing facing : EnumFacing.HORIZONTALS) {
                BlockPos element = pos.offset(facing);
                if (!visited.contains(element) && !toScan.contains(element) && !fluids.contains(element) && isValid.test(getWorld().getBlockState(element).getBlock()))
                    toScan.push(element);
            }
            fluids.push(pos);
        }
    }

    @SideOnly(Side.CLIENT)
    private void updateClient( ) {
        if (probe == -1) {
            if (pipe != null) pipe.cleanUp();
            return;
        } else {
            if (probe > 0) {
                if (pipe != null) pipe.cleanUp();
                if (canWork && hitFluid) {
                    createPipe(getPos().getY() - probe + 14 / 16f);
                } else {
                    createPipe(getPos().getY() - probe + 30 / 16f - ((1 / 100f) * (getWorld().getTotalWorldTime() % 100)));
                }
            }
        }
    }

    @SideOnly(Side.CLIENT)
    private void createPipe(double y) {
        pipe = BoxRender.create(new Color(40, 40, 40, 150), new Vec3d(getPos().getX() + 0.35, getPos().getY() + 0.01, getPos().getZ() + 0.35f), new Vec3d(getPos().getX() + 0.65, y, getPos().getZ() + 0.65), BoxRender.BoxMode.ENDLESS);
        pipe.show();
    }

    @Override
    public void invalidate( ) {
        if (getWorld().isRemote) //noinspection MethodCallSideOnly
            cleanup();
        super.invalidate();
    }

    @SideOnly(Side.CLIENT)
    public void cleanup( ) {
        if (pipe != null) pipe.cleanUp();
    }
}
