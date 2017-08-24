package bluemonster.simplerandomstuff.core;

import bluemonster.simplerandomstuff.SimpleRandomStuff;
import bluemonster.simplerandomstuff.config.Configs;
import bluemonster.simplerandomstuff.registry.RegistryHandler;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public abstract class BlockCore extends Block {

    public BlockCore(String name, Material blockMaterialIn, MapColor blockMapColorIn) {
        super(blockMaterialIn, blockMapColorIn);
        if (isEnabled() && Configs.CORE.shouldLoad) {
            setup(name);
        }
    }

    public BlockCore(String name, Material materialIn) {
        this(name, materialIn, materialIn.getMaterialMapColor());
    }

    public BlockCore(String name) {
        this(name, Material.CLOTH);
    }

    protected void setup(String name) {
        setRegistryName(SimpleRandomStuff.MOD_ID, name);
        setUnlocalizedName(getRegistryName().getResourceDomain() + "." + name);
        setCreativeTab(SimpleRandomStuff.Tab);
    }

    public void register() throws IllegalAccessException {
        if (isEnabled() && Configs.CORE.shouldLoad) {
            switch (RegistryHandler.currentStage){
                case NONE:
                    throw new IllegalAccessException("BlockCore#register was called without a registry stage being in progress.");
                case BLOCKS:
                    ForgeRegistries.BLOCKS.register(this);
                    break;
                case ITEMS:
                    ForgeRegistries.ITEMS.register(createItemBlock());
                    break;
                case MODELS:
                    SimpleRandomStuff.proxy.registerModel(new ItemStack(this), this.getRegistryName(), "inventory");
                    break;
            }
        }
    }

    public ItemBlock createItemBlock() {
        return new ItemBlockCore(this);
    }

    public ItemStack createItemStack(IBlockState state) {
        return new ItemStack(state.getBlock(), 1, getMetaFromState(state));
    }

    protected abstract boolean isEnabled();
}
