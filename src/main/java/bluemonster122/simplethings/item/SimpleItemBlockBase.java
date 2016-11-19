package bluemonster122.simplethings.item;

import bluemonster122.simplethings.block.SimpleBlockBase;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

import java.util.Collections;
import java.util.List;

public class SimpleItemBlockBase extends ItemBlock
{
    public SimpleItemBlockBase(SimpleBlockBase block)
    {
        super(block);
        setRegistryName(block.getRegistryName());
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced)
    {
        addStringToTooltip(I18n.format("simplethings.tooltip." + getUnlocalizedName()), tooltip);
        if (!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT))
            addStringToTooltip(I18n.format("simplethings.tooltip.holdshift"), tooltip);
        else if (!((SimpleBlockBase) block).addExtraInformation(stack, playerIn, tooltip, advanced))
            addStringToTooltip(I18n.format("simplethings.tooltip.noextrainfo"), tooltip);
    }

    public static void addStringToTooltip(String s, List<String> tooltip)
    {
        Collections.addAll(tooltip, s.replaceAll("&", "\u00a7").split("<br>"));
    }
}
