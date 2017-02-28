package bluemonster122.mods.simplethings.util;

import com.google.common.collect.ImmutableSet;
import net.minecraft.util.math.BlockPos;

import java.util.Set;

public enum AreaType {
    AROUND, SMALL, MEDIUM, LARGE, XL, XXL;
    public static final AreaType[] VALUES = new AreaType[]{SMALL, MEDIUM, LARGE, XL, XXL};

    public static int getIndex(AreaType type) {
        switch (type) {
            case SMALL:
                return 0;
            case MEDIUM:
                return 1;
            case LARGE:
                return 2;
            case XL:
                return 3;
            case XXL:
                return 4;
            default:
                return 0;
        }
    }

    public static AreaType getFromIndex(int index) {
        return VALUES[index];
    }

    public Set<BlockPos> getArea() {
        if (this.equals(AROUND)) {
            return ImmutableSet.of(new BlockPos(-1, 0, -1), new BlockPos(-1, 0, 0), new BlockPos(-1, 0, 1),
                    new BlockPos(0, 0, -1), new BlockPos(0, 0, 0), new BlockPos(0, 0, 1),
                    new BlockPos(1, 0, -1), new BlockPos(1, 0, 0), new BlockPos(1, 0, 1)
            );
        }
        return ImmutableSet.of(new BlockPos(-3, 0, -3), new BlockPos(-2, 0, -3), new BlockPos(-1, 0, -3),
                new BlockPos(0, 0, -3), new BlockPos(1, 0, -3), new BlockPos(2, 0, -3),
                new BlockPos(3, 0, -3), new BlockPos(-3, 0, -2), new BlockPos(-2, 0, -2),
                new BlockPos(-1, 0, -2), new BlockPos(0, 0, -2), new BlockPos(1, 0, -2),
                new BlockPos(2, 0, -2), new BlockPos(3, 0, -2), new BlockPos(-3, 0, -1),
                new BlockPos(-2, 0, -1), new BlockPos(-1, 0, -1), new BlockPos(0, 0, -1),
                new BlockPos(1, 0, -1), new BlockPos(2, 0, -1), new BlockPos(3, 0, -1),
                new BlockPos(-3, 0, 0), new BlockPos(-2, 0, 0), new BlockPos(-1, 0, 0),
                new BlockPos(1, 0, 0), new BlockPos(2, 0, 0), new BlockPos(3, 0, 0),
                new BlockPos(-3, 0, 1), new BlockPos(-2, 0, 1), new BlockPos(-1, 0, 1),
                new BlockPos(0, 0, 1), new BlockPos(1, 0, 1), new BlockPos(2, 0, 1),
                new BlockPos(3, 0, 1), new BlockPos(-3, 0, 2), new BlockPos(-2, 0, 2),
                new BlockPos(-1, 0, 2), new BlockPos(0, 0, 2), new BlockPos(1, 0, 2),
                new BlockPos(2, 0, 2), new BlockPos(3, 0, 2), new BlockPos(-3, 0, 3),
                new BlockPos(-2, 0, 3), new BlockPos(-1, 0, 3), new BlockPos(0, 0, 3),
                new BlockPos(1, 0, 3), new BlockPos(2, 0, 3), new BlockPos(3, 0, 3)
        );
    }
}
