package luhegi.mods.simplyrandom.basis.objects;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.NetworkTagManager;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EmptyTickList;
import net.minecraft.world.ITickList;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.MapData;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FakeWorld extends World {
    private final ServerWorld seed;
    private final Map<BlockPos, BlockState> initial = new HashMap<>();
    private final Map<BlockPos, BlockState> current = new HashMap<>();

    public FakeWorld(ServerWorld base) {
        super(base.getWorldInfo(), base.dimension.getType(), (a, b) -> base.getChunkProvider(), base.getProfiler(), base.isRemote);
        seed = base;
    }

    public void reset() {
        initial.clear();
        current.clear();
    }

    public List<Pair<BlockPos, BlockState>> getTree() {
        List<Pair<BlockPos, BlockState>> tree = new ArrayList<>();
        current.forEach((pos, state) -> {
            if (BlockTags.LEAVES.contains(state.getBlock()) || BlockTags.LOGS.contains(state.getBlock())) {
                tree.add(Pair.of(pos, state));
            }
        });
        return tree;
    }

    @Override
    public boolean setBlockState(BlockPos pos, BlockState newState, int flags) {
        current.put(pos, newState);
        return true;
    }

    @Override
    public BlockState getBlockState(BlockPos pos) {
        return current.computeIfAbsent(pos, p -> {
            BlockState state = seed.getBlockState(p);
            initial.put(p, state);
            return state;
        });
    }


    @Override
    public void notifyBlockUpdate(BlockPos pos, BlockState oldState, BlockState newState, int flags) {

    }

    @Override
    public void playSound(@Nullable PlayerEntity player, double x, double y, double z, SoundEvent soundIn, SoundCategory category, float volume, float pitch) {

    }

    @Override
    public void playMovingSound(@Nullable PlayerEntity playerIn, Entity entityIn, SoundEvent eventIn, SoundCategory categoryIn, float volume, float pitch) {

    }

    @Nullable
    @Override
    public Entity getEntityByID(int id) {
        return null;
    }

    @Nullable
    @Override
    public MapData getMapData(String mapName) {
        return null;
    }

    @Override
    public void registerMapData(MapData mapDataIn) {

    }

    @Override
    public int getNextMapId() {
        return 0;
    }

    @Override
    public void sendBlockBreakProgress(int breakerId, BlockPos pos, int progress) {

    }

    @Override
    public Scoreboard getScoreboard() {
        return null;
    }

    @Override
    public RecipeManager getRecipeManager() {
        return null;
    }

    @Override
    public NetworkTagManager getTags() {
        return null;
    }

    @Override
    public ITickList<Block> getPendingBlockTicks() {
        return new EmptyTickList<>();
    }

    @Override
    public ITickList<Fluid> getPendingFluidTicks() {
        return new EmptyTickList<>();
    }

    @Override
    public void playEvent(@Nullable PlayerEntity player, int type, BlockPos pos, int data) {

    }

    @Override
    public List<? extends PlayerEntity> getPlayers() {
        return null;
    }

    @Override
    public Biome getNoiseBiomeRaw(int x, int y, int z) {
        return null;
    }
}
