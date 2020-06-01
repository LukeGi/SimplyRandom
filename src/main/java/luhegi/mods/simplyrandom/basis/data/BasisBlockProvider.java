package luhegi.mods.simplyrandom.basis.data;

import luhegi.mods.simplyrandom.SimplyRandom;
import luhegi.mods.simplyrandom.basis.setup.ModSetupManager;
import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ModelFile;

import javax.annotation.Nonnull;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

public class BasisBlockProvider extends BlockStateProvider {
    public static final BasisBlockProvider INSTANCE = new BasisBlockProvider();

    private BasisBlockProvider() {
        super(ModSetupManager.INSTANCE.generator, SimplyRandom.ID, ModSetupManager.INSTANCE.existingFileHelper);
    }

    private final Set<Consumer<BasisBlockProvider>> registerCallbacks = new HashSet<>();

    public void addCallback(Consumer<BasisBlockProvider> callback) {
        registerCallbacks.add(callback);
    }

    @Override
    protected void registerStatesAndModels() {
        registerCallbacks.parallelStream().forEach(callback -> callback.accept(this));
        registerCallbacks.clear();
    }

    public ModelFile.ExistingModelFile getBlockModel(Block block) {
        return models().getExistingFile(modLoc("block/" + block.getRegistryName().getPath()));
    }

    public ResourceLocation tex(String id, String ext) {
        return modLoc("block/" + id + "_" + ext);
    }

    @Nonnull
    @Override
    public String getName() {
        return "Simply Random Block Provider";
    }
}
