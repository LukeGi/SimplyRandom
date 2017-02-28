package bluemonster122.mods.simplethings.block;

import bluemonster122.mods.simplethings.SimpleThings;
import bluemonster122.mods.simplethings.reference.Names;
import bluemonster122.mods.simplethings.tileentity.TileLightningRod;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;

@SuppressWarnings("deprecation")
public class BlockLightningRod extends BlockST implements ITileEntityProvider {
    protected static final AxisAlignedBB AABB = new AxisAlignedBB(0.375D, 0.0D, 0.375D, 0.625D, 1.0D, 0.625D);

    public BlockLightningRod() {
        super(Names.LIGHTNING_ROD, Material.CIRCUITS);
        setCreativeTab(SimpleThings.theTab);

    }

    @Override
    public boolean isFullyOpaque(IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullBlock(IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return AABB;
    }

    @Nullable
    @Override
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
        return AABB;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (worldIn.isRemote) return true;
        ItemStack held = playerIn.getHeldItem(hand);
        if (held.getItem().equals(Items.WATER_BUCKET)) {
            if (getTile(worldIn, pos).addWater()) {
                playerIn.setHeldItem(hand, new ItemStack(Items.BUCKET, 1));
            }
        } else if (held.getItem().equals(Item.getItemFromBlock(Blocks.PLANKS))) {
            if (getTile(worldIn, pos).addWood()) {
                playerIn.getHeldItem(hand).shrink(1);
            }
        } else if (held.getItem().equals(Item.getItemFromBlock(Blocks.PISTON))) {
            if (getTile(worldIn, pos).addPiston()) {
                playerIn.getHeldItem(hand).shrink(1);
            }
        } else if (held.getItem().equals(Items.GOLD_INGOT)) {
            if (getTile(worldIn, pos).addGold()) {
                playerIn.getHeldItem(hand).shrink(1);
            }
        }
        return true;
    }

    @Override
    public boolean isNormalCube(IBlockState state, IBlockAccess world, BlockPos pos) {
        return false;
    }

    private TileLightningRod getTile(World world, BlockPos pos) {
        return (TileLightningRod) world.getTileEntity(pos);
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileLightningRod();
    }
}
