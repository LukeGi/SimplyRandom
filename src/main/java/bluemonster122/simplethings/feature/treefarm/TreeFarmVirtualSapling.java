package bluemonster122.simplethings.feature.treefarm;

import net.minecraft.block.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TreeFarmVirtualSapling
{
    BlockPlanks.EnumType TYPE;
    boolean harvest = false;
    boolean isMega;
    int minHeight;
    int chanceHeight;
    int additionalChanceHeight;

    public TreeFarmVirtualSapling(BlockPlanks.EnumType TYPE, boolean isMega, int minHeight, int chanceHeight, int additionalChanceHeight)
    {
        this.TYPE = TYPE;
        this.isMega = isMega && (TYPE == BlockPlanks.EnumType.SPRUCE || TYPE == BlockPlanks.EnumType.JUNGLE);
        this.minHeight = minHeight;
        this.chanceHeight = chanceHeight;
        this.additionalChanceHeight = additionalChanceHeight;
    }

    public List<ItemStack> getTreeGrown(Random random)
    {
        List<ItemStack> ret = new ArrayList<>();

        // ---------------- HANDLES THE LOGS ----------------
        int logBlocks = minHeight * (TYPE == BlockPlanks.EnumType.DARK_OAK ? 4 : megaModifier()) + (chanceHeight > 0 ? random.nextInt(chanceHeight) : 1) * (TYPE == BlockPlanks.EnumType.DARK_OAK ? 4 : megaModifier()) + (additionalChanceHeight > 0 ? random.nextInt(additionalChanceHeight) : 0);
        IBlockState logState = getLogState();

        for (; logBlocks >= 0; logBlocks--)
        {
            logState.getBlock().getDrops(null, null, logState, 0).forEach(s -> ret.add(s));
        }

        // ---------------- HANDLES THE LEAVES ----------------
        IBlockState leavesState = getLeavesState();
        for (int i = 30 + random.nextInt(20) * megaModifier(); i >= 0; i--)
        {
            leavesState.getBlock().getDrops(null, null, leavesState, 0).forEach(s -> ret.add(s));
            if ((TYPE == BlockPlanks.EnumType.DARK_OAK || TYPE == BlockPlanks.EnumType.OAK) && random.nextInt(200) == 0)
            {
                ret.add(new ItemStack(Items.APPLE, 1));
            }
        }

        return ret;
    }

    private IBlockState getLogState()
    {
        switch (TYPE)
        {
            case BIRCH:
                return Blocks.LOG.getDefaultState().withProperty(BlockOldLog.VARIANT, TYPE);
            case SPRUCE:
                return Blocks.LOG.getDefaultState().withProperty(BlockOldLog.VARIANT, TYPE);
            case JUNGLE:
                return Blocks.LOG.getDefaultState().withProperty(BlockOldLog.VARIANT, TYPE);
            case DARK_OAK:
                return Blocks.LOG2.getDefaultState().withProperty(BlockNewLog.VARIANT, TYPE);
            case ACACIA:
                return Blocks.LOG2.getDefaultState().withProperty(BlockNewLog.VARIANT, TYPE);
            case OAK:
            default:
                return Blocks.LOG.getDefaultState().withProperty(BlockOldLog.VARIANT, TYPE);
        }
    }

    private IBlockState getLeavesState()
    {
        switch (TYPE)
        {
            case BIRCH:
                return Blocks.LEAVES.getDefaultState().withProperty(BlockOldLeaf.VARIANT, TYPE);
            case SPRUCE:
                return Blocks.LEAVES.getDefaultState().withProperty(BlockOldLeaf.VARIANT, TYPE);
            case JUNGLE:
                return Blocks.LEAVES.getDefaultState().withProperty(BlockOldLeaf.VARIANT, TYPE);
            case DARK_OAK:
                return Blocks.LEAVES2.getDefaultState().withProperty(BlockNewLeaf.VARIANT, TYPE);
            case ACACIA:
                return Blocks.LEAVES2.getDefaultState().withProperty(BlockNewLeaf.VARIANT, TYPE);
            case OAK:
            default:
                return Blocks.LEAVES.getDefaultState().withProperty(BlockOldLeaf.VARIANT, TYPE);
        }
    }

    private int megaModifier()
    {
        return isMega ? 4 : 1;
    }

    public static int getMinHeight(BlockPlanks.EnumType type, boolean isMega)
    {
        switch (type)
        {
            case OAK:
            default:
                return 4;
            case BIRCH:
                return 3;
            case SPRUCE:
                return isMega ? 12 : 6;
            case JUNGLE:
                return isMega ? 15 : 7;
            case DARK_OAK:
                return 6;
            case ACACIA:
                return 5;
        }
    }

    public static int getChanceHeight(BlockPlanks.EnumType type, boolean isMega, Random rand)
    {
        switch (type)
        {
            case OAK:
            default:
                return rand.nextInt(12) == 4 ? rand.nextInt(3) : rand.nextInt(8);
            case BIRCH:
                return rand.nextInt(4);
            case SPRUCE:
                return rand.nextInt(5);
            case JUNGLE:
                return rand.nextInt(7);
            case DARK_OAK:
                return rand.nextInt(3);
            case ACACIA:
                return rand.nextInt(3);
        }
    }

    public static int getAddChanceHeight(BlockPlanks.EnumType type, boolean isMega, Random rand)
    {
        switch (type)
        {
            case OAK:
            default:
                return rand.nextInt(6);
            case BIRCH:
                return rand.nextInt(3);
            case SPRUCE:
                return isMega ? rand.nextInt(2) : 0;
            case JUNGLE:
                return isMega ? rand.nextInt(18) : 2;
            case DARK_OAK:
                return rand.nextInt(3) + rand.nextInt(4);
            case ACACIA:
                return rand.nextInt(4);
        }
    }

    public int getType()
    {
        return TYPE.ordinal();
    }

    public BlockPlanks.EnumType getTYPE()
    {
        return TYPE;
    }

    public boolean isMega()
    {
        return isMega;
    }

    public void setHarvest(boolean b)
    {
        harvest = b;
    }

    public boolean shouldHarvest()
    {
        return harvest;
    }
}
