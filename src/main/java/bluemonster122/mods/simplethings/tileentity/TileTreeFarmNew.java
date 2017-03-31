package bluemonster122.mods.simplethings.tileentity;

import bluemonster122.mods.simplethings.SimpleThings;
import bluemonster122.mods.simplethings.reference.Names;
import com.mojang.authlib.GameProfile;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import sun.plugin.dom.exception.InvalidStateException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TileTreeFarmNew extends TileEntity implements ITickable {

    private static final String TREE_FARM_PLAYER_NAME = String.format("%s:%s", SimpleThings.MOD_ID, Names.TREE_FARM);
    private static final GameProfile profile = new GameProfile(UUID.fromString(TREE_FARM_PLAYER_NAME), TREE_FARM_PLAYER_NAME);
    private static final Vec3i[] farmedPositions = new Vec3i[]{new Vec3i(-3, 0, -3), new Vec3i(-3, 0, -2), new Vec3i(-2, 0, -3), new Vec3i(-2, 0, -2), new Vec3i(-3, 0, 3), new Vec3i(-3, 0, 2), new Vec3i(-2, 0, 3), new Vec3i(-2, 0, 2), new Vec3i(3, 0, -3), new Vec3i(3, 0, -2), new Vec3i(2, 0, -3), new Vec3i(2, 0, -2), new Vec3i(3, 0, 3), new Vec3i(3, 0, 2), new Vec3i(2, 0, 3), new Vec3i(2, 0, 2)};

    private boolean requiresSync = true;
    private int currentPos = -1;

    public EntityPlayerMP fakePlayer;

    @Override
    public void update() {
        getWorld().theProfiler.startSection(Names.TREE_FARM);
        if (getWorld().isRemote) {
            getWorld().theProfiler.startSection("clientUpdate");
            updateClient();
            getWorld().theProfiler.endSection();
        } else {
            getWorld().theProfiler.startSection("serverUpdate");
            // Do the server stuff

            // Initialize the Fake Player if it is null
            if (fakePlayer == null) {
                fakePlayer = new FakePlayer(FMLCommonHandler.instance().getMinecraftServerInstance().worldServerForDimension(getWorld().provider.getDimension()), profile);
            }
            // Do the Work needed on the server side
            try {
                requiresSync |= updateServer();
            } catch (Exception e) {
                e.printStackTrace();
            }

            // If the tile needs to sync it's data
            if (requiresSync) {
                getWorld().theProfiler.startSection("sendingClientUpdate");
                // Reset the sync boolean
                requiresSync = false;
                // Mark the block for a client update
                IBlockState state = getWorld().getBlockState(getPos());
                getWorld().notifyBlockUpdate(pos, state, state, 3);
                // Mark the block's current state to be saved
                markDirty();
                getWorld().theProfiler.endSection();
            }
            getWorld().theProfiler.endSection();
        }
        getWorld().theProfiler.endSection();
    }

    private boolean updateServer() throws Exception {
        if (currentPos == -1) {
            currentPos = getWorld().rand.nextInt(farmedPositions.length);
            return false;
        }
        BlockPos current = getPos().add(farmedPositions[currentPos]);
        TreeFarmer farmer = new TreeFarmer(current, this, getWorld());

        List<BlockPos> cutList = new ArrayList<>();
        if (farmer.shouldCutTree()) if (farmer.cutTree()) if (farmer.handleDrops()) requiresSync |= farmer.addBlocksToChop(cutList);
        if (requiresSync = false) {
            cutList.clear();
        } else {
            BlockCutter.addBlocks(cutList);
        }

        if (farmer.shouldPlantSapling()) if (farmer.plantSapling()) requiresSync |= true;

        farmer = null;
        return false;
    }

    private void updateClient() {

    }

    private class BlockCutter {
    }
}
