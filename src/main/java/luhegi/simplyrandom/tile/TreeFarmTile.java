package luhegi.simplyrandom.tile;

import com.google.common.collect.ImmutableSet;
import com.mojang.authlib.GameProfile;
import com.mojang.datafixers.util.Pair;
import luhegi.simplyrandom.SimplyRandom;
import luhegi.simplyrandom.util.LazyOptionalHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.IGrowable;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.*;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

public class TreeFarmTile extends TileEntity implements ITickableTileEntity {
    private static final Direction[] VALID_STORAGE_SIDES = new Direction[]{Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST, Direction.DOWN};

    private BlockState AIR_STATE = Blocks.AIR.getDefaultState();

    private int tick = -1;
    private int maxBreak = 5;

    private List<BlockPos> logList = new ArrayList<>();
    private List<BlockPos> leavesList = new ArrayList<>();
    private Stack<BlockPos> searchNodes = new Stack<>();

    private int furthestFirst(BlockPos pos1, BlockPos pos2) {
        return pos2.manhattanDistance(pos) - pos1.manhattanDistance(pos);
    }

    public TreeFarmTile() {
        super(SimplyRandom.instance.tree_farm_tile.get());
    }

    private LazyOptional<IItemHandler> inventory = LazyOptional.empty();

    private LazyOptional<IItemHandler> getInventory() {
        if (!inventory.isPresent()) {
            for (Direction side : VALID_STORAGE_SIDES) {
                TileEntity tileEntity = getWorld().getTileEntity(getPos().offset(side));
                if (tileEntity == null || tileEntity instanceof TreeFarmTile) {
                    continue;
                }
                LazyOptional<IItemHandler> capability = tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, side.getOpposite());
                if (capability.isPresent()) {
                    inventory = capability;
                    break;
                }
            }
        }
        return inventory;
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return LazyOptionalHelper.findFirst(ImmutableSet.of(
                () -> CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.orEmpty(cap, getInventory()),
                () -> super.getCapability(cap, side)
        ));
    }

    @Override
    public void tick() {
        if (tick == -1) {
            tick = getWorld().getRandom().nextInt(20);
        }
        if (getWorld().isRemote() || getWorld().getGameTime() <= tick) {
            return;
        }

        BlockPos above = getPos().up();

        System.out.println(searchNodes.size());
        if (plantSapling(above))  {
            tick = (int) (getWorld().getGameTime() + 100);
            return;
        }

        if (applyGrowthJuice(above)) {
            tick = (int) (getWorld().getGameTime() + 200);
            return;
        }

        if (performSearch(above)) {
            tick = (int) (getWorld().getGameTime() + 1);
            return;
        }

        if (breakBlocks(getWorld(), above)) {
            tick = (int) (getWorld().getGameTime() + 20);
            return;
        }

        tick = (int) (getWorld().getGameTime() + 300);
    }

    private boolean performSearch(BlockPos above) {
        if (searchNodes.isEmpty() && leavesList.isEmpty() && logList.isEmpty()) {
            searchNodes.push(above);
            return true;
        } else if (!searchNodes.isEmpty()) {
            while (!searchNodes.isEmpty() && search(searchNodes.pop()));
            if (searchNodes.isEmpty()) {
                leavesList.sort(this::furthestFirst);
                logList.sort(this::furthestFirst);
            }
            return true;
        }
        return false;
    }

    private boolean applyGrowthJuice(BlockPos above) {
        if (getWorld().hasBlockState(above, aboveState -> !BlockTags.LOGS.contains(aboveState.getBlock()))) {
            BlockState aboveState = getWorld().getBlockState(above);
            if (BlockTags.SAPLINGS.contains(aboveState.getBlock()) && aboveState.getBlock() instanceof IGrowable) {
                ((IGrowable) aboveState.getBlock()).grow(getWorld(), getWorld().rand, above, aboveState);
            }
            return true;
        }
        return false;
    }

    private boolean plantSapling(BlockPos above) {
        if (getWorld().isAirBlock(above)) {
            getInventory().ifPresent(itemHandler -> {
                for (int i = 0; i < itemHandler.getSlots(); i++) {
                    ItemStack itemStack = itemHandler.getStackInSlot(i);
                    Item item = itemStack.getItem();
                    if (item instanceof BlockItem && ItemTags.SAPLINGS.contains(item)) {
                        BlockItem blockItem = (BlockItem) item;
                        BlockRayTraceResult fakeRayTrace = new BlockRayTraceResult(new Vec3d(pos).add(0.5, 1, 0.5), Direction.UP, pos, false);
                        FakePlayer fakePlayer = new FakePlayer(((ServerWorld) getWorld()), new GameProfile(UUID.nameUUIDFromBytes("LuHeGi Fake Player".getBytes()), "LuHeGi Fake Player"));
                        if (blockItem.tryPlace(new BlockItemUseContext(new ItemUseContext(fakePlayer, Hand.MAIN_HAND, fakeRayTrace))) == ActionResultType.SUCCESS) {
                            itemHandler.extractItem(i, 1, false);
                            break;
                        }
                    }
                }
            });
            return getWorld().isAirBlock(above);
        }
        return false;
    }

    private boolean breakBlocks(World world, BlockPos above) {
        List<ItemStack> drops = new ArrayList<>();
        int broken = tryBreak(tryBreak(0, drops, leavesList, BlockTags.LEAVES), drops, logList, BlockTags.LOGS);
        handleBlockDrops(world, above, drops);
        return broken > 0;
    }

    private int tryBreak(int broken, List<ItemStack> drops, List<BlockPos> blockPosList, Tag<Block> validityTag) {
        if (!blockPosList.isEmpty()) {
            Iterator<BlockPos> logIterator = blockPosList.iterator();
            while (logIterator.hasNext() && broken < maxBreak) {
                BlockPos logPos = logIterator.next();
                if (getWorld().hasBlockState(logPos, state -> validityTag.contains(state.getBlock()))) {
                    broken++;
                    BlockState logState = getWorld().getBlockState(logPos);
                    drops.addAll(Block.getDrops(logState, (ServerWorld) getWorld(), logPos, null));
                    getWorld().destroyBlock(logPos, false);
                }
                logIterator.remove();
            }
        }
        return broken;
    }

    private void handleBlockDrops(World world, BlockPos above, List<ItemStack> drops) {
        if (!drops.isEmpty()) {
            Vec3d vec3d = new Vec3d(above).add(0.5, 0.5, 0.5);
            drops.stream()
                    .map(stack -> getInventory().map(itemHandler -> ItemHandlerHelper.insertItem(itemHandler, stack.copy(), false)).orElse(stack))
                    .filter(stack -> !stack.isEmpty())
                    .map(stack -> new ItemEntity(world, vec3d.x, vec3d.y, vec3d.z, stack))
                    .peek(entity -> entity.setVelocity(0, 0, 0))
                    .forEach(world::addEntity);
        }
    }

    private boolean search(BlockPos pos) {
        BlockState state = getWorld().getBlockState(pos);
        if (BlockTags.LOGS.contains(state.getBlock())) {
            logList.add(pos);
        } else if (BlockTags.LEAVES.contains(state.getBlock())) {
            leavesList.add(pos);
        } else {
            return true;
        }
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                for (int k = -1; k <= 1; k++) {
                    BlockPos q = pos.add(i, j, k);
                    if (logList.contains(q) || leavesList.contains(q) || searchNodes.contains(q)) {
                        continue;
                    }
                    if (getWorld().hasBlockState(q, qState -> BlockTags.LOGS.contains(qState.getBlock()) || BlockTags.LEAVES.contains(qState.getBlock()))) {
                        searchNodes.push(q);
                    }
                }
            }
        }
        return false;
    }

    private void breakBlock(BlockPos pos, BlockState state) {
        if (BlockTags.LOGS.contains(state.getBlock())) {
            logList.add(pos);
        } else if (BlockTags.LEAVES.contains(state.getBlock())) {
            leavesList.add(pos);
        } else {
            return;
        }
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                for (int k = -1; k <= 1; k++) {
                    BlockPos q = pos.add(i, j, k);
                    if (logList.contains(q) || leavesList.contains(q)) {
                        continue;
                    }
                    BlockState qState = getWorld().getBlockState(q);
                    if (BlockTags.LOGS.contains(qState.getBlock()) || BlockTags.LEAVES.contains(qState.getBlock())) {
                        breakBlock(q, qState);
                    }
                }
            }
        }
    }
}
