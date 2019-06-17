package lhg.forgemods.simplyrandom.core;

import com.google.common.collect.ImmutableSet;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntityType;

import java.util.function.Supplier;

/**
 * This is the base type, use for a better constructor and to provide a type for only one block
 *
 * @param <T> Tile Type which specifically is an {@link SRTileEntity}
 */
public class SRTileEntityType<T extends SRTileEntity> extends TileEntityType<T>
{
    public SRTileEntityType(Supplier<? extends T> factoryIn, Block block)
    {
        super(factoryIn, ImmutableSet.of(block), null);
    }
}
