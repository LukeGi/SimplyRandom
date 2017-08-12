package bluemonster.simplerandomstuff.cobblegen;

import bluemonster.simplerandomstuff.core.block.BlockSRS;
import bluemonster.simplerandomstuff.reference.Names;
import bluemonster.simplerandomstuff.util.IFeatureRegistry;
import bluemonster.simplerandomstuff.util.ModelHelpers;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;

import static bluemonster.simplerandomstuff.util.ModelHelpers.registerBlockModelAsItem;

public class FRCobbleGen
        implements IFeatureRegistry {
    public static final FRCobbleGen INSTANCE = new FRCobbleGen();
    public static int Cobble_RF = 0;
    public static BlockSRS cobblestone_generator = new BlockCobblestoneGenerator();

    @Override
    public void registerBlocks(IForgeRegistry<Block> registry) {
        registry.registerAll(cobblestone_generator);
    }

    @Override
    public void registerItems(IForgeRegistry<Item> registry) {
        registry.registerAll(cobblestone_generator.createItemBlock());
    }

    @Override
    public void registerTileEntities() {
        GameRegistry.registerTileEntity(TileCobblestoneGenerator.class, "simplerandomstuff:cobblestone_generator");
    }

    @Override
    public void loadConfigs(Configuration configuration) {
        Cobble_RF = configuration.getInt(
                Names.Features.Configs.COBBLE_GEN_RF_COST,
                Names.Features.COBBLESTONE_GENERATOR,
                0,
                0,
                1000,
                "If set to 0, the cobblestone is free."
        );
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
        ModelHelpers.registerBlockModelAsItem(cobblestone_generator);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerClientEvents() {

    }

    @Override
    public String getName() {
        return Names.Features.COBBLESTONE_GENERATOR;
    }

    private FRCobbleGen() {
    }
}
