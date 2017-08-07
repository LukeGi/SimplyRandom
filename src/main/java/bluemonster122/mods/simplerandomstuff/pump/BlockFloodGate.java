package bluemonster122.mods.simplerandomstuff.pump;

import bluemonster122.mods.simplerandomstuff.core.block.BlockSRS;
import bluemonster122.mods.simplerandomstuff.reference.Names;
import bluemonster122.mods.simplerandomstuff.tanks.FRTank;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidActionResult;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

import javax.annotation.Nullable;

public class BlockFloodGate extends BlockSRS implements ITileEntityProvider {
    public BlockFloodGate( ) {
        super(Names.Blocks.FLOOD_GATE, Material.IRON);
        setResistance(5);
        setHardness(5);
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        ItemStack heldItem = playerIn.getHeldItem(hand);
        if (heldItem.getItem() == FRTank.upgrade) return false;
        if (worldIn.isRemote) return heldItem != ItemStack.EMPTY && !(heldItem.getItem() instanceof ItemBlock);
        TileEntity te = worldIn.getTileEntity(pos);
        if (te == null || !te.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, facing)) {
            return false;
        }
        IFluidHandler fluidHandler = te.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, facing);
        FluidActionResult result = FluidUtil.interactWithFluidHandler(heldItem, fluidHandler, playerIn);
        if (result.isSuccess()) playerIn.setHeldItem(hand, result.getResult());
        // prevent interaction so stuff like buckets and other things don't place the liquid block
        te.markDirty();
        IBlockState blockState = worldIn.getBlockState(pos);
        worldIn.notifyBlockUpdate(pos, blockState, blockState, 3);
        return heldItem != ItemStack.EMPTY && !(heldItem.getItem() instanceof ItemBlock);
    }


    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileFloodGate();
    }
}
