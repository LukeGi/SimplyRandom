package bluemonster122.simplethings.feature.cobblegen;

import bluemonster122.simplethings.SimpleThings;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import java.util.List;

public class BlockCobblestoneGenerator extends Block implements ITileEntityProvider{
    public BlockCobblestoneGenerator() {
        super(Material.IRON);
        setRegistryName(SimpleThings.MOD_ID, "cobblestonegenerator");
        setUnlocalizedName(getRegistryName().getResourcePath());
        setCreativeTab(SimpleThings.theTab);
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List<String> tooltip, boolean advanced)
    {
        tooltip.add("Can provide cobblestone as fast as you can pull it.");
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityCobblestoneGenerator();
    }
}
