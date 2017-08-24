package bluemonster.simplerandomstuff.proxy;

import bluemonster.simplerandomstuff.registry.RegistryHandler.ClientRegistryHandler;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ClientProxy implements IProxy {

    @Override
    public void registerModel(ItemStack stack, ResourceLocation location, String variant) {
        ClientRegistryHandler.REGISTRY.put(stack, new ModelResourceLocation(location, variant));
    }
}
