package luhegi.mods.simplyrandom.basis.setup;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.registries.IForgeRegistry;

public interface ISetupManager {
    default void onConstruct() {
    }

    default void onClientConfig(ForgeConfigSpec.Builder spec) {
    }


    default void onServerConfig(ForgeConfigSpec.Builder spec) {
    }

    default void onSetup() {
    }

    default void registerBlocks(IForgeRegistry<Block> registry) {
    }

    default void registerItems(IForgeRegistry<Item> registry) {
    }

    default void registerTileType(IForgeRegistry<TileEntityType<?>> registry) {
    }

    default void generateData() {
    }

    /**
     * This function is used by registry methods, please make sure it complies with the rules of the path of a
     * {@link ResourceLocation}
     *
     * @return id of feature
     */
    String getID();

    /**
     * This function is used by the Data Generators and should return the english name of the block.
     *
     * @return name of the feature
     */
    String getName();
}
