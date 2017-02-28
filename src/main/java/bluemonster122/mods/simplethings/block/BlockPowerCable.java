package bluemonster122.mods.simplethings.block;

import bluemonster122.mods.simplethings.SimpleThings;
import bluemonster122.mods.simplethings.reference.Names;
import bluemonster122.mods.simplethings.tileentity.TilePowerCable;
import bluemonster122.mods.simplethings.util.ITileEntityProvider1;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockPowerCable extends BlockST implements ITileEntityProvider1 {
    public BlockPowerCable() {
        super(Names.POWER_CABLE, Material.CIRCUITS);
        setCreativeTab(SimpleThings.theTab);

    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TilePowerCable();
    }

    @Override
    public Class<? extends TileEntity> getTileClass() {
        return TilePowerCable.class;
    }
}
