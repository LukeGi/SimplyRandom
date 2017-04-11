package bluemonster122.mods.simplethings.core.block;

import bluemonster122.mods.simplethings.core.FRCore;
import bluemonster122.mods.simplethings.core.ItemMisc;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemHandlerHelper;

import java.util.List;

public interface IPickup {
    default boolean pickup(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand) {
        if (!worldIn.isRemote) {
            ItemStack stack = playerIn.getHeldItem(hand);
            if (stack.getItem().equals(FRCore.INSTANCE.misc) && stack.getMetadata() == ItemMisc.Types.WRENCH.getMeta()) {
                List<ItemStack> drops = state.getBlock().getDrops(worldIn, pos, state, 0);
                worldIn.destroyBlock(pos, false);
                for (ItemStack drop : drops) {
                    ItemHandlerHelper.giveItemToPlayer(playerIn, drop);
                }
                return true;
            }
        }
        return false;
    }
}
