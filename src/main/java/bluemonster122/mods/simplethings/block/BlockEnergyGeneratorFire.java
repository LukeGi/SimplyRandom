package bluemonster122.mods.simplethings.block;

import bluemonster122.mods.simplethings.SimpleThings;
import bluemonster122.mods.simplethings.reference.Names;
import bluemonster122.mods.simplethings.tileentity.TileGeneratorFire;
import bluemonster122.mods.simplethings.util.ITileEntityProvider1;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockEnergyGeneratorFire extends BlockST implements ITileEntityProvider1 {
    public BlockEnergyGeneratorFire() {
        super(Names.ENERGY_GENERATOR_FIRE, Material.ROCK);
        setCreativeTab(SimpleThings.theTab);

    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileGeneratorFire();
    }

    @Override
    public Class<? extends TileEntity> getTileClass() {
        return TileGeneratorFire.class;
    }
}
