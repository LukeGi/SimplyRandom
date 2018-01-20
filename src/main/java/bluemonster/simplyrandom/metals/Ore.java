package bluemonster.simplyrandom.metals;

import bluemonster.simplyrandom.core.BlockBase;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;

import java.util.Random;


public class Ore extends BlockBase {

    private final Item drop;
    private boolean advanced;

    public Ore(String type, Item drop, boolean advanced) {
        super(Material.ROCK, type + "_ore");
        this.drop = drop;
        this.advanced = advanced;
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return drop;
    }

    @Override
    public int quantityDropped(IBlockState state, int fortune, Random random) {
        return advanced ? 2 + random.nextInt(4) : 4 + random.nextInt(7);
    }
}
