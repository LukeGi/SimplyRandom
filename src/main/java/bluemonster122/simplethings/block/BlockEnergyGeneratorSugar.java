package bluemonster122.simplethings.block;

import bluemonster122.simplethings.reference.Names;
import bluemonster122.simplethings.tileentity.TileEnergyGeneratorSugar;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockEnergyGeneratorSugar extends SimpleBlockBase implements ITileEntityProvider
{
    public BlockEnergyGeneratorSugar()
    {
        super(Material.IRON, Names.ENERGY_GENERATOR_SUGAR);
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta)
    {
        return new TileEnergyGeneratorSugar();
    }
}
