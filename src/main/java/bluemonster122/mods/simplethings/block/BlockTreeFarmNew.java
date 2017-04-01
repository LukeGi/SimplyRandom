package bluemonster122.mods.simplethings.block;

import bluemonster122.mods.simplethings.tileentity.treefarm.TileTreeFarmNew;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import javax.annotation.Nullable;

/**
 * Created by blue on 3/31/17.
 */
public class BlockTreeFarmNew extends BlockST implements ITileEntityProvider{
    public BlockTreeFarmNew() {
        super("tree_farm_new", Material.IRON);
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileTreeFarmNew();
    }
}
