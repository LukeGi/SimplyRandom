package bluemonster122.mods.simplethings.proxy;

import bluemonster122.mods.simplethings.SimpleThings;
import bluemonster122.mods.simplethings.treefarm.TreeFarmRegistry;
import bluemonster122.mods.simplethings.util.IFeatureRegistry;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import static bluemonster122.mods.simplethings.SimpleThings.featureRegistries;
import static bluemonster122.mods.simplethings.util.ModelHelpers.registerBlockModelAsItem;
import static bluemonster122.mods.simplethings.util.ModelHelpers.registerItemModel;

@Mod.EventBusSubscriber(Side.CLIENT)
@SideOnly(Side.CLIENT)
public class ClientProxy implements IProxy {
    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent event) {
        // Spears
        registerItemModel(SimpleThings.wooden_spear, 0, SimpleThings.location("spear"), "material=wood");
        registerItemModel(SimpleThings.stone_spear, 0, SimpleThings.location("spear"), "material=stone");
        registerItemModel(SimpleThings.iron_spear, 0, SimpleThings.location("spear"), "material=iron");
        registerItemModel(SimpleThings.gold_spear, 0, SimpleThings.location("spear"), "material=gold");
        registerItemModel(SimpleThings.diamond_spear, 0, SimpleThings.location("spear"), "material=diamond");

        // Machines
        registerBlockModelAsItem(SimpleThings.machine_block);

        // Generators
        registerBlockModelAsItem(SimpleThings.generators, 0, "type=fire");
        registerBlockModelAsItem(SimpleThings.generators, 1, "type=sugar");
        registerBlockModelAsItem(SimpleThings.generators, 2, "type=lightning_rod");

        // Power Blocks
        registerBlockModelAsItem(SimpleThings.power_cable);
        registerBlockModelAsItem(SimpleThings.power_storage);

        // Tank
        featureRegistries.forEach(IFeatureRegistry::registerRenders);
    }

    @Override
    public void preInit() {
        featureRegistries.forEach(IFeatureRegistry::registerClientEvents);
    }

    @Override
    public void init() {
//        StateMapper mapper = new StateMapper(ModInfo.MOD_ID, "tank", "tank");
//        ModelLoader.setCustomStateMapper(TankRegistry.tank, mapper);
    }
}
