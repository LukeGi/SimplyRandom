package luhegi.mods.simplyrandom.basis.data;

import luhegi.mods.simplyrandom.SimplyRandom;
import luhegi.mods.simplyrandom.basis.setup.ModSetupManager;
import net.minecraft.block.Block;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

public class BasisItemProvider extends ItemModelProvider {
    public static final BasisItemProvider INSTANCE = new BasisItemProvider();

    public BasisItemProvider() {
        super(ModSetupManager.INSTANCE.generator, SimplyRandom.ID, ModSetupManager.INSTANCE.existingFileHelper);
    }

    private final Set<Consumer<BasisItemProvider>> registerCallbacks = new HashSet<>();

    public void addCallback(Consumer<BasisItemProvider> callback) {
        registerCallbacks.add(callback);
    }

    public ModelFile.ExistingModelFile getBlockModel(Block block) {
        return getExistingFile(modLoc("block/" + block.getRegistryName().getPath()));
    }

    @Override
    protected void registerModels() {
        registerCallbacks.parallelStream().forEach(callback -> callback.accept(this));
        registerCallbacks.clear();
    }

    @Override
    public String getName() {
        return "Simply Random Item Provider";
    }
}
