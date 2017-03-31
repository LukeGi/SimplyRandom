package bluemonster122.mods.simplethings.tileentity;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class TreeFarmer {
    private BlockPos pos;
    private TileTreeFarmNew farmNew;
    private World world;
    private List<EntityItem> drops;

    public TreeFarmer(BlockPos pos, TileTreeFarmNew farmNew, World world) {
        this.pos = pos;
        this.farmNew = farmNew;
        this.world = world;
        this.drops = new ArrayList<>();
    }

    public boolean cutTree() {
    }

    public boolean plantSapling() {
    }

    public boolean shouldCutTree() {
    }

    public boolean shouldPlantSapling() {
    }

    public boolean hasDrops() {
    }

    public boolean handleDrops() {

    }

    public boolean addBlocksToChop(List<BlockPos> cutList) {

    }
}
