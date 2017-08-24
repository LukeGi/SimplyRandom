package bluemonster.simplerandomstuff.core;

import bluemonster.simplerandomstuff.config.Configs;
import bluemonster.simplerandomstuff.registry.RegistryHandler;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

import javax.annotation.Nullable;

public abstract class BlockCoreTile extends BlockCore {

    public BlockCoreTile(String name, Material blockMaterialIn, MapColor blockMapColorIn) {
        super(name, blockMaterialIn, blockMapColorIn);
    }

    public BlockCoreTile(String name, Material materialIn) {
        super(name, materialIn);
    }

    public BlockCoreTile(String name) {
        super(name);
    }

    @Override
    public void register() throws IllegalAccessException {
        super.register();
        if (isEnabled() && Configs.CORE.shouldLoad && RegistryHandler.currentStage == RegistryHandler.CurrentStage.BLOCKS) {
            GameRegistry.registerTileEntity(getTileClass(), getRegistryName().toString());
        }
    }

    protected abstract Class<? extends TileEntity> getTileClass();

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return stateHasTile(state);
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return getNewTile(world, state);
    }

    protected abstract TileEntity getNewTile(World world, IBlockState state);

    protected boolean stateHasTile(IBlockState state) {
        return true;
    }
}
