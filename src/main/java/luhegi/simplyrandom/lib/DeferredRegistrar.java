package luhegi.simplyrandom.lib;

import com.google.common.collect.ImmutableSet;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;

import java.util.HashSet;
import java.util.TreeMap;

public class DeferredRegistrar<T extends IForgeRegistryEntry<T>> {
    private TreeMap<String, DeferredRegister<T>> REGISTERS;

    private ForgeRegistry<T> registry;

    public DeferredRegistrar(ForgeRegistry<T> registry) {
        this.registry = registry;
    }

    private ForgeRegistry<T> getRegistry() {
        return registry;
    }

    public static class TileTypes {
//        public static final TileEntityType<MyTile> MY_TILE_TYPE = new TileEntityType<>(MyTile::new, ImmutableSet.of(MyBlock), null);
    }
}
