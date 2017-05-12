package bluemonster122.mods.simplerandomstuff.pump;

import bluemonster122.mods.simplerandomstuff.core.block.BlockST;
import bluemonster122.mods.simplerandomstuff.reference.Names;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockFloodGate extends BlockST implements ITileEntityProvider{
    public BlockFloodGate() {
        super(Names.Blocks.FLOOD_GATE, Material.IRON);
        setResistance(5);
        setHardness(5);
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileFloodGate();
    }
}
