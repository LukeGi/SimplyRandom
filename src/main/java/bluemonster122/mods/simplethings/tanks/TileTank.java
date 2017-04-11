package bluemonster122.mods.simplethings.tanks;

import bluemonster122.mods.simplethings.tileentity.core.TileEntityST;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;

import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class TileTank extends TileEntityST {
    public FluidTank tank = new FluidTank(32000);
    public int tier = 0;

    public NBTTagCompound writeTank(NBTTagCompound compound) {
        tank.writeToNBT(compound);
        return compound;
    }

    public NBTTagCompound readTank(NBTTagCompound compound) {
        createTank();
        tank.readFromNBT(compound);
        return compound;
    }

    private void createTank() {
        try {
            tier = (byte) world.getBlockState(pos).getValue(BlockTank.VARIANT).getMeta();
        } catch (Exception ignore) {
        }
        tank.setCapacity((8 << tier) * 1000);
    }

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate) {
        return oldState.getBlock() != newSate.getBlock();
    }

    @Override
    public Set<Consumer<NBTTagCompound>> getAllWrites() {
        return getMinWrites();
    }

    @Override
    public Set<Consumer<NBTTagCompound>> getAllReads() {
        return getMinReads();
    }

    @Override
    public Map<Capability, Supplier<Capability>> getCaps() {
        return ImmutableMap.of(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(tank));
    }

    @Override
    public Set<Consumer<NBTTagCompound>> getMinWrites() {
        return ImmutableSet.of(super::writeNBTLegacy, this::writeTank);
    }

    @Override
    public Set<Consumer<NBTTagCompound>> getMinReads() {
        return ImmutableSet.of(super::readNBTLegacy, this::readTank);
    }
}
