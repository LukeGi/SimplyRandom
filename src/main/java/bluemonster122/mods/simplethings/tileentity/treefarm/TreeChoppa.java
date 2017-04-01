package bluemonster122.mods.simplethings.tileentity.treefarm;

import bluemonster122.mods.simplethings.SimpleThings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLog;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;
import org.apache.commons.lang3.ArrayUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class TreeChoppa {
    private TileTreeFarmNew farmNew;
    private World world;
    private long startTime;

    public TreeChoppa(TileTreeFarmNew farmNew) {
        this.farmNew = farmNew;
        startTime = System.nanoTime();
    }

    public boolean scanTree(BlockPos pos) {
//        SimpleThings.logger.info("Scanning at " + pos.toString());
        startTime = System.nanoTime();
        ArrayList<BlockPos> tree = new ArrayList<BlockPos>();
        scanBlock(pos, tree, pos);
        return harvestDrops(tree);
    }

    private void scanBlock(BlockPos origin, ArrayList<BlockPos> tree, BlockPos pos) {
        if (!isInRange(origin, pos) || tree.contains(pos)) return;

        world = farmNew.getWorld();

        IBlockState state = world.getBlockState(pos);
        int leaves;
        if (state.getMaterial().equals(Material.LEAVES))
            leaves = 1;
        else if (state.getBlock() instanceof BlockLog)
            leaves = 2;
        else
            leaves = 0;
        switch (leaves) {
            case 0:
                break;
            case 1:
                tree.add(pos);
                for (EnumFacing facing : EnumFacing.VALUES) {
                    scanBlock(origin, tree, pos.offset(facing));
                    if (ArrayUtils.contains(EnumFacing.HORIZONTALS, facing))
                        for (EnumFacing facing1 : EnumFacing.HORIZONTALS)
                            scanBlock(origin, tree, pos.offset(facing).offset(facing1));
                }
            case 2:
                tree.add(pos);
                for (EnumFacing facing : EnumFacing.VALUES) {
                    if (facing != EnumFacing.DOWN) {
                        scanBlock(origin, tree, pos.offset(facing));
                        if (ArrayUtils.contains(EnumFacing.HORIZONTALS, facing))
                            for (EnumFacing facing1 : EnumFacing.HORIZONTALS)
                                scanBlock(origin, tree, pos.offset(facing).offset(facing1));
                    }
                }
        }

    }

    private boolean isInRange(BlockPos origin, BlockPos test) {
        if (Math.abs(origin.getX() - test.getX()) > 13)
            return false;
        if (Math.abs(origin.getZ() - test.getZ()) > 13)
            return false;
        return true;
    }

    private boolean harvestDrops(ArrayList<BlockPos> tree) {
        ItemStackHandler dropInv = new ItemStackHandler(100);
        for (BlockPos pos : tree) {
            IBlockState state = world.getBlockState(pos);
            Block block = state.getBlock();
            List<ItemStack> drops = block.getDrops(world, pos, state, 0);
            for (ItemStack drop : drops) {
                ItemHandlerHelper.insertItem(dropInv, drop, false);
            }
        }
        for (int i = 0; i < dropInv.getSlots(); i++) {
            ItemStack itemStack = dropInv.getStackInSlot(i);
            if (!itemStack.equals(ItemStack.EMPTY)){
                world.spawnEntity(new EntityItem(world, farmNew.getPos().getX() + 0.5, farmNew.getPos().getY() + 1.5, farmNew.getPos().getZ(), itemStack));
            }
        }
        return cutTree(tree);
    }

    private boolean cutTree(ArrayList<BlockPos> tree) {
        HashSet<Chunk> chunksUpdated = new HashSet<>();
        Chunk chunk;
        for (BlockPos pos : tree) {
            chunk = world.getChunkFromBlockCoords(pos);
            chunk.setBlockState(pos, Blocks.AIR.getDefaultState());
            chunksUpdated.add(chunk);
            world.markAndNotifyBlock(pos, chunk, world.getBlockState(pos), Blocks.AIR.getDefaultState(), 3);
        }
        chunksUpdated.forEach(Chunk::checkLight);
        SimpleThings.logger.info(String.format("Took %s nanoseconds", System.nanoTime() - startTime));
        return false;
    }
}
