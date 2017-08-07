package bluemonster122.mods.simplerandomstuff.generators;

import bluemonster122.mods.simplerandomstuff.core.block.BlockEnum;
import bluemonster122.mods.simplerandomstuff.core.block.IEnumMeta;
import bluemonster122.mods.simplerandomstuff.core.block.IPickup;
import bluemonster122.mods.simplerandomstuff.reference.Names;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockGenerator
        extends BlockEnum
        implements ITileEntityProvider, IPickup {
    public static final PropertyEnum<Types> VARIANT = PropertyEnum.create("variant", Types.class);

    public BlockGenerator() {
        super(Names.Blocks.GENERATOR, Material.IRON, Types.VARIANTS);
        setHardness(5000f);
        setResistance(1f);
        setDefaultState(getDefaultState().withProperty(VARIANT, Types.SUGAR));
    }

    @Override
    @Deprecated
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState()
                .withProperty(VARIANT, Types.byMeta(meta));
    }

    @Override
    public int getMetaFromState(IBlockState blockState) {
        return blockState.getValue(VARIANT)
                .getMeta();
    }

    @Override
    public int damageDropped(IBlockState state) {
        return state.getValue(VARIANT)
                .getMeta();
    }

    @Override
    public boolean onBlockActivated(
            World worldIn,
            BlockPos pos,
            IBlockState state,
            EntityPlayer playerIn,
            EnumHand hand,
            EnumFacing facing,
            float hitX,
            float hitY,
            float hitZ
    ) {
        if (!worldIn.isRemote) {
            switch (state.getValue(VARIANT)) {
                case SUGAR:
                    ItemStack heldItem = playerIn.getHeldItem(hand);
                    if (heldItem.getItem()
                            .equals(Items.SUGAR)) {
                        TileEntity tile = worldIn.getTileEntity(pos);
                        if (tile instanceof TileGeneratorSugar) {
                            heldItem.shrink(1);
                            playerIn.setHeldItem(hand, heldItem);
                            ((TileGeneratorSugar) tile).addBurnTime(FRGenerators.Sugar_Burntime);
                            return true;
                        }
                    }
                    break;
                case FIRE:
                default:
                    break;
            }
        }
        return false;
    }

    @Override
    public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> itemList) {
        for (Types type : Types.VARIANTS) {
            itemList.add(new ItemStack(this, 1, type.getMeta()));
        }
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, VARIANT);
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        switch (Types.byMeta(meta)) {
            case SUGAR:
                return new TileGeneratorSugar();
            case FIRE:
                return new TileGeneratorFire();
            default:
                return null;
        }
    }

    public enum Types
            implements IEnumMeta, Comparable<Types> {
        SUGAR, FIRE;

        protected static final Types[] VARIANTS = values();

        private int meta;

        Types() {
            meta = ordinal();
        }

        public static Types byMeta(int meta) {
            return VARIANTS[Math.abs(meta) % VARIANTS.length];
        }

        @Override
        public int getMeta() {
            return meta;
        }
    }
}
