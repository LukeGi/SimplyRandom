package bluemonster122.mods.simplethings.pump;

import bluemonster122.mods.simplethings.client.renderer.BoxRender;
import bluemonster122.mods.simplethings.core.block.IHaveTank;
import bluemonster122.mods.simplethings.core.block.TileST;
import bluemonster122.mods.simplethings.core.energy.BatteryST;
import bluemonster122.mods.simplethings.core.energy.IEnergyRecieverST;
import com.google.common.collect.ImmutableMap;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
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
import java.util.ArrayList;
import java.util.Map;
import java.util.Stack;
import java.util.function.Predicate;

public class TilePump extends TileST implements IEnergyRecieverST, IHaveTank, ITickable {
    public FluidTank tank = createTank();
    public BatteryST battery = createBattery();
    Stack<BlockPos> fluids = new Stack<>();
    private int probe = 0;
    private boolean hitFluid = false;
    private BoxRender pipe;

    @Override
    public Map<Capability, Capability> getCaps() {
        return ImmutableMap.of(
                CapabilityEnergy.ENERGY, CapabilityEnergy.ENERGY.cast((IEnergyStorage) tank),
                CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast((IFluidHandler) tank)
        );
    }

    @Override
    public NBTTagCompound writeChild(NBTTagCompound tag) {
        tag.setInteger("probe", probe);
        tag.setBoolean("hit", hitFluid);
        return tag;
    }

    @Override
    public NBTTagCompound readChild(NBTTagCompound tag) {
        probe = tag.getInteger("probe");
        hitFluid = tag.getBoolean("hit");
        return tag;
    }

    /**
     * Gets the Tile's current battery.
     *
     * @return The Tile's current battery.
     */
    @Override
    public BatteryST getBattery() {
        return battery;
    }

    /**
     * Creates a new Battery for the Tile.
     *
     * @return a new Battery for the Tile.
     */
    @Override
    public BatteryST createBattery() {
        return new BatteryST(100);
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
     * Gets the Tile's current Tank.
     *
     * @return The Tile's current Tank.
     */
    @Override
    public FluidTank getTank() {
        return tank;
    }

    /**
     * Creates a new Tank for the Tile.
     *
     * @return a new Tank for the Tile.
     */
    @Override
    public FluidTank createTank() {
        return new FluidTank(1000);
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

    @Override
    public void update() {
        if (getWorld().isRemote) {
            //noinspection MethodCallSideOnly
            updateClient();
        } else {
            updateServer();
            sendUpdate();
        }
    }

    private void updateServer() {
        if (getWorld().getTotalWorldTime() % (hitFluid ? 10 : 100) == 0) {
            if (tank.getFluid() == null) {
                BlockPos down = getPos().down(probe);
                IBlockState state = getWorld().getBlockState(down);
                Block block = state.getBlock();
                if (!fluids.isEmpty()) {
                    BlockPos current = fluids.pop();
                    state = getWorld().getBlockState(current);
                    block = state.getBlock();
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
                } else if (block instanceof IFluidBlock) {
                    hitFluid = true;
                    scanForBlock(down);
                    // Pump fluid
                } else if (block instanceof BlockLiquid) {
                    hitFluid = true;
                    if (state.getMaterial().equals(Material.WATER)) {
                        int count = 0;
                        for (EnumFacing facing : EnumFacing.HORIZONTALS) {
                            if (world.getBlockState(down.offset(facing)).getMaterial().equals(Material.WATER)) {
                                count++;
                            }
                        }
                        if (count >= 2) {
                            fluids.add(down);
                            return;
                        }
                    }
                    scanForBlock(down);
                } else {
                    hitFluid = false;
                    state = getWorld().getBlockState(down.down());
                    block = state.getBlock();
                    if (block instanceof IFluidBlock || block instanceof BlockLiquid || state.equals(Blocks.AIR.getDefaultState()))
                        probe += 1;
                    else {
                        probe = -1;
                        hitFluid = true;
                    }
                }
            } else {
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
        }
    }

    private void scanForBlock(BlockPos start) {
        ArrayList<BlockPos> visited = new ArrayList<>();
        Stack<BlockPos> toScan = new Stack<>();
        Block block = getWorld().getBlockState(start).getBlock();
        Predicate<Block> isValid = b -> b.equals(block);
        toScan.add(start);

        while (!toScan.isEmpty()) {
            BlockPos pos = toScan.pop();
            visited.add(pos);
            for (EnumFacing facing : EnumFacing.HORIZONTALS) {
                BlockPos element = pos.offset(facing);
                if (!visited.contains(element) && !toScan.contains(element) && !fluids.contains(element)) {
                    toScan.push(element);
                }
            }
            Block b = getWorld().getBlockState(pos).getBlock();
            if (isValid.test(b)) {
                fluids.push(pos);
            }
        }
        fluids.sort((o1, o2) -> {
            double v = o1.distanceSq(start);
            double u = o2.distanceSq(start);
            return v > u ? 1 : v == u ? 0 : -1;
        });
    }

    @SideOnly(Side.CLIENT)
    private void updateClient() {
        if (hitFluid) return;
        if (pipe != null) pipe.cleanUp();
        if (probe == -1) return;
        pipe = BoxRender.create(
                new Color(0, 50, 120, 40),
                new Vec3d(getPos().getX() + 0.35, getPos().getY() + 0.01, getPos().getZ() + 0.35f),
                new Vec3d(getPos().getX() + 0.65, getPos().getY() - probe + .5 - (1 / 100f) * (getWorld().getTotalWorldTime() % 100), getPos().getZ() + 0.65),
                BoxRender.BoxMode.ENDLESS
        );
        pipe.show();
    }

    @Override
    public void invalidate() {
        if (getWorld().isRemote)
            //noinspection MethodCallSideOnly
            cleanup();
        super.invalidate();
    }

    @SideOnly(Side.CLIENT)
    public void cleanup() {
        if (pipe != null)
            pipe.cleanUp();
    }
}
