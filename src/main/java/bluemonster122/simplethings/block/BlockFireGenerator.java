package bluemonster122.simplethings.block;

import bluemonster122.simplethings.SimpleThings;
import bluemonster122.simplethings.tileentity.TileFireGenerator;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import java.util.List;

public class BlockFireGenerator extends Block implements ITileEntityProvider
{
    public BlockFireGenerator()
    {
        super(Material.ROCK);
        setRegistryName(SimpleThings.MOD_ID, "energy_generator_fire");
        setUnlocalizedName(getRegistryName().getResourcePath());
        setCreativeTab(SimpleThings.theTab);
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List<String> tooltip, boolean advanced)
    {
        super.addInformation(stack, player, tooltip, advanced);
        tooltip.add("Generates 1 Energy / Tick, when fire is 2 above.");
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta)
    {
        return new TileFireGenerator();
    }
}
