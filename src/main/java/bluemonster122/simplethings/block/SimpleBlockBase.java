package bluemonster122.simplethings.block;

import bluemonster122.simplethings.SimpleThings;
import bluemonster122.simplethings.item.SimpleItemBlockBase;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class SimpleBlockBase extends Block
{
    public SimpleBlockBase(Material materialIn, String name)
    {
        super(materialIn);
        setRegistryName(SimpleThings.MOD_ID, name);
        setUnlocalizedName(getRegistryName().toString());
        setCreativeTab(SimpleThings.theTab);
    }

    public ItemBlock getItemBlock()
    {
        return new SimpleItemBlockBase(this);
    }

    @SideOnly(Side.CLIENT)
    public boolean addExtraInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced)
    {
        return false;
    }
}
