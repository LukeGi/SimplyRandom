package bluemonster122.mods.simplethings.proxy;

import bluemonster122.mods.simplethings.SimpleThings;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

import static bluemonster122.mods.simplethings.util.ModelHelpers.registerBlockModelAsItem;
import static bluemonster122.mods.simplethings.util.ModelHelpers.registerItemModel;

@Mod.EventBusSubscriber(Side.CLIENT)
public class ClientProxy implements IProxy {
    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent event) {
        registerItemModel(SimpleThings.wooden_spear, 0, SimpleThings.location("spear"), "material=wood");
        registerItemModel(SimpleThings.stone_spear, 0, SimpleThings.location("spear"), "material=stone");
        registerItemModel(SimpleThings.iron_spear, 0, SimpleThings.location("spear"), "material=iron");
        registerItemModel(SimpleThings.gold_spear, 0, SimpleThings.location("spear"), "material=gold");
        registerItemModel(SimpleThings.diamond_spear, 0, SimpleThings.location("spear"), "material=diamond");
        registerBlockModelAsItem(SimpleThings.tree_farm);
        registerBlockModelAsItem(SimpleThings.cobblestone_generator);
        registerBlockModelAsItem(SimpleThings.lightning_rod);
        registerBlockModelAsItem(SimpleThings.power_generator_fire);
        registerBlockModelAsItem(SimpleThings.power_cable);
        registerBlockModelAsItem(SimpleThings.power_generator_sugar);
        registerBlockModelAsItem(SimpleThings.machine_block);
        registerBlockModelAsItem(SimpleThings.power_storage);
    }

    @Override
    public void preInit() {

    }
}
