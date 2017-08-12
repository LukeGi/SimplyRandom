package bluemonster.simplerandomstuff.core.block;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemHandlerHelper;

public interface IPickup {
    default boolean pickup(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn) {
        NonNullList<ItemStack> drops = NonNullList.create();
        state.getBlock().getDrops(drops, worldIn, pos, state, 0);
        if (worldIn.destroyBlock(pos, false)) {
            for (ItemStack drop : drops) {
                ItemHandlerHelper.giveItemToPlayer(playerIn, drop);
            }
            return true;
        }
        return false;
    }
}
