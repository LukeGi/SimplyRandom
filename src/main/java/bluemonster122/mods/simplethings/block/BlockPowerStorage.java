package bluemonster122.mods.simplethings.block;

import bluemonster122.mods.simplethings.SimpleThings;
import bluemonster122.mods.simplethings.tileentity.TilePowerStorage;
import bluemonster122.mods.simplethings.util.ITileEntityProvider1;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockPowerStorage extends BlockST implements ITileEntityProvider1 {
    public BlockPowerStorage(String name) {
        super(name + "_battery", Material.IRON);
        setCreativeTab(SimpleThings.theTab);

    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (!worldIn.isRemote) {
            playerIn.sendMessage(new TextComponentString("EnergyStored: " + ((TilePowerStorage) worldIn.getTileEntity(pos)).getBattery().getEnergyStored()));
        }
        return super.onBlockActivated(worldIn, pos, state, playerIn, hand, facing, hitX, hitY, hitZ);
    }

    @Override
    public Class<? extends TileEntity> getTileClass() {
        return TilePowerStorage.class;
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TilePowerStorage();
    }
}
