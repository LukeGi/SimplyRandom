package bluemonster122.mods.simplethings.tanks;

import bluemonster122.mods.simplethings.SimpleThings;
import bluemonster122.mods.simplethings.block.IEnumMeta;
import bluemonster122.mods.simplethings.reference.ModInfo;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemTankUpgrade extends Item {
    public ItemTankUpgrade() {
        super();
        setRegistryName("tank_upgrade");
        setHasSubtypes(true);
        setCreativeTab(SimpleThings.theTab);
    }


    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (world.isRemote) return EnumActionResult.SUCCESS;
        IBlockState state = world.getBlockState(pos);
        ItemStack stack = player.getHeldItem(hand);
        if (state.getBlock().equals(TankRegistry.tank) && !(EnumTankUpgrade.BASE.getMeta() == stack.getMetadata())) {
            switch (state.getValue(BlockTank.VARIANT)) {
                case GLASS:
                    if (EnumTankUpgrade.GLASS_TO_IRON.getMeta() == stack.getMetadata()){
                        world.setBlockState(pos, state.withProperty(BlockTank.VARIANT, BlockTank.EnumTankType.IRON), 3);
                        stack.shrink(1);
                        player.setHeldItem(hand, stack);
                    }
                    break;
                case IRON:
                    if (EnumTankUpgrade.IRON_TO_GOLD.getMeta() == stack.getMetadata()){
                        world.setBlockState(pos, state.withProperty(BlockTank.VARIANT, BlockTank.EnumTankType.GOLD), 3);
                        stack.shrink(1);
                        player.setHeldItem(hand, stack);
                    }
                    break;
                case GOLD:
                    if (EnumTankUpgrade.GOLD_TO_OBSIDIAN.getMeta() == stack.getMetadata()){
                        world.setBlockState(pos, state.withProperty(BlockTank.VARIANT, BlockTank.EnumTankType.OBSIDIAN), 3);
                        stack.shrink(1);
                        player.setHeldItem(hand, stack);
                    }
                    break;
                case OBSIDIAN:
                    if (EnumTankUpgrade.OBSIDIAN_TO_DIAMOND.getMeta() == stack.getMetadata()){
                        world.setBlockState(pos, state.withProperty(BlockTank.VARIANT, BlockTank.EnumTankType.DIAMOND), 3);
                        stack.shrink(1);
                        player.setHeldItem(hand, stack);
                    }
                    break;
                case DIAMOND:
                    break;
            }
        }
        return EnumActionResult.SUCCESS;
    }

    @Override
    public String getUnlocalizedName(ItemStack itemStack) {
        return String.format("item.%s:%s", ModInfo.MOD_ID, "tank_upgrade" + EnumTankUpgrade.byMeta(itemStack.getMetadata()).getName());
    }

    @Override
    public void getSubItems(Item item, CreativeTabs tab, NonNullList<ItemStack> itemList) {
        for (ItemTankUpgrade.EnumTankUpgrade type : ItemTankUpgrade.EnumTankUpgrade.values()) {
            itemList.add(new ItemStack(item, 1, type.getMeta()));
        }
    }

    public enum EnumTankUpgrade implements IEnumMeta {
        BASE,
        GLASS_TO_IRON,
        IRON_TO_GOLD,
        GOLD_TO_OBSIDIAN,
        OBSIDIAN_TO_DIAMOND;

        protected static final EnumTankUpgrade[] VARIANTS = values();
        private int meta;

        EnumTankUpgrade() {
            meta = ordinal();
        }

        public static EnumTankUpgrade byMeta(int meta) {
            return VARIANTS[Math.abs(meta) % VARIANTS.length];
        }

        @Override
        public int getMeta() {
            return meta;
        }
    }
}
