package luhegi.mods.simplyrandom.basis.objects;

import com.google.common.collect.Sets;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;

import java.util.function.Supplier;

public class BasisTileType<T extends TileEntity> extends TileEntityType<T> {
    public BasisTileType(Supplier<? extends T> factoryIn, Block... validBlocks) {
        super(factoryIn, Sets.newHashSet(validBlocks), null);
    }
}
