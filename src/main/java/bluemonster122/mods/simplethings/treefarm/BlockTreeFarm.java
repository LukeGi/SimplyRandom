package bluemonster122.mods.simplethings.treefarm;

import bluemonster122.mods.simplethings.SimpleThings;
import bluemonster122.mods.simplethings.block.BlockST;
import bluemonster122.mods.simplethings.handler.GuiHandler;
import bluemonster122.mods.simplethings.reference.Names;
import bluemonster122.mods.simplethings.tanks.TileTank;
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

public class BlockTreeFarm extends BlockST implements ITileEntityProvider {
    public BlockTreeFarm() {
        super(Names.TREE_FARM, Material.IRON);
        setCreativeTab(SimpleThings.theTab);
        setLightOpacity(0);
        setHarvestLevel("pickaxe", 3);
        setHardness(7);
        setResistance(500);
    }

    @Override
    public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos) {
        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof TileTank ){
            FluidStack stack = ((TileTank)tile).tank.getFluid();
            return stack == null || stack.amount <= 0 ? 0 : stack.getFluid().getLuminosity(stack);
        }
        return super.getLightValue(state, world, pos);
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

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        TileEntity tileEntity = worldIn.getTileEntity(pos);
        if (tileEntity instanceof TileTreeFarm) {
            TileTreeFarm farmTile = (TileTreeFarm) tileEntity;
            farmTile.dropInventory();
        }
        super.breakBlock(worldIn, pos, state);
    }

    @SideOnly(Side.CLIENT)
    @Nonnull
    @Override
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing heldItem, float side, float hitX, float hitY) {
        playerIn.openGui(
                SimpleThings.INSTANCE, GuiHandler.tree_farm_gui_id, worldIn, pos.getX(), pos.getY(), pos.getZ());
        return true;
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileTreeFarm();
    }
}
