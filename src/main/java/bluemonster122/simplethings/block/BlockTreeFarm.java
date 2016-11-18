package bluemonster122.simplethings.block;

import bluemonster122.simplethings.SimpleThings;
import bluemonster122.simplethings.tileentity.TileTreeFarm;
import bluemonster122.simplethings.handler.GuiHandler;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

import javax.annotation.Nonnull;
import java.util.List;

public class BlockTreeFarm extends Block implements ITileEntityProvider {

    public BlockTreeFarm() {
        super(Material.IRON);
        setRegistryName(SimpleThings.MOD_ID, "simpletreefarm");
        setUnlocalizedName(getRegistryName().getResourcePath());
        setCreativeTab(SimpleThings.theTab);
        setHarvestLevel("pickaxe", 3);
        setHardness(7);
        setResistance(500);
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List<String> tooltip, boolean advanced) {
        tooltip.add("Each sapling in this farm must have respective amounts of clear space above it, otherwise it will mess with the virtual chopping program.");
        tooltip.add("Farms a 7x7 area, this at the centre.");
        tooltip.add("Range of this is not currently expandable");
        tooltip.add("Can only place saplings on supported blocks" + (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) ? ", such as:" : ""));

        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            TileTreeFarm.ALLOWED_FARMING_BLOCKS.forEach(b -> tooltip.add("   *" + b.getLocalizedName()));
        } else {
            tooltip.add("Hold SHIFT for more information.");
        }
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        TileEntity tileEntity = worldIn.getTileEntity(pos);
        if (tileEntity instanceof TileTreeFarm) {
            TileTreeFarm farmTile = (TileTreeFarm) tileEntity;
            farmTile.dropInventory();
            farmTile.breakSaplings();
        }
        super.breakBlock(worldIn, pos, state);
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing heldItem, float side, float hitX, float hitY)
    {
        playerIn.openGui(SimpleThings.INSTANCE, GuiHandler.tree_farm_gui_id, worldIn, pos.getX(), pos.getY(), pos.getZ());
        return true;
    }

    @SideOnly(Side.CLIENT)
    @Nonnull
    @Override
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileTreeFarm();
    }
}
