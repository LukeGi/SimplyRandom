package bluemonster122.mods.simplerandomstuff.treefarm;

import bluemonster122.mods.simplerandomstuff.SimpleRandomStuff;
import bluemonster122.mods.simplerandomstuff.core.block.BlockSRS;
import bluemonster122.mods.simplerandomstuff.handler.GuiHandler;
import bluemonster122.mods.simplerandomstuff.reference.Names;
import bluemonster122.mods.simplerandomstuff.tanks.TileTank;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

// TODO: Make custom inventory, which you can getStoredStacks() from, as to make dropping inventory easier.
public class BlockTreeFarm extends BlockSRS implements ITileEntityProvider {
    public BlockTreeFarm( ) {
        super(Names.Blocks.TREE_FARM, Material.IRON);
        setLightOpacity(0);
        setHarvestLevel("pickaxe", 3);
        setHardness(7);
        setResistance(500);
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @SideOnly(Side.CLIENT)
    @Nonnull
    @Override
    public BlockRenderLayer getBlockLayer( ) {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing heldItem, float side, float hitX, float hitY) {
        playerIn.openGui(SimpleRandomStuff.INSTANCE, GuiHandler.ID, worldIn, pos.getX(), pos.getY(), pos.getZ());
        return true;
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        TileEntity tileEntity = worldIn.getTileEntity(pos);
        if (tileEntity instanceof TileTreeFarm) {
            ((TileTreeFarm) tileEntity).dropContents();
        }
        super.breakBlock(worldIn, pos, state);
    }

    @Override
    public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos) {
        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof TileTank) {
            FluidStack stack = ((TileTank) tile).tank.getFluid();
            return stack == null || stack.amount <= 0 ? 0 : stack.getFluid().getLuminosity(stack);
        }
        return super.getLightValue(state, world, pos);
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileTreeFarm();
    }
}
