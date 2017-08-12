package bluemonster.simplerandomstuff.pump;

import bluemonster.simplerandomstuff.core.block.BlockSRS;
import bluemonster.simplerandomstuff.core.block.IPickup;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockPump
        extends BlockSRS
        implements IPickup, ITileEntityProvider {
    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TilePump();
    }

    public BlockPump() {
        super("pump", Material.IRON);
        setHardness(5000f);
        setResistance(1f);
    }
}
