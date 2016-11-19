package bluemonster122.simplethings.block;

import bluemonster122.simplethings.item.SimpleItemBlockBase;
import bluemonster122.simplethings.reference.Names;
import bluemonster122.simplethings.tileentity.TileEnergyGeneratorFire;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class BlockEnergyGeneratorFire extends SimpleBlockBase implements ITileEntityProvider
{
    public BlockEnergyGeneratorFire()
    {
        super(Material.ROCK, Names.ENERGY_GENERATOR_FIRE);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean addExtraInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced)
    {
        SimpleItemBlockBase.addStringToTooltip(I18n.format("simplethings.tooltip." + getUnlocalizedName() +".extra"), tooltip);
        return true;
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta)
    {
        return new TileEnergyGeneratorFire();
    }
}
