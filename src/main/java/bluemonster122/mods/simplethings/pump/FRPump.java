package bluemonster122.mods.simplethings.pump;

import bluemonster122.mods.simplethings.core.block.BlockST;
import bluemonster122.mods.simplethings.util.IFeatureRegistry;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import static bluemonster122.mods.simplethings.util.ModelHelpers.registerBlockModelAsItem;

public class FRPump implements IFeatureRegistry {

    public static final FRPump INSTANCE = new FRPump();

    public BlockST pump = new BlockPump();

    @Override
    public void registerBlocks() {
        GameRegistry.register(pump);
    }

    @Override
    public void registerItems() {
        GameRegistry.register(pump.createItemBlock());
    }

    @Override
    public void registerRecipes() {

    }

    @Override
    public void registerTileEntities() {
        GameRegistry.registerTileEntity(TilePump.class, "simplethings:pump");
    }

    @Override
    public void loadConfigs(Configuration configuration) {

    }

    @Override
    public void registerEvents() {

    }

    @Override
    public void registerOreDict() {

    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerRenders() {
        registerBlockModelAsItem(pump);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerClientEvents() {

    }

    private FRPump() {
    }
}
