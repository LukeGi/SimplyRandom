package luhegi.mods.simplyrandom.basis.data;

import luhegi.mods.simplyrandom.SimplyRandom;
import luhegi.mods.simplyrandom.basis.setup.ModSetupManager;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.common.data.LanguageProvider;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

public class BasisLangProvider extends LanguageProvider {
    public static final BasisLangProvider EN_US = new BasisLangProvider("en_us");
    private final String locale;

    public BasisLangProvider(String locale) {
        super(ModSetupManager.INSTANCE.generator, SimplyRandom.ID, locale);
        this.locale = locale;
    }

    private Set<Consumer<BasisLangProvider>> registerCallbacks = new HashSet<>();


    public void addCallback(Consumer<BasisLangProvider> callback) {
        registerCallbacks.add(callback);
    }

    @Override
    protected void addTranslations() {
        registerCallbacks.parallelStream().forEach(callback -> callback.accept(this));
        registerCallbacks.clear();
    }

    @Override
    public String getName() {
        return "Simply Random Lang Provider - " + locale;
    }

    public void addTooltip(Block block, String tooltipText) {
        add(block.getTranslationKey() + ".tooltip", tooltipText);
    }

    public void addTooltip(Item item, String tooltipText) {
        add(item.getTranslationKey() + ".tooltip", tooltipText);
    }
}
