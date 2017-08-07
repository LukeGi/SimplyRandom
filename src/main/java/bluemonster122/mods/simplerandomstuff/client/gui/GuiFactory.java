package bluemonster122.mods.simplerandomstuff.client.gui;

import bluemonster122.mods.simplerandomstuff.client.gui.configs.SimpleThingsConfigGui;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.client.IModGuiFactory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Set;

@SideOnly(Side.CLIENT)
public class GuiFactory implements IModGuiFactory {
    @Override
    public void initialize(Minecraft minecraftInstance) {
    }

    @Override
    public boolean hasConfigGui( ) {
        return false;
    }

    @Override
    public GuiScreen createConfigGui(GuiScreen parentScreen) {
        return null;
    }

    @Override
    public Class<? extends GuiScreen> mainConfigGuiClass( ) {
        return SimpleThingsConfigGui.class;
    }

    @Override
    public Set<RuntimeOptionCategoryElement> runtimeGuiCategories( ) {
        return null;
    }

    @SuppressWarnings("deprecation")
    @Override
    public RuntimeOptionGuiHandler getHandlerFor(RuntimeOptionCategoryElement element) {
        return null;
    }
}
