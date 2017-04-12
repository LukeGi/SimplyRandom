package bluemonster122.mods.simplethings.core;

import bluemonster122.mods.simplethings.core.block.IEnumMeta;
import bluemonster122.mods.simplethings.item.ItemST;
import bluemonster122.mods.simplethings.reference.ModInfo;
import buildcraft.api.tools.IToolWrench;
import cofh.item.IToolHammer;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.fml.common.Optional.Interface;
import net.minecraftforge.fml.common.Optional.InterfaceList;
import net.minecraftforge.fml.common.Optional.Method;

@InterfaceList({
        @Interface(modid = "cofhapi|item", iface = "cofh.item.IToolHammer"),
        @Interface(modid = "BuildCraftAPI|core", iface = "buildcraft.api.tools.IToolWrench")
})
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

    /*          IToolHammer           */

    /**
     * Called to ensure that the tool can be used on a block.
     *
     * @param item The ItemStack for the tool. Not required to match equipped item (e.g., multi-tools that contain other tools).
     * @param user The entity using the tool.
     * @param pos  Coordinates of the block.
     * @return True if this tool can be used.
     */
    @Override
    @Method(modid = "cofhapi|item")
    public boolean isUsable(ItemStack item, EntityLivingBase user, BlockPos pos) {
        return true;
    }

    /**
     * Called to ensure that the tool can be used on an entity.
     *
     * @param item   The ItemStack for the tool. Not required to match equipped item (e.g., multi-tools that contain other tools).
     * @param user   The entity using the tool.
     * @param entity The entity the tool is being used on.
     * @return True if this tool can be used.
     */
    @Override
    @Method(modid = "cofhapi|item")
    public boolean isUsable(ItemStack item, EntityLivingBase user, Entity entity) {
        return false;
    }

    /**
     * Callback for when the tool has been used reactively.
     *
     * @param item The ItemStack for the tool. Not required to match equipped item (e.g., multi-tools that contain other tools).
     * @param user The entity using the tool.
     * @param pos  Coordinates of the block.
     */
    @Override
    @Method(modid = "cofhapi|item")
    public void toolUsed(ItemStack item, EntityLivingBase user, BlockPos pos) {

    }

    /**
     * Callback for when the tool has been used reactively.
     *
     * @param item   The ItemStack for the tool. Not required to match equipped item (e.g., multi-tools that contain other tools).
     * @param user   The entity using the tool.
     * @param entity The entity the tool is being used on.
     */
    @Override
    @Method(modid = "cofhapi|item")
    public void toolUsed(ItemStack item, EntityLivingBase user, Entity entity) {

    }

    /*          IToolWrench           */

    /*** Called to ensure that the wrench can be used.
     *
     * @param player - The player doing the wrenching
     * @param hand - Which hand was holding the wrench
     * @param wrench - The item stack that holds the wrench
     * @param rayTrace - The object that is being wrenched
     *
     * @return true if wrenching is allowed, false if not */
    @Override
    @Method(modid = "BuildCraftAPI|core")
    public boolean canWrench(EntityPlayer player, EnumHand hand, ItemStack wrench, RayTraceResult rayTrace) {
        return false;
    }

    /*** Callback after the wrench has been used. This can be used to decrease durability or for other purposes.
     *  @param player - The player doing the wrenching
     * @param hand - Which hand was holding the wrench

     * @param wrench - The item stack that holds the wrench

     * @param rayTrace - The object that is being wrenched   */
    @Override
    @Method(modid = "BuildCraftAPI|core")
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
