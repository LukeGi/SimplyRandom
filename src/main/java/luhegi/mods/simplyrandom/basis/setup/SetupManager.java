package luhegi.mods.simplyrandom.basis.setup;

import luhegi.mods.simplyrandom.SimplyRandom;
import luhegi.mods.simplyrandom.basis.data.BasisBlockProvider;
import luhegi.mods.simplyrandom.basis.data.BasisItemProvider;
import luhegi.mods.simplyrandom.basis.data.BasisLangProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;

public abstract class SetupManager implements ISetupManager {
    private ResourceLocation resLoc;

    protected SetupManager() {
        resLoc = new ResourceLocation(SimplyRandom.ID, getID());
    }

    /**
     * Generic register method
     *
     * @param registry The register to register to
     * @param value    The value to register
     * @param <R>      The type of the Registry
     * @param <V>      The type of the value
     * @return The registered value, with the assigned registry name.
     */
    protected <R extends IForgeRegistryEntry<R>, V extends R> V register(IForgeRegistry<R> registry, V value) {
        registry.register(value.setRegistryName(resLoc));
        return value;
    }

    @Override
    public void generateData() {
        getBlockData().addCallback(this::generateBlockData);
        getItemData().addCallback(this::generateItemData);
        getEnUsData().addCallback(this::generateEnUsLangData);
    }

    @Override
    public void onClientConfig(ForgeConfigSpec.Builder spec) {
        spec.push(getName());
        addClientConfigs(spec);
        spec.pop();
    }

    @Override
    public void onServerConfig(ForgeConfigSpec.Builder spec) {
        spec.push(getName());
        addServerConfigs(spec);
        spec.pop();
    }

    protected abstract void generateBlockData(BasisBlockProvider provider);
    protected abstract void generateItemData(BasisItemProvider provider);
    protected abstract void generateEnUsLangData(BasisLangProvider provider);
    protected abstract void addClientConfigs(ForgeConfigSpec.Builder spec);
    protected abstract void addServerConfigs(ForgeConfigSpec.Builder spec);

    protected static BasisBlockProvider getBlockData() {
        return BasisBlockProvider.INSTANCE;
    }

    protected static BasisItemProvider getItemData() {
        return BasisItemProvider.INSTANCE;
    }

    protected static BasisLangProvider getEnUsData() {
        return BasisLangProvider.EN_US;
    }
}
