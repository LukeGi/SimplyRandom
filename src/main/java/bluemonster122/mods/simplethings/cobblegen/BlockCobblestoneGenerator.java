package bluemonster122.mods.simplethings.cobblegen;

import bluemonster122.mods.simplethings.SimpleThings;
import bluemonster122.mods.simplethings.core.block.BlockST;
import bluemonster122.mods.simplethings.reference.Names;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockCobblestoneGenerator extends BlockST implements ITileEntityProvider {
    public BlockCobblestoneGenerator() {
        super(Names.COBBLESTONE_GENERATOR, Material.IRON);
        setCreativeTab(SimpleThings.theTab);
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileCobblestoneGenerator();
    }
}
