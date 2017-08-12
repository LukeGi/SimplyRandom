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
public class ElementCobblestoneGenerator
        extends GuiConfigEntries.CategoryEntry {
    @Override
    protected GuiScreen buildChildScreen() {
        return new GuiConfig(
                this.owningScreen,
                (new ConfigElement(ConfigurationHandler.INSTANCE.configuration.getCategory("cobble_gen"))).getChildElements(),
                this.owningScreen.modID,
                "cobble_gen",
                this.configElement.requiresWorldRestart() || this.owningScreen.allRequireWorldRestart,
                this.configElement.requiresMcRestart() || this.owningScreen.allRequireMcRestart,
                GuiConfig.getAbridgedConfigPath(ForgeModContainer.getConfig()
                        .toString())
        );
    }

    public ElementCobblestoneGenerator(
            GuiConfig owningScreen,
            GuiConfigEntries owningEntryList,
            IConfigElement configElement
    ) {
        super(owningScreen, owningEntryList, configElement);
    }
}
