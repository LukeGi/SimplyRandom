package bluemonster122.simplethings.feature.treefarm;

import bluemonster122.simplethings.SimpleThings;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.function.Predicate;

public class ItemBlockTreeFarm extends ItemBlock {
    public ItemBlockTreeFarm(Block block) {
        super(block);
        setRegistryName(SimpleThings.MOD_ID, "simpletreefarm");
        setUnlocalizedName(getRegistryName().getResourcePath());
        setCreativeTab(CreativeTabs.TOOLS);
    }

    @Override
    public boolean onBlockStartBreak(ItemStack itemstack, BlockPos pos, EntityPlayer player) {
        if (!player.isCreative()) return false;
        World world = player.worldObj;
        if (world.isRemote) return false;
        Block blockIn = world.getBlockState(pos).getBlock();
        Predicate<Block> woodTest = theBlock -> theBlock == Blocks.LOG || theBlock == Blocks.LOG2;
        Predicate<Block> leafTest = theBlock -> theBlock == Blocks.LEAVES || theBlock == Blocks.LEAVES2;
        if (woodTest.test(blockIn)) {
            int i = 0;
            while (i++ < 100) {
                Block current = world.getBlockState(pos.up(i)).getBlock();
                if (!woodTest.test(current)) {
                    if (leafTest.test(current)) {
                        break;
                    }
                }
            }
            if (i == 100 || i > 100) {
                return false;
            }
            Stack<BlockPos> todo = new Stack<>();
            List<BlockPos> broken = new ArrayList<>();
            todo.push(pos);
            while (!todo.isEmpty()) {
                BlockPos current = todo.pop();
                world.destroyBlock(current, false);
                broken.add(current);
                for (EnumFacing facing : EnumFacing.VALUES) {
                    BlockPos offsetPos = current.offset(facing);
                    Block t = world.getBlockState(offsetPos).getBlock();
                    if (!woodTest.test(t) && !leafTest.test(t))
                        continue;
                    if (!broken.contains(offsetPos) && !todo.contains(offsetPos)) {
                        todo.push(offsetPos);
                    }
                }
            }
            return true;
        }
        return false;
    }
}
