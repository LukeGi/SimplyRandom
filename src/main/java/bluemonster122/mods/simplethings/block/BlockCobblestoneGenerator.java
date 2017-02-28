package bluemonster122.mods.simplethings.block;

import bluemonster122.mods.simplethings.SimpleThings;
import bluemonster122.mods.simplethings.reference.Names;
import bluemonster122.mods.simplethings.tileentity.TileCobblestoneGenerator;
import bluemonster122.mods.simplethings.util.ITileEntityProvider1;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockCobblestoneGenerator extends BlockST implements ITileEntityProvider1 {
    public BlockCobblestoneGenerator() {
        super(Names.COBBLESTONE_GENERATOR, Material.IRON);
        setCreativeTab(SimpleThings.theTab);
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileCobblestoneGenerator();
    }

    @Override
    public Class<? extends TileEntity> getTileClass() {
        return TileCobblestoneGenerator.class;
    }
}
