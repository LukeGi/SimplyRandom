package bluemonster122.mods.simplerandomstuff.pump;

import bluemonster122.mods.simplerandomstuff.core.block.BlockST;
import bluemonster122.mods.simplerandomstuff.core.block.IPickup;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockPump extends BlockST implements IPickup, ITileEntityProvider {
    public BlockPump( ) {
        super("pump", Material.IRON);
        setHardness(5000f);
        setResistance(1f);
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TilePump();
    }
}
