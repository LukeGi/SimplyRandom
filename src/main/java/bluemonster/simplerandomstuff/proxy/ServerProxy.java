package bluemonster.simplerandomstuff.proxy;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class ServerProxy implements IProxy {
    @Override
    public void registerModel(ItemStack stack, ResourceLocation location, String variant) {
        /* NO OPERATION */
    }
}
