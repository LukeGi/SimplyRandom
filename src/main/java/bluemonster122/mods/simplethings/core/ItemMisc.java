package bluemonster122.mods.simplethings.core;

import bluemonster122.mods.simplethings.core.block.IEnumMeta;
import bluemonster122.mods.simplethings.core.block.IPickup;
import bluemonster122.mods.simplethings.item.ItemST;
import bluemonster122.mods.simplethings.reference.ModInfo;
import buildcraft.api.tools.IToolWrench;
import cofh.item.IToolHammer;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.Optional.Interface;
import net.minecraftforge.fml.common.eventhandler.Event.Result;

@Interface(modid = "BuildCraftAPI|core", iface = "buildcraft.api.tools.IToolWrench")
public class ItemMisc extends ItemST implements IToolHammer, IToolWrench {
    public ItemMisc() {
        super("misc", true);
    }

    @Override
    public String getUnlocalizedName(ItemStack itemStack) {
        return String.format("item.%s:%s.%s", ModInfo.MOD_ID, "misc", Types.byMeta(itemStack.getMetadata()).getName());
    }

    @Override
    public void getSubItems(Item item, CreativeTabs tab, NonNullList<ItemStack> itemList) {
        for (Types type : Types.VARIANTS) {
            itemList.add(new ItemStack(item, 1, type.getMeta()));
        }
    }

    @Override
    public EnumActionResult onItemUseFirst(EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {
        IBlockState state = world.getBlockState(pos);
        Block block = state.getBlock();

        if (world.isAirBlock(pos)) {
            return EnumActionResult.PASS;
        }
        PlayerInteractEvent event = new PlayerInteractEvent.RightClickBlock(player, hand, pos, side, new Vec3d(hitX, hitY, hitZ));
        if (MinecraftForge.EVENT_BUS.post(event) || event.getResult() == Result.DENY) {
            return EnumActionResult.PASS;
        }
        if (!world.isRemote && isUsable(player.getHeldItem(hand), player, pos) && player.isSneaking() && block instanceof IPickup) {
            ((IPickup) block).pickup(world, pos, state, player);
            return EnumActionResult.SUCCESS;
        }
        return EnumActionResult.PASS;
    }

    @Override
    public int getItemStackLimit(ItemStack stack) {
        int limit;
        switch (Types.byMeta(stack.getMetadata())) {
            case WRENCH:
                limit = 1;
                break;
            default:
                limit = 64;
                break;
        }
        return limit;
    }

    /* IToolHammer */
    @Override
    public boolean isUsable(ItemStack item, EntityLivingBase user, BlockPos pos) {
        switch (Types.byMeta(item.getMetadata())) {
            case WRENCH:
                return true;
            default:
                return false;
        }
    }

    @Override
    public boolean isUsable(ItemStack item, EntityLivingBase user, Entity entity) {
        switch (Types.byMeta(item.getMetadata())) {
            case WRENCH:
                return true;
            default:
                return false;
        }
    }

    @Override
    public void toolUsed(ItemStack item, EntityLivingBase user, BlockPos pos) {

    }

    @Override
    public void toolUsed(ItemStack item, EntityLivingBase user, Entity entity) {

    }

    /* IToolWrench */
    @Override
    public boolean canWrench(EntityPlayer player, EnumHand hand, ItemStack wrench, RayTraceResult rayTrace) {
        switch (Types.byMeta(wrench.getMetadata())) {
            case WRENCH:
                return true;
            default:
                return false;
        }
    }

    @Override
    public void wrenchUsed(EntityPlayer player, EnumHand hand, ItemStack wrench, RayTraceResult rayTrace) {

    }

    public enum Types implements IEnumMeta, Comparable<Types> {
        WRENCH;

        protected static final Types[] VARIANTS = values();
        private int meta;

        public static Types byMeta(int meta) {
            return VARIANTS[Math.abs(meta) % VARIANTS.length];
        }

        @Override
        public int getMeta() {
            return meta;
        }

        Types() {
            meta = ordinal();
        }
    }
}
