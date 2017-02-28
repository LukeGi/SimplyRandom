package bluemonster122.mods.simplethings.block;

import bluemonster122.mods.simplethings.SimpleThings;
import bluemonster122.mods.simplethings.handler.ConfigurationHandler;
import bluemonster122.mods.simplethings.tileentity.TileGeneratorFire;
import bluemonster122.mods.simplethings.tileentity.TileGeneratorSugar;
import bluemonster122.mods.simplethings.tileentity.TileLightningRod;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockGenerator extends BlockST implements ITileEntityProvider {
    public static final PropertyEnum<GeneratorType> TYPE = PropertyEnum.create("type", GeneratorType.class);
    protected static final AxisAlignedBB AABB_LR = new AxisAlignedBB(0.375D, 0.0D, 0.375D, 0.625D, 1.0D, 0.625D);

    public BlockGenerator(String name, Material materialIn) {
        super(name, materialIn);
    }

    public BlockGenerator(String name) {
        this(name, Material.IRON);
        setDefaultState(blockState.getBaseState().withProperty(TYPE, GeneratorType.FIRE));
        setHardness(10);
        setResistance(25);
        setHarvestLevel("pickaxe", 1);
        setCreativeTab(SimpleThings.theTab);
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        GeneratorType type = state.getValue(TYPE);
        TileEntity tileEntity = worldIn.getTileEntity(pos);
        if (type.equals(GeneratorType.SUGAR)) {
            if (worldIn.isRemote) return true;
            if (playerIn.getHeldItem(hand).getItem().equals(Items.SUGAR)) {
                if (tileEntity != null && tileEntity instanceof TileGeneratorSugar) {
                    ((TileGeneratorSugar) tileEntity).addToBurnTime(ConfigurationHandler.sugar_burn_time);
                    playerIn.getHeldItem(hand).shrink(1);
                }
            }
            return true;
        } else if (type.equals(GeneratorType.LIGHTNING_ROD) && tileEntity != null && tileEntity instanceof TileLightningRod) {
            if (worldIn.isRemote) return true;
            ItemStack held = playerIn.getHeldItem(hand);
            TileLightningRod tile = (TileLightningRod) tileEntity;
            if (held.getItem().equals(Items.WATER_BUCKET)) {
                if (tile.addWater()) {
                    playerIn.setHeldItem(hand, new ItemStack(Items.BUCKET, 1));
                }
            } else if (held.getItem().equals(Item.getItemFromBlock(Blocks.PLANKS))) {
                if (tile.addWood()) {
                    playerIn.getHeldItem(hand).shrink(1);
                }
            } else if (held.getItem().equals(Item.getItemFromBlock(Blocks.PISTON))) {
                if (tile.addPiston()) {
                    playerIn.getHeldItem(hand).shrink(1);
                }
            } else if (held.getItem().equals(Items.GOLD_INGOT)) {
                if (tile.addGold()) {
                    playerIn.getHeldItem(hand).shrink(1);
                }
            }
            return true;
        } else {
            return super.onBlockActivated(worldIn, pos, state, playerIn, hand, facing, hitX, hitY, hitZ);
        }
    }

    @Override
    public void getSubBlocks(Item itemIn, CreativeTabs tab, NonNullList<ItemStack> list) {
        for (int i = 0; i < GeneratorType.values().length; i++) {
            list.add(new ItemStack(itemIn, 1, i));
        }
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, TYPE);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(TYPE, GeneratorType.values()[meta % GeneratorType.values().length]);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(TYPE).ordinal();
    }

    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
        return getStateFromMeta(placer.getHeldItem(hand).getMetadata());
    }

    @Override
    public boolean isNormalCube(IBlockState state, IBlockAccess world, BlockPos pos) {
        return !state.getValue(TYPE).equals(GeneratorType.LIGHTNING_ROD);
    }

    @Override
    public boolean isFullyOpaque(IBlockState state) {
        return !state.getValue(TYPE).equals(GeneratorType.LIGHTNING_ROD);
    }

    @Override
    public boolean isFullBlock(IBlockState state) {
        return !state.getValue(TYPE).equals(GeneratorType.LIGHTNING_ROD);
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return !state.getValue(TYPE).equals(GeneratorType.LIGHTNING_ROD);
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return state.getValue(TYPE).equals(GeneratorType.LIGHTNING_ROD) ? AABB_LR : super.getBoundingBox(state, source, pos);
    }

    @Nullable
    @Override
    public AxisAlignedBB getCollisionBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return state.getValue(TYPE).equals(GeneratorType.LIGHTNING_ROD) ? AABB_LR : super.getCollisionBoundingBox(state, source, pos);
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return !state.getValue(TYPE).equals(GeneratorType.LIGHTNING_ROD);
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        GeneratorType type = getStateFromMeta(meta).getValue(TYPE);
        if (type.equals(GeneratorType.FIRE)) {
            return new TileGeneratorFire();
        } else if (type.equals(GeneratorType.SUGAR)) {
            return new TileGeneratorSugar();
        } else if (type.equals(GeneratorType.LIGHTNING_ROD)) {
            return new TileLightningRod();
        }
        return null;
    }

    public enum GeneratorType implements IStringSerializable {
        FIRE, SUGAR, LIGHTNING_ROD;

        public static GeneratorType byMetadata(int metadata) {
            return values()[metadata % values().length];
        }

        @Override
        public String getName() {
            return name().toLowerCase();
        }
    }
}
