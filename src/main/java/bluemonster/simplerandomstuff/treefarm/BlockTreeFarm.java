package bluemonster.simplerandomstuff.treefarm;

import bluemonster.simplerandomstuff.SimpleRandomStuff;
import bluemonster.simplerandomstuff.config.Configs;
import bluemonster.simplerandomstuff.core.BlockCoreMachine;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockTreeFarm extends BlockCoreMachine {
    public BlockTreeFarm() {
        super(TreeFarmProps.ID, Material.IRON);
    }

    @Override
    protected boolean isEnabled() {
        return Configs.TREE_FARM.shouldLoad;
    }

    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    public boolean isFullCube(IBlockState state) {
        return false;
    }

    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    protected Class<? extends TileEntity> getTileClass() {
        return TileTreeFarm.class;
    }

    @Override
    protected TileEntity getNewTile(World world, IBlockState state) {
        return new TileTreeFarm();
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
        TileEntity tile = worldIn.getTileEntity(pos);
        if (tile instanceof TileTreeFarm && placer instanceof EntityPlayer) {
            ((TileTreeFarm) tile).player = (EntityPlayer) placer;
        } else
            worldIn.destroyBlock(pos, true);
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        playerIn.openGui(SimpleRandomStuff.instance, 0, worldIn, pos.getX(), pos.getY(), pos.getZ());
        return true;
    }


}
