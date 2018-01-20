package bluemonster.simplyrandom.core;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

import static bluemonster.simplyrandom.ModInfo.MOD_ID;

public class BlockBase extends Block {

    public BlockBase(Material material, String name) {
        super(material);
        setup(name);
    }

    public void setup(String name) {
        ResourceLocation registryName = new ResourceLocation(MOD_ID, name);
        setRegistryName(registryName);
        setUnlocalizedName(registryName.toString());
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
