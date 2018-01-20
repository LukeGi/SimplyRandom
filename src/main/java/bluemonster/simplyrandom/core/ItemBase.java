package bluemonster.simplyrandom.core;

import bluemonster.simplyrandom.RegistryHandler;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;

import javax.annotation.Nonnull;
import java.util.Arrays;

import static bluemonster.simplyrandom.ModInfo.MOD_ID;

public class ItemBase extends Item {

    public ItemBase(String name) {
        super();
        setup(name);
    }

    protected void setup(String name) {
        ResourceLocation good_name = new ResourceLocation(MOD_ID, name.toLowerCase().replace(' ', '_'));
        setRegistryName(good_name);
        setUnlocalizedName(good_name.toString());
        registerModels();
    }

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
        if (Arrays.asList(getCreativeTabs()).contains(tab)) {
            items.add(getItemStack());
        }
    }

    public void registerModels() {
        RegistryHandler.MODELS_TO_REGISTER.add(() -> registerModel(0, "item"));
    }

    public void registerModel(int meta, String variant) {
        assert getRegistryName() != null;
        ModelLoader.setCustomModelResourceLocation(this, meta, new ModelResourceLocation(getRegistryName(), "variant=" + variant));
    }

    @Nonnull
    public ItemStack getItemStack() {
        return getItemStack(1);
    }

    @Nonnull
    public ItemStack getItemStack(int count) {
        return new ItemStack(this, count);
    }
}
