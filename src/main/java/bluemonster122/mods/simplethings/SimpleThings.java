package bluemonster122.mods.simplethings;

import bluemonster122.mods.simplethings.block.*;
import bluemonster122.mods.simplethings.cobblegen.CobbleGenRegistry;
import bluemonster122.mods.simplethings.handler.ConfigurationHandler;
import bluemonster122.mods.simplethings.handler.GuiHandler;
import bluemonster122.mods.simplethings.item.ItemSpear;
import bluemonster122.mods.simplethings.network.message.MessageParticle;
import bluemonster122.mods.simplethings.proxy.IProxy;
import bluemonster122.mods.simplethings.reference.ModInfo;
import bluemonster122.mods.simplethings.tab.CreativeTabST;
import bluemonster122.mods.simplethings.tanks.TankRegistry;
import bluemonster122.mods.simplethings.tileentity.*;
import bluemonster122.mods.simplethings.treefarm.TreeFarmRegistry;
import bluemonster122.mods.simplethings.util.IFeatureRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockPlanks;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.oredict.OreDictionary;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber
@Mod(modid = ModInfo.MOD_ID, version = ModInfo.VERSION, guiFactory = ModInfo.GUI_FACTORY_CLASS, updateJSON = ModInfo.UPDATE_JSON)
public class SimpleThings {
    @Instance(value = ModInfo.MOD_ID)
    public static SimpleThings INSTANCE;

    @SidedProxy(clientSide = ModInfo.CLIENT_PROXY_CLASS, serverSide = ModInfo.SERVER_PROXY_CLASS)
    public static IProxy proxy;

    public static Logger logger;

    public static SimpleNetworkWrapper channel;

    public static CreativeTabs theTab = new CreativeTabST();

    public static List<IFeatureRegistry> featureRegistries = new ArrayList<>();

    static {
        featureRegistries.add(TankRegistry.INSTANCE);
        featureRegistries.add(TreeFarmRegistry.INSTANCE);
        featureRegistries.add(CobbleGenRegistry.INSTANCE);
    }

    public static BlockST generators;
    public static BlockST power_cable;
    public static BlockST machine_block;
    public static BlockST power_storage;
    public static BlockST test_block;
    public static Item wooden_spear;
    public static Item stone_spear;
    public static Item iron_spear;
    public static Item gold_spear;
    public static Item diamond_spear;

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        event.getRegistry().registerAll(
                generators = new BlockGenerator("generator"),
                power_cable = new BlockPowerCable(),
                machine_block = new BlockMachineBlock(),
                power_storage = new BlockPowerStorage("simple"),
                test_block = new BlockTest()
        );
        featureRegistries.forEach(IFeatureRegistry::registerBlocks);
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        event.getRegistry().registerAll(
                generators.createItemBlock(),
                power_cable.createItemBlock(),
                machine_block.createItemBlock(),
                power_storage.createItemBlock(),
                test_block.createItemBlock(),
                wooden_spear = new ItemSpear("wooden_spear").setMaterial(ItemSpear.SpearMaterial.WOOD),
                stone_spear = new ItemSpear("stone_spear").setMaterial(ItemSpear.SpearMaterial.STONE),
                iron_spear = new ItemSpear("iron_spear").setMaterial(ItemSpear.SpearMaterial.IRON),
                gold_spear = new ItemSpear("gold_spear").setMaterial(ItemSpear.SpearMaterial.GOLD),
                diamond_spear = new ItemSpear("diamond_spear").setMaterial(ItemSpear.SpearMaterial.DIAMOND)
        );
        featureRegistries.forEach(IFeatureRegistry::registerItems);
    }

    public static ResourceLocation location(String path) {
        return new ResourceLocation(ModInfo.MOD_ID, path);
    }

    public static void registerTileEntities() {
        GameRegistry.registerTileEntityWithAlternatives(TileGeneratorFire.class, generators.getRegistryName().toString() + "tileGeneratorFire");
        GameRegistry.registerTileEntityWithAlternatives(TileGeneratorSugar.class, generators.getRegistryName().toString() + "tileTreeFarm");
        GameRegistry.registerTileEntityWithAlternatives(TileLightningRod.class, generators.getRegistryName().toString() + "tileLightningRod");
        GameRegistry.registerTileEntityWithAlternatives(TilePowerCable.class, power_cable.getRegistryName().toString(), "tilePowerCable");
        GameRegistry.registerTileEntityWithAlternatives(TilePowerStorage.class, power_storage.getRegistryName().toString(), "tilePowerStorage");
        GameRegistry.registerTileEntityWithAlternatives(TileTest.class, test_block.getRegistryName().toString(), "tileTest");
        featureRegistries.forEach(IFeatureRegistry::registerTileEntities);
    }

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();
        ConfigurationHandler.init(event.getSuggestedConfigurationFile());
        featureRegistries.forEach(IFeatureRegistry::registerEvents);
        INSTANCE.setupNetwork();
        for (BlockPlanks.EnumType type : BlockPlanks.EnumType.values()) {
            String name = type.getName();
            OreDictionary.registerOre("sapling",new ItemStack(Blocks.SAPLING, 1, type.getMetadata()));
            OreDictionary.registerOre("sapling" + name.substring(0,1).toUpperCase() + name.substring(1), new ItemStack(Blocks.SAPLING, 1, type.getMetadata()));
        }
        proxy.preInit();
    }

    private void setupNetwork() {
        logger.info(">>> Registering network channel...");

        channel = NetworkRegistry.INSTANCE.newSimpleChannel(ModInfo.CHANNEL);
        int id = 0;
        channel.registerMessage(MessageParticle.MessageHandler.class, MessageParticle.class, id++, Side.CLIENT);
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init();
        NetworkRegistry.INSTANCE.registerGuiHandler(SimpleThings.INSTANCE, new GuiHandler());
        registerTileEntities();
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        featureRegistries.forEach(IFeatureRegistry::registerRecipes);
    }

}
