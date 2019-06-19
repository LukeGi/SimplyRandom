package lhg.forgemods.simplyrandom.treefarm;

import com.google.common.collect.Maps;
import lhg.forgemods.simplyrandom.core.SRConfig;
import lhg.forgemods.simplyrandom.core.SRConfig.TreeFarmConfig;
import lhg.forgemods.simplyrandom.core.SRTileEntity;
import lhg.forgemods.simplyrandom.render.BoxRender;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.item.DirectionalPlaceContext;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.ServerWorld;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.Comparator;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Stack;

/**
 * Tree Farm Tile Entity
 */
public class TreeFarmTileEntity extends SRTileEntity<TreeFarmTileEntity> implements ITickableTileEntity
{
    public static final TreeFarmConfig TREE_FARM_CONFIG = SRConfig.SERVER.treeFarmConfig;
    private static final Logger LOGGER = LogManager.getLogger();
    private static final Direction[] EXTERNAL_INV_SIDES = {Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST, Direction.DOWN};
    private ItemStackHandler inventory;
    private EnergyStorage battery;
    private State workingState = State.IDLE;
    private State currentState = State.IDLE;
    private Map<BlockType, Queue<BlockPos>> tree = Maps.newEnumMap(BlockType.class);
    private Stack<ItemStack> drops = new Stack<>();
    private Stack<BlockPos> broken = new Stack<>();
    private TreeScanner treeScanner;
    private BlockPos currentPosition = null;
    private BoxRender render = null;
    private long lastIdleTime = 0;

    /**
     * Constructor
     */
    public TreeFarmTileEntity(TileEntityType<TreeFarmTileEntity> type)
    {
        super(type);
        battery = new EnergyStorage(TREE_FARM_CONFIG.maxPower.get());
        inventory = new ItemStackHandler(9)
        {
            @Override
            protected void onContentsChanged(int slot)
            {
                TreeFarmTileEntity.this.markDirty();
                super.onContentsChanged(slot);
            }
        };
    }

    @Override
    public void tick()
    {
        if (world.isRemote())
        {
            if (currentPosition != null)
            {
                if (render != null)
                {
                    render.cleanUp();
                }
                final Vec3d pos1 = new Vec3d(currentPosition.getX() - 0.0005f, currentPosition.getY() - 0.0005f, currentPosition.getZ() - 0.0005f);
                final Vec3d pos2 = new Vec3d(currentPosition.getX() + 1.0005f, currentPosition.getY() + 1.0005f, currentPosition.getZ() + 1.0005f);
                Color color = new Color(255, 255, 255, 50);
                if (currentState == State.SCANNING)
                {
                    color = new Color(0, 255, 255, 50);
                } else if (currentState == State.BREAKING_LEAVES || currentState == State.BREAKING_LOGS)
                {
                    color = new Color(255, 0, 0, 50);
                }
                render = BoxRender.create(color, pos1, pos2);
                render.show();
            }
            return;
        }
        if (treeScanner == null)
        {
            treeScanner = new TreeScanner(pos.up(), this::isLog, this::isLeaf, this::isAir);
        }
        this.markDirty();
        switch (currentState)
        {
            case IDLE:
                if (workingState == State.IDLE)
                {
                    checkForGrowth();
                } else if (world.getGameTime() - lastIdleTime > 100)
                {
                    switchTo(workingState, false);
                }
                break;
            case SCANNING:
                scanTree();
                break;
            case BREAKING_LEAVES:
                breakLeaf();
                break;
            case BREAKING_LOGS:
                breakLog();
                break;
            case REPLANTING:
                replant();
                break;
            case DUMPING:
                dump();
                break;
        }
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt)
    {
        final CompoundNBT data = pkt.getNbtCompound();
        currentState = State.valueOf(data.getString("[SR]currentState"));
        if (data.contains("[SR]currentPosition") && (currentState == State.SCANNING || currentState == State.BREAKING_LOGS || currentState == State.BREAKING_LEAVES))
        {
            currentPosition = NBTUtil.readBlockPos(data.getCompound("[SR]currentPosition"));
        } else
        {
            currentPosition = null;
        }
    }

    @Override
    public void handleUpdateTag(CompoundNBT tag)
    {
        super.handleUpdateTag(tag);
    }

    @Nullable
    @Override
    public SUpdateTileEntityPacket getUpdatePacket()
    {
        final CompoundNBT data = new CompoundNBT();
        data.putString("[SR]currentState", currentState.name());
        if (currentPosition != null && (currentState == State.SCANNING || currentState == State.BREAKING_LOGS || currentState == State.BREAKING_LEAVES))
        {
            data.put("[SR]currentPosition", NBTUtil.writeBlockPos(currentPosition));
        }
        return new SUpdateTileEntityPacket(pos, 0, data);
    }

    @Override
    public CompoundNBT getUpdateTag()
    {
        return super.getUpdateTag();
    }

    @Override
    public void read(CompoundNBT compound)
    {
        if (compound.contains("[SR]stateCurrent"))
        {
            currentState = State.valueOf(compound.getString("[SR]stateCurrent"));
        }
        if (compound.contains("[SR]stateWorking"))
        {
            workingState = State.valueOf(compound.getString("[SR]stateWorking"));
        }
        lastIdleTime = compound.getLong("[SR]lastIdleTime");
        super.read(compound);
    }

    @Override
    public CompoundNBT write(CompoundNBT compound)
    {
        compound.putString("[SR]stateCurrent", currentState.name());
        if (workingState == State.BREAKING_LEAVES || workingState == State.BREAKING_LOGS)
        {
            compound.putString("[SR]stateWorking", State.SCANNING.name());
        } else
        {
            compound.putString("[SR]stateWorking", workingState.name());
        }
        compound.putLong("[SR]lastIdleTime", lastIdleTime);
        return super.write(compound);
    }

    @Override
    protected void populateCaps()
    {
        if (!itemCapability.isPresent())
        {
            itemCapability = inventory == null ? LazyOptional.empty() : LazyOptional.of(() -> inventory);
        }
        if (!energyCapability.isPresent())
        {
            energyCapability = battery == null ? LazyOptional.empty() : LazyOptional.of(() -> battery);
        }
    }

    /**
     * This will dump the contents of this block's inventory into neighbouring inventories
     */
    private void dump()
    {
        boolean empty = true;
        for (int i = 0; i < inventory.getSlots(); i++)
        {
            final ItemStack stack = inventory.getStackInSlot(i);
            if (stack.isEmpty())
            {
                continue;
            }
            empty = false;
            boolean pushed = false;
            for (Direction side : EXTERNAL_INV_SIDES)
            {
                final LazyOptional<IItemHandler> itemHandler = getItemHandler(side);
                if (itemHandler.isPresent())
                {
                    final IItemHandler handler = itemHandler.orElse(null);
                    final ItemStack result = ItemHandlerHelper.insertItem(handler, stack, true);
                    if (result.isEmpty())
                    {
                        ItemHandlerHelper.insertItem(handler, inventory.extractItem(i, stack.getCount(), false), false);
                        pushed = true;
                        break;
                    }
                }
            }
            if (!pushed)
            {
                switchTo(State.IDLE, false);
            }
            break;
        }
        if (empty)
        {
            switchTo(State.IDLE, true);
        }
    }

    /**
     * This will attempt to replant a sapling from an inventory, prioritising its own and then it's neighbours
     */
    private void replant()
    {
        if (!isAir(world, pos.up()))
        {
            switchTo(State.IDLE, true);
        }
        LazyOptional<IItemHandler>[] toCheck = new LazyOptional[6];
        toCheck[0] = LazyOptional.of(() -> inventory);
        for (int i = 0; i < EXTERNAL_INV_SIDES.length; i++)
        {
            toCheck[i + 1] = getItemHandler(EXTERNAL_INV_SIDES[i]);
        }
        for (LazyOptional<IItemHandler> optionalHandler : toCheck)
        {
            optionalHandler.ifPresent(this::replantFrom);
            if (currentState == State.DUMPING)
            {
                return;
            }
        }
        if (currentState != State.REPLANTING)
        {
            switchTo(State.IDLE, false);
        }
    }

    /**
     * This will "break" the next block in the {@link BlockType#LOG Log} queue. If the queue is empty it
     * will process the broken blocks in the {@link #broken} queue and  drops in the {@link #drops} queue
     * <p>
     * It does not actually break the block in the world, but rather add it to the {@link #broken} queue and it's
     * drops to the {@link #drops} queue.
     */
    private void breakLog()
    {
        if (consumeLogBreakPower())
        {
            final Queue<BlockPos> logs = tree.get(BlockType.LOG);
            if (logs == null || logs.isEmpty())
            {
                if (clearQueues()) return;
                switchTo(State.REPLANTING, true);
                currentPosition = null;
            } else
            {
                assert world instanceof ServerWorld : "world on Server Thread should be a ServerWorld";
                final BlockPos logPos = logs.poll();
                if (isLog(world, logPos))
                {
                    breakBlock(logPos);
                    currentPosition = logPos;
                }
            }
            sendDataPacketToClient();
        } else
        {
            switchTo(State.IDLE, false);
        }
    }


    /**
     * This will "break" the next block in the {@link BlockType#LEAF Leaf} queue. If the queue is empty it
     * will process the broken blocks in the {@link #broken} queue and  drops in the {@link #drops} queue
     * <p>
     * It does not actually break the block in the world, but rather add it to the {@link #broken} queue and it's
     * drops to the {@link #drops} queue.
     */
    private void breakLeaf()
    {
        if (consumeLeafBreakPower())
        {
            final Queue<BlockPos> leaves = tree.get(BlockType.LEAF);
            if (leaves == null || leaves.isEmpty())
            {
                if (clearQueues()) return;
                switchTo(State.BREAKING_LOGS, true);
                currentPosition = null;
            } else
            {
                assert world instanceof ServerWorld : "world on Server Thread should be a ServerWorld";
                final BlockPos leafPos = leaves.poll();
                if (isLeaf(world, leafPos))
                {
                    breakBlock(leafPos);
                }
                currentPosition = leafPos;
            }
            sendDataPacketToClient();
        } else
        {
            switchTo(State.IDLE, false);
        }
    }

    /**
     * This will use this block's {@link TreeScanner Tree Scanner} to scan the tree above.
     */
    private void scanTree()
    {
        if (consumeBlockScanPower())
        {
            if (treeScanner.scan(world))
            {
                treeScanner.getTree().forEach((key, value) -> tree.computeIfAbsent(key, type -> createQueue()).addAll(value));
                treeScanner.reset();
                switchTo(State.BREAKING_LEAVES, true);
                currentPosition = null;
            } else
            {
                currentPosition = treeScanner.getLast();
            }
            sendDataPacketToClient();
        } else
        {
            switchTo(State.IDLE, false);
        }
    }

    private void sendDataPacketToClient()
    {
        final BlockState state = world.getBlockState(pos);
        getWorld().notifyBlockUpdate(pos, state, state, 3);
    }

    /**
     * @return Helper method for clearing the {@link #drops} and {@link #broken} queues.
     */
    private boolean clearQueues()
    {
        while (!drops.empty())
        {
            if (ItemHandlerHelper.insertItem(inventory, drops.peek(), true).isEmpty())
            {
                ItemHandlerHelper.insertItem(inventory, drops.pop(), false);
            } else
            {
                switchTo(State.DUMPING, true);
                return true;
            }
        }
        final BlockState airState = Blocks.AIR.getDefaultState();
        Stack<BlockPos> copy = new Stack<>();
        copy.addAll(broken);
        while (!broken.empty())
        {
            world.setBlockState(broken.pop(), airState);
        }
        while (!copy.empty())
        {
            world.getChunkProvider().getLightManager().checkBlock(copy.pop());
        }
        return false;
    }

    /**
     * This will check if the block above is a log (the tree has grown and now we are scanning) or the block above is
     * air (the sapling broke and now we are replanting) or the block above is anything else (either the sapling hasnt
     * grown, or the block above is not workable and we are now IDLE without success)
     */
    private void checkForGrowth()
    {
        if (isLog(world, pos.up()))
        {
            switchTo(State.SCANNING, true);
        } else if (isAir(world, pos.up()))
        {
            switchTo(State.REPLANTING, true);
        } else
        {
            switchTo(State.IDLE, false);
        }
    }

    /**
     * @param blockPos the position of the block to break
     */
    private void breakBlock(BlockPos blockPos)
    {
        if (world.isAirBlock(blockPos))
        {
            return;
        }
        drops.addAll(Block.getDrops(world.getBlockState(blockPos), (ServerWorld) world, blockPos, world.getTileEntity(blockPos)));
        broken.add(blockPos);
    }

    /**
     * Use this to create a queue with the ideal comparator. That is one that puts
     * blocks that are farthest from the block at the front of the queue.
     *
     * @return A queue with the ideal comparator
     */
    private PriorityQueue<BlockPos> createQueue()
    {
        return new PriorityQueue<>(Comparator.<BlockPos>comparingDouble(pos::distanceSq).reversed());
    }

    /**
     * Searches {@code inventory} for a sapling, and attempts to place it in the block above.
     *
     * @param inventory the inventory to search for a sapling in
     */
    private void replantFrom(IItemHandler inventory)
    {
        if (consumeInvScanPower())
        {
            for (int i = 0; i < inventory.getSlots(); i++)
            {
                final ItemStack stack = inventory.extractItem(i, 1, true);
                if (isSapling(stack) && !stack.isEmpty())
                {
                    final DirectionalPlaceContext placeContext = new DirectionalPlaceContext(world, pos.up(), Direction.DOWN, stack, Direction.DOWN);
                    final ActionResultType actionResult = ((BlockItem) stack.getItem()).tryPlace(placeContext);
                    if (actionResult == ActionResultType.SUCCESS)
                    {
                        inventory.extractItem(i, 1, false);
                        switchTo(State.DUMPING, true);
                        return;
                    }
                }
            }
        } else
        {
            switchTo(State.IDLE, false);
        }
    }

    /**
     * @param world world to check in
     * @param pos   position to check
     * @return true if the block is air
     */
    private boolean isAir(IBlockReader world, BlockPos pos)
    {
        return world.getBlockState(pos).isAir(world, pos);
    }

    /**
     * @param world world to check in
     * @param pos   position to check
     * @return true if the block is a log
     */
    private boolean isLog(IBlockReader world, BlockPos pos)
    {
        return BlockTags.LOGS.contains(world.getBlockState(pos).getBlock());
    }

    /**
     * @param world world to check in
     * @param pos   position to check
     * @return true if the block is a leaf
     */
    private boolean isLeaf(IBlockReader world, BlockPos pos)
    {
        return BlockTags.LEAVES.contains(world.getBlockState(pos).getBlock());
    }

    /**
     * @param stack stack to check
     * @return true if the item in the stack is a sapling (is in the {@link ItemTags#SAPLINGS sapling tag})
     */
    private boolean isSapling(ItemStack stack)
    {
        return ItemTags.SAPLINGS.getAllElements().contains(stack.getItem());
    }

    /**
     * @param amount the amount of power to consume
     * @return whether the power was successfully consumed
     */
    private boolean consumePower(int amount)
    {
        populateCaps();
        if (energyCapability.isPresent() && battery.extractEnergy(amount, true) == amount)
        {
            battery.extractEnergy(amount, false);
            return true;
        }
        return false;
    }

    /**
     * @return whether the power was successfully consumed
     */
    private boolean consumeBlockScanPower()
    {
        return consumePower(TREE_FARM_CONFIG.blockScanEnergy.get());
    }

    /**
     * @return whether the power was successfully consumed
     */
    private boolean consumeInvScanPower()
    {
        return consumePower(TREE_FARM_CONFIG.inventoryScanEnergy.get());
    }

    /**
     * @return whether the power was successfully consumed
     */
    private boolean consumeLogBreakPower()
    {
        return consumePower(TREE_FARM_CONFIG.logBreakEnergy.get());
    }

    /**
     * @return whether the power was successfully consumed
     */
    private boolean consumeLeafBreakPower()
    {
        return consumePower(TREE_FARM_CONFIG.leafBreakEnergy.get());
    }

    /**
     * @param state   state to switch to
     * @param success whether the currentPosition state was successful
     */
    private void switchTo(State state, boolean success)
    {
        if (state == State.IDLE)
        {
            lastIdleTime = world.getGameTime();
        }
        if (state == currentState)
        {
            return;
        }
        currentState = state;
        LOGGER.trace(String.format("Tree farm at %s switched to %s", pos.toString(), currentState.toString()));
        if (success)
        {
            workingState = state;
        }
    }

    /**
     * @param side the side to get the item handler from
     * @return the item handler that was on that side of the block
     */
    private LazyOptional<IItemHandler> getItemHandler(Direction side)
    {
        final TileEntity tileEntity = world.getTileEntity(pos.offset(side));
        consumePower(1);
        return tileEntity == null ? LazyOptional.empty() : tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, side.getOpposite());
    }

    /**
     * The types of block this can break
     */
    enum BlockType
    {
        LOG, LEAF
    }

    /**
     * The states that this block can be in
     */
    private enum State
    {
        IDLE, SCANNING, BREAKING_LEAVES, BREAKING_LOGS, REPLANTING, DUMPING
    }
}
