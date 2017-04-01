package bluemonster122.mods.simplethings.tileentity.treefarm;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;

public class Tree {
    public World world;
    private BlockPos[] blocks;
    private int index;

    public Tree(World world_, ArrayList<BlockPos> blocks_) {
        world = world_;
        blocks = new BlockPos[blocks_.size()];
        for (int i = 0; i < blocks.length; i++) {
            blocks[i] = blocks_.get(i);
        }
        index = 0;
    }

    public BlockPos poll() {
        return blocks[index++];
    }

    public boolean isFinished() {
        return index == blocks.length;
    }
}
