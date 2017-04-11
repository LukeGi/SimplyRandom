package bluemonster122.mods.simplethings.tanks;

import bluemonster122.mods.simplethings.SimpleThings;
import bluemonster122.mods.simplethings.core.block.BlockEnum;
import bluemonster122.mods.simplethings.core.block.IEnumMeta;
import bluemonster122.mods.simplethings.core.block.IPickup;
import com.google.common.collect.Lists;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidActionResult;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public class BlockTank extends BlockEnum implements ITileEntityProvider, IPickup {
    public static final PropertyEnum<Types> VARIANT = PropertyEnum.create("variant", Types.class);
    private static final AxisAlignedBB AABB = new AxisAlignedBB(1 / 16F, 0, 1 / 16F, 15 / 16f, 1, 15 / 16f);

    public BlockTank() {
        super("tank", Material.GLASS, Types.VARIANTS);
        setCreativeTab(SimpleThings.theTab);
        setHardness(5000f);
        setResistance(1f);
        setDefaultState(getDefaultState().withProperty(VARIANT, Types.GLASS));
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean isTranslucent(IBlockState state) {
        return true;
    }

    @Override
    @Deprecated
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(VARIANT, Types.byMeta(meta));
    }

    @Override
    public int getMetaFromState(IBlockState blockState) {
        return blockState.getValue(VARIANT).getMeta();
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @SuppressWarnings("deprecation")
    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return AABB;
    }

    @SuppressWarnings("deprecation")
    @Nullable
    @Override
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
        return AABB;
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public int damageDropped(IBlockState blockState) {
        return blockState.getValue(VARIANT).getMeta();
    }

    @SideOnly(Side.CLIENT)
    @Nonnull
    @Override
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (!pickup(worldIn, pos, state, playerIn, hand)) {
            ItemStack heldItem = playerIn.getHeldItem(hand);
            if (heldItem.getItem() == FRTank.upgrade) return false;
            if (worldIn.isRemote) return heldItem != ItemStack.EMPTY && !(heldItem.getItem() instanceof ItemBlock);
            TileEntity te = worldIn.getTileEntity(pos);
            if (te == null || !te.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, facing)) {
                return false;
            }
            IFluidHandler fluidHandler = te.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, facing);
            FluidActionResult result = FluidUtil.interactWithFluidHandler(heldItem, fluidHandler, playerIn);
            if (result.isSuccess())
                playerIn.setHeldItem(hand, result.getResult());
            // prevent interaction so stuff like buckets and other things don't place the liquid block
            te.markDirty();
            IBlockState blockState = worldIn.getBlockState(pos);
            worldIn.notifyBlockUpdate(pos, blockState, blockState, 3);
            return heldItem != ItemStack.EMPTY && !(heldItem.getItem() instanceof ItemBlock);
        } else {
            return true;
        }
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        TileEntity te = world.getTileEntity(pos);
        if (te instanceof TileTank && stack != null && stack.hasTagCompound()) {
            ((TileTank) te).readTank(stack.getTagCompound());
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void getSubBlocks(Item item, CreativeTabs tab, NonNullList<ItemStack> itemStacks) {
        for (Types type : Types.VARIANTS) {
            itemStacks.add(new ItemStack(item, 1, type.getMeta()));
        }
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, VARIANT);
    }

    @Nonnull
    @Override
    public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, @Nonnull IBlockState state, int fortune) {
        List<ItemStack> ret = Lists.newArrayList();
        Random rand = world instanceof World ? ((World) world).rand : RANDOM;
        Item item = this.getItemDropped(state, rand, fortune);
        NBTTagCompound tag = null;
        TileEntity te = world.getTileEntity(pos);
        if (te instanceof TileTank) {
            if (((TileTank) te).tank.getFluid() != null) {
                tag = ((TileTank) te).writeTank(new NBTTagCompound());
            }
        }
        ret.add(new ItemStack(item, 1, this.damageDropped(state), tag));
        return ret;
    }

    @Override
    public ItemBlock createItemBlock() {
        return new ItemBlockTank(this);
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileTank();
    }

    public enum Types implements IEnumMeta, Comparable<Types> {
        GLASS,
        IRON,
        GOLD,
        OBSIDIAN,
        DIAMOND;

        protected static final Types[] VARIANTS = values();
        private int meta;

        public static Types byMeta(int meta) {
            return VARIANTS[Math.abs(meta) % VARIANTS.length];
        }

        @Override
        public int getMeta() {
            return meta;
        }

        Types() {
            meta = ordinal();
        }
    }
}
