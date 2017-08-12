package bluemonster.simplerandomstuff.client.gui.configs;

import bluemonster.simplerandomstuff.handler.ConfigurationHandler;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.ForgeModContainer;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.GuiConfigEntries;
import net.minecraftforge.fml.client.config.IConfigElement;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ElementTreeFarm
        extends GuiConfigEntries.CategoryEntry {
    @Override
    protected GuiScreen buildChildScreen() {
        return new GuiConfig(
                this.owningScreen,
                (new ConfigElement(ConfigurationHandler.INSTANCE.configuration.getCategory("tree_farm"))).getChildElements(),
                this.owningScreen.modID,
                "tree_farm",
                this.configElement.requiresWorldRestart() || this.owningScreen.allRequireWorldRestart,
                this.configElement.requiresMcRestart() || this.owningScreen.allRequireMcRestart,
                GuiConfig.getAbridgedConfigPath(ForgeModContainer.getConfig()
                        .toString())
        );
    }

    public ElementTreeFarm(GuiConfig owningScreen, GuiConfigEntries owningEntryList, IConfigElement configElement) {
        super(owningScreen, owningEntryList, configElement);
    }
}
