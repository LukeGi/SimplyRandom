package bluemonster.simplerandomstuff.proxy;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public interface IProxy {
    void registerModel(ItemStack stack, ResourceLocation location, String variant);
}
