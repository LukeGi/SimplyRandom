package bluemonster122.mods.simplethings.block;

import bluemonster122.mods.simplethings.SimpleThings;
import bluemonster122.mods.simplethings.handler.GuiHandler;
import bluemonster122.mods.simplethings.reference.Names;
import bluemonster122.mods.simplethings.tileentity.TileTreeFarm;
import bluemonster122.mods.simplethings.util.ITileEntityProvider1;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

public class BlockTreeFarm extends BlockST implements ITileEntityProvider1 {
    public BlockTreeFarm() {
        super(Names.TREE_FARM, Material.IRON);
        setCreativeTab(SimpleThings.theTab);
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

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        TileEntity tileEntity = worldIn.getTileEntity(pos);
        if (tileEntity instanceof TileTreeFarm) {
            TileTreeFarm farmTile = (TileTreeFarm) tileEntity;
            farmTile.dropInventory();
            farmTile.breakSaplings();
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

    @Override
    public Class<? extends TileEntity> getTileClass() {
        return TileTreeFarm.class;
    }
}
