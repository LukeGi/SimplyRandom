package bluemonster122.mods.simplethings.tileentity;

import bluemonster122.mods.simplethings.tileentity.treefarm.Tree;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import org.apache.commons.lang3.ArrayUtils;

import java.util.ArrayList;

@Mod.EventBusSubscriber
class BlockCutter {
    private static Tree[] trees = new Tree[0];

    public static void addBlocks(World world, ArrayList<BlockPos> blocksIn) {
        Tree[] temp = trees.clone();
        trees = new Tree[temp.length + 1];
        for (int i = 0; i < temp.length; i++) {
            trees[i] = temp[i];
        }
        trees[temp.length] = new Tree(world, blocksIn);
    }

    @SubscribeEvent
    public void onWorldTick(TickEvent.WorldTickEvent e) {
        if (e.side == Side.CLIENT || trees.length == 0) return;

        for (int i = trees.length - 1; i >= 0; i--) {
            if (e.world.equals(trees[i].world)) {
                e.world.setBlockToAir(trees[i].poll());
                if (trees[i].isFinished()) {
                    trees = ArrayUtils.remove(trees, i);
                }
            }
        }
    }

    @SubscribeEvent
    public void onWorldUnload(WorldEvent.Unload e) {
        if (e.getWorld().isRemote || trees.length == 0) return;

        for (int i = trees.length - 1; i >= 0; i--) {
            Tree tree = trees[i];
            if (e.getWorld().equals(tree.world)) {
                while (!tree.isFinished())
                    e.getWorld().setBlockToAir(tree.poll());
            }
        }
    }
}
