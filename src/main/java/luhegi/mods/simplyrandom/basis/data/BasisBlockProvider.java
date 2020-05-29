package luhegi.mods.simplyrandom.basis.data;

import luhegi.mods.simplyrandom.SimplyRandom;
import luhegi.mods.simplyrandom.basis.setup.ModSetupManager;
import net.minecraftforge.client.model.generators.BlockStateProvider;

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

    @Nonnull
    @Override
    public String getName() {
        return "Simply Random Block Provider";
    }
}
