package lhg.forgemods.simplyrandom.treefarm;

import com.google.common.collect.Maps;
import com.google.common.collect.Queues;
import com.google.common.collect.Sets;
import lhg.forgemods.simplyrandom.treefarm.TreeFarmTileEntity.BlockType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.EnumMap;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.function.BiPredicate;

/**
 * Tree Scanner. This is used to efficiently scan for a whole tree.
 */
public class TreeScanner
{
    private static final Logger LOGGER = LogManager.getLogger();
    private EnumMap<BlockType, Queue<BlockPos>> tree = Maps.newEnumMap(BlockType.class);
    private Queue<BlockPos> toVisit = Queues.newPriorityQueue();
    private Set<BlockPos> visited = Sets.newHashSet();
    private BiPredicate<IBlockReader, BlockPos> isAir;
    private BiPredicate<IBlockReader, BlockPos> isLog;
    private BiPredicate<IBlockReader, BlockPos> isLeaf;
    private BlockPos last;
    private BlockPos seed;

    public TreeScanner(BlockPos seed, BiPredicate<IBlockReader, BlockPos> logPredicate, BiPredicate<IBlockReader, BlockPos> leafPredicate, BiPredicate<IBlockReader, BlockPos> airPredicate)
    {
        this.seed = seed;
        this.isAir = airPredicate;
        this.isLog = logPredicate;
        this.isLeaf = leafPredicate;
        toVisit.add(seed);
    }

    public boolean scan(IBlockReader world)
    {
        return scan(world, 0);
    }

    public boolean scan(IBlockReader world, int depth)
    {
        if (toVisit.isEmpty())
        {
            return true;
        }
        if (depth >= 8)
        {
            return false;
        }
        final BlockPos pos = toVisit.poll();
        last = pos;
        visited.add(pos);
        LOGGER.trace(String.format("Scanning %s", pos.toString()));
        if (isAir.test(world, pos))
        {
            return scan(world, depth++);
        } else if (isLeaf.test(world, pos))
        {
            tree.computeIfAbsent(BlockType.LEAF, type -> Queues.newPriorityQueue()).add(pos);
        } else if (isLog.test(world, pos))
        {
            tree.computeIfAbsent(BlockType.LOG, type -> Queues.newPriorityQueue()).add(pos);
        } else
        {
            return scan(world, depth++);
        }
        for (Direction direction : Direction.values())
        {
            final BlockPos next = pos.offset(direction);
            processPos(next);
            for (Direction direction1 : Direction.values())
            {
                processPos(next.offset(direction1));
            }
        }
        LOGGER.trace(String.format("queueSize:%d", toVisit.size()));
        return false;
    }

    private void processPos(BlockPos next)
    {
        if (!visited.contains(next) && !toVisit.contains(next))
        {
            toVisit.add(next);
        }
    }

    public Map<BlockType, Queue<BlockPos>> getTree()
    {
        return tree;
    }

    public void reset()
    {
        tree.clear();
        toVisit.clear();
        visited.clear();
        toVisit.add(seed);
        last = seed;
    }

    public BlockPos getLast()
    {
        return last;
    }
}
