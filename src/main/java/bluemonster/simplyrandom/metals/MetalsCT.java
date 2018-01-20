package bluemonster.simplyrandom.metals;

import bluemonster.simplyrandom.ModInfo;
import bluemonster.simplyrandom.RegistryHandler;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public class MetalsCT extends CreativeTabs {

    public static final CreativeTabs INSTANCE = new MetalsCT();

    public MetalsCT() {
        super(ModInfo.MOD_ID + ":metals");
    }

    @Override
    public ItemStack getTabIconItem() {
        return RegistryHandler.Objects.COPPER_NUGGET.getItemStack();
    }
}
