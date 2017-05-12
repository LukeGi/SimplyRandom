package bluemonster122.mods.simplerandomstuff.tanks;

import bluemonster122.mods.simplerandomstuff.core.block.IHaveTank;
import bluemonster122.mods.simplerandomstuff.core.block.TileST;
import com.google.common.collect.ImmutableMap;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

import java.util.Map;

public class TileTank extends TileST implements IHaveTank {
    public int tier = 0;
    public FluidTank tank = createTank();

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
     * Creates a new Tank for the Tile.
     *
     * @return a new Tank for the Tile.
     */
    @Override
    public FluidTank createTank( ) {
        try {
            tier = (byte) world.getBlockState(pos).getValue(BlockTank.VARIANT).getMeta();
        } catch (Exception ignore) {
        }
        return new FluidTank((8 << tier) * 1000);
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
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate) {
        return oldState.getBlock() != newSate.getBlock();
    }

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
}