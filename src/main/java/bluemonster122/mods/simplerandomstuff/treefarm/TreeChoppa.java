package bluemonster122.mods.simplerandomstuff.treefarm;

import net.minecraft.block.BlockLog;
import net.minecraft.block.BlockVine;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;
import org.apache.commons.lang3.ArrayUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class TreeChoppa
{
  private TileTreeFarm farmNew;
  
  private World        world;
  
  private long         startTime;
  
  public TreeChoppa(TileTreeFarm farmNew)
  {
    this.farmNew = farmNew;
    startTime = System.nanoTime();
  }
  
  public int scanTree(BlockPos pos, boolean cut)
  {
    startTime = System.nanoTime();
    HashSet<BlockPos> tree = new HashSet<>();
    scanBlock(pos, tree, pos);
    if (!cut) return tree.size();
    if (tree.isEmpty()) return 0;
    cutTree(tree);
    return tree.size();
  }
  
  private void scanBlock(BlockPos origin, Set<BlockPos> tree, BlockPos pos)
  {
    if (!isInRange(origin, pos) || tree.contains(pos)) return;
    
    world = farmNew.getWorld();
    
    IBlockState state = world.getBlockState(pos);
    int         leaves;
      if (state.getMaterial()
               .equals(Material.LEAVES))
      {
          leaves = 1;
      }
      else if (state.getBlock() instanceof BlockLog)
      {
          leaves = 2;
      }
      else if (state.getBlock() instanceof BlockVine)
      {
          leaves = 3;
      }
      else
      {
          leaves = 0;
      }
    switch (leaves)
    {
      case 0:
        break;
      case 1:
      case 3:
        tree.add(pos);
        for (EnumFacing facing : EnumFacing.VALUES)
        {
          scanBlock(origin, tree, pos.offset(facing));
            if (ArrayUtils.contains(EnumFacing.HORIZONTALS, facing))
            {
                for (EnumFacing facing1 : EnumFacing.HORIZONTALS)
                {
                    scanBlock(
                      origin,
                      tree,
                      pos.offset(facing)
                         .offset(facing1)
                    );
                }
            }
        }
        break;
      case 2:
        tree.add(pos);
        for (EnumFacing facing : EnumFacing.VALUES)
        {
          if (facing != EnumFacing.DOWN)
          {
            scanBlock(origin, tree, pos.offset(facing));
              if (ArrayUtils.contains(EnumFacing.HORIZONTALS, facing))
              {
                  for (EnumFacing facing1 : EnumFacing.HORIZONTALS)
                  {
                      scanBlock(
                        origin,
                        tree,
                        pos.offset(facing)
                           .offset(facing1)
                      );
                  }
              }
          }
        }
        break;
    }
  }
  
  private boolean isInRange(BlockPos origin, BlockPos test)
  {
    if (Math.abs(origin.getX() - test.getX()) > 13) return false;
    return Math.abs(origin.getZ() - test.getZ()) <= 13;
  }
  
  private void cutTree(Set<BlockPos> tree)
  {
    tree = tree.stream()
               .sorted()
               .collect(Collectors.toSet());
    HashSet<Chunk>   chunksUpdated = new HashSet<>();
    ItemStackHandler dropInv       = new ItemStackHandler(100);
    
    Chunk           chunk;
    List<ItemStack> drops;
    
    for (BlockPos pos : tree)
    {
      // Create the chunk and state ready for future use.
      chunk = world.getChunkFromBlockCoords(pos);
      IBlockState state = chunk.getBlockState(pos);
      
      // Edit the chunk data directly, in order to make it all work as efficiently as possible.
      ExtendedBlockStorage extendedblockstorage = chunk.getBlockStorageArray()[pos.getY() >> 4];
      if (extendedblockstorage == Chunk.NULL_BLOCK_STORAGE)
      {
        extendedblockstorage = new ExtendedBlockStorage(pos.getY() >> 4 << 4, this.world.provider.hasSkyLight());
        chunk.getBlockStorageArray()[pos.getY() >> 4] = extendedblockstorage;
      }
      extendedblockstorage.set(pos.getX() & 15, pos.getY() & 15, pos.getZ() & 15, Blocks.AIR.getDefaultState());
      
      // Add drops
      drops = state.getBlock()
                   .getDrops(world, pos, state, 0);
      drops.forEach(d -> ItemHandlerHelper.insertItem(dropInv, d, false));
      
      // Add this chunk the the list of chunks that have been edited.
      chunksUpdated.add(chunk);
    }
    
    // For each chunk that has been updated in this process, say that it has been modified.
    chunksUpdated.forEach(c -> c.setModified(true));
    
    // For each chunk that has been updated in this process, tell it to update the lighting.
    chunksUpdated.forEach(Chunk::generateSkylightMap);
    
    // Notify Client
    tree.forEach(p -> world.notifyBlockUpdate(p, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), 3));
    
    IItemHandler handler = farmNew.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
    // Spawn all drops
    for (int i = 0; i < dropInv.getSlots(); i++)
    {
      ItemStack leftovers = dropInv.getStackInSlot(i);
      leftovers = ItemHandlerHelper.insertItem(handler, leftovers, false);
      if (leftovers != ItemStack.EMPTY)
      {
        EntityItem entityIn = new EntityItem(
          world,
          farmNew.getPos()
                 .getX() + 0.5,
          farmNew.getPos()
                 .getY() + 5,
          farmNew.getPos()
                 .getZ() + 0.5,
          leftovers
        );
        entityIn.motionX = entityIn.motionY = entityIn.motionZ = 0;
        world.spawnEntity(entityIn);
      }
    }
  }
}
