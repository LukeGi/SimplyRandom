package bluemonster122.mods.simplethings.block;

import bluemonster122.mods.simplethings.SimpleThings;
import bluemonster122.mods.simplethings.handler.ConfigurationHandler;
import bluemonster122.mods.simplethings.reference.Names;
import bluemonster122.mods.simplethings.tileentity.TileGeneratorSugar;
import bluemonster122.mods.simplethings.util.ITileEntityProvider1;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockEnergyGeneratorSugar extends BlockST implements ITileEntityProvider1 {
    public BlockEnergyGeneratorSugar() {
        super(Names.ENERGY_GENERATOR_SUGAR, Material.IRON);
        setCreativeTab(SimpleThings.theTab);

    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing heldItem, float side, float hitX, float hitY) {
        if (worldIn.isRemote) return true;
        if (playerIn.getHeldItem(hand).getItem().equals(Items.SUGAR)) {
            TileEntity tile = worldIn.getTileEntity(pos);
            if (tile != null && tile instanceof TileGeneratorSugar) {
                ((TileGeneratorSugar) tile).addToBurnTime(ConfigurationHandler.sugar_burn_time);
                playerIn.getHeldItem(hand).shrink(1);
            }
        }
        return true;
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileGeneratorSugar();
    }

    @Override
    public Class<? extends TileEntity> getTileClass() {
        return TileGeneratorSugar.class;
    }
}
