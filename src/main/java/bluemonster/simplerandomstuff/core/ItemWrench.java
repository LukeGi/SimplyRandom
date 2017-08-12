package bluemonster.simplerandomstuff.core;

import bluemonster.simplerandomstuff.core.block.IPickup;
import bluemonster.simplerandomstuff.reference.ModInfo;
import bluemonster.simplerandomstuff.reference.Names;
import cofh.api.item.IToolHammer;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.common.Optional.Interface;
import net.minecraftforge.fml.common.eventhandler.Event.Result;

import javax.annotation.Nullable;

@Interface(modid = "BuildCraftAPI|core", iface = "buildcraft.api.tools.IToolWrench")
public class ItemWrench
        extends ItemST
        implements IToolHammer {
    @Override
    public String getUnlocalizedName(ItemStack itemStack) {
        return String.format("item.%s.%s", ModInfo.MOD_ID, Names.Items.WRENCH);
    }

    @Nullable
    @Override
    public Item getContainerItem() {
        return this;
    }

    @Override
    public EnumActionResult onItemUseFirst(
            EntityPlayer player,
            World world,
            BlockPos pos,
            EnumFacing side,
            float hitX,
            float hitY,
            float hitZ,
            EnumHand hand
    ) {
        IBlockState state = world.getBlockState(pos);
        Block block = state.getBlock();

        if (world.isAirBlock(pos)) {
            return EnumActionResult.PASS;
        }
        PlayerInteractEvent event = new PlayerInteractEvent.RightClickBlock(
                player,
                hand,
                pos,
                side,
                new Vec3d(hitX, hitY, hitZ)
        );
        if (MinecraftForge.EVENT_BUS.post(event) || event.getResult() == Result.DENY) {
            return EnumActionResult.PASS;
        }
        if (!world.isRemote && isUsable(
                player.getHeldItem(hand),
                player,
                pos
        ) && player.isSneaking() && block instanceof IPickup) {
            ((IPickup) block).pickup(world, pos, state, player);
            return EnumActionResult.SUCCESS;
        }
        return EnumActionResult.PASS;
    }

    @Override
    public boolean hasContainerItem(ItemStack stack) {
        return true;
    }

    @Override
    public int getItemStackLimit(ItemStack stack) {
        return 1;
    }

    /* IToolHammer */
    @Override
    public boolean isUsable(ItemStack item, EntityLivingBase user, BlockPos pos) {
        return true;
    }

    @Override
    public boolean isUsable(ItemStack item, EntityLivingBase user, Entity entity) {
        return true;
    }

    @Override
    public void toolUsed(ItemStack item, EntityLivingBase user, BlockPos pos) {
        /* NO OPERATION */
    }

    @Override
    public void toolUsed(ItemStack item, EntityLivingBase user, Entity entity) {
        /* NO OPERATION */
    }

    /* IToolWrench */
    @Optional.Method(modid = "BuildCraftAPI|core")
    public boolean canWrench(EntityPlayer player, EnumHand hand, ItemStack wrench, RayTraceResult rayTrace) {
        return true;
    }

    @Optional.Method(modid = "BuildCraftAPI|core")
    public void wrenchUsed(EntityPlayer player, EnumHand hand, ItemStack wrench, RayTraceResult rayTrace) {
        /* NO OPERATION */
    }

    public ItemWrench() {
        super(Names.Items.WRENCH, false);
    }
}
