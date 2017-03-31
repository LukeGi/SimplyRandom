package bluemonster122.mods.simplethings;

import bluemonster122.mods.simplethings.block.*;
import bluemonster122.mods.simplethings.handler.ConfigurationHandler;
import bluemonster122.mods.simplethings.handler.GuiHandler;
import bluemonster122.mods.simplethings.item.ItemSpear;
import bluemonster122.mods.simplethings.network.message.MessageParticle;
import bluemonster122.mods.simplethings.proxy.IProxy;
import bluemonster122.mods.simplethings.tab.CreativeTabST;
import bluemonster122.mods.simplethings.tileentity.*;
import bluemonster122.mods.simplethings.util.IInitialize;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
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

@Mod.EventBusSubscriber
@Mod(modid = SimpleThings.MOD_ID, version = SimpleThings.VERSION, guiFactory = SimpleThings.GUI_FACTORY_CLASS, updateJSON = SimpleThings.UPDATE_JSON)
public class SimpleThings implements IInitialize {

    public static final String MOD_ID = "simplethings";
    public static final String VERSION = "@VERSION@";
    public static final String MOD_DIR = "bluemonster122.mods.simplethings.";
    public static final String GUI_FACTORY_CLASS = MOD_DIR + "client.GuiFactory";
    public static final String SERVER_PROXY_CLASS = MOD_DIR + "proxy.ServerProxy";
    public static final String CLIENT_PROXY_CLASS = MOD_DIR + "proxy.ClientProxy";
    public static final String UPDATE_JSON = "https://github.com/bluemonster122/SimpleThings/blob/master/update.json";
    private static final String CHANNEL = "simplethings";
    @Instance(value = SimpleThings.MOD_ID)
    public static SimpleThings INSTANCE;

    @SidedProxy(clientSide = SimpleThings.CLIENT_PROXY_CLASS, serverSide = SimpleThings.SERVER_PROXY_CLASS)
    public static IProxy proxy;

    public static Logger logger;

    public static SimpleNetworkWrapper channel;

    public static CreativeTabs theTab = new CreativeTabST();

    public static BlockST tree_farm;
    public static BlockST tree_farm_new;
    public static BlockST cobblestone_generator;
    public static BlockST generators;
    public static BlockST power_cable;
    public static BlockST machine_block;
    public static BlockST power_storage;
    public static Item wooden_spear;
    public static Item stone_spear;
    public static Item iron_spear;
    public static Item gold_spear;
    public static Item diamond_spear;

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        event.getRegistry().registerAll(
                tree_farm = new BlockTreeFarm(),
                tree_farm = new BlockTreeFarmNew(),
                cobblestone_generator = new BlockCobblestoneGenerator(),
                generators = new BlockGenerator("generator"),
                power_cable = new BlockPowerCable(),
                machine_block = new BlockMachineBlock(),
                power_storage = new BlockPowerStorage("simple")
        );
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        event.getRegistry().registerAll(
                tree_farm.createItemBlock(),
                cobblestone_generator.createItemBlock(),
                generators.createItemBlock(),
                power_cable.createItemBlock(),
                machine_block.createItemBlock(),
                power_storage.createItemBlock(),
                wooden_spear = new ItemSpear("wooden_spear").setMaterial(ItemSpear.SpearMaterial.WOOD),
                stone_spear = new ItemSpear("stone_spear").setMaterial(ItemSpear.SpearMaterial.STONE),
                iron_spear = new ItemSpear("iron_spear").setMaterial(ItemSpear.SpearMaterial.IRON),
                gold_spear = new ItemSpear("gold_spear").setMaterial(ItemSpear.SpearMaterial.GOLD),
                diamond_spear = new ItemSpear("diamond_spear").setMaterial(ItemSpear.SpearMaterial.DIAMOND)
        );
    }

    public static ResourceLocation location(String path) {
        return new ResourceLocation(MOD_ID, path);
    }

    public static void registerTileEntities() {
        GameRegistry.registerTileEntityWithAlternatives(TileTreeFarm.class, tree_farm.getRegistryName().toString(), "tileTreeFarm");
        GameRegistry.registerTileEntityWithAlternatives(TileCobblestoneGenerator.class, cobblestone_generator.getRegistryName().toString(), "tileCobblestoneGenerator");
        GameRegistry.registerTileEntityWithAlternatives(TileGeneratorFire.class, generators.getRegistryName().toString() + "tileGeneratorFire");
        GameRegistry.registerTileEntityWithAlternatives(TileGeneratorSugar.class, generators.getRegistryName().toString() + "tileTreeFarm");
        GameRegistry.registerTileEntityWithAlternatives(TileLightningRod.class, generators.getRegistryName().toString() + "tileLightningRod");
        GameRegistry.registerTileEntityWithAlternatives(TilePowerCable.class, power_cable.getRegistryName().toString(), "tilePowerCable");
        GameRegistry.registerTileEntityWithAlternatives(TilePowerStorage.class, power_storage.getRegistryName().toString(), "tilePowerStorage");
    }

    @Override
    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();
        ConfigurationHandler.init(event.getSuggestedConfigurationFile());
        setupNetwork();
    }

    private void setupNetwork() {
        logger.info(">>> Registering network channel...");

        channel = NetworkRegistry.INSTANCE.newSimpleChannel(CHANNEL);
        int id = 0;
        channel.registerMessage(MessageParticle.MessageHandler.class, MessageParticle.class, id++, Side.CLIENT);
    }

    @Override
    @EventHandler
    public void init(FMLInitializationEvent event) {
        NetworkRegistry.INSTANCE.registerGuiHandler(SimpleThings.INSTANCE, new GuiHandler());
        registerTileEntities();
        GameRegistry.addShapedRecipe(new ItemStack(tree_farm, 1), "SAS", "IOI", "SAS", 'S',
                new ItemStack(Blocks.SAPLING, 1, OreDictionary.WILDCARD_VALUE), 'A',
                new ItemStack(Items.IRON_AXE, 1), 'I', new ItemStack(Blocks.IRON_BLOCK, 1), 'O',
                new ItemStack(Blocks.OBSIDIAN, 1)
        );
        GameRegistry.addShapedRecipe(new ItemStack(cobblestone_generator, 1), "PPP", "WCL", "PPP", 'W',
                new ItemStack(Items.WATER_BUCKET, 1), 'C', new ItemStack(Blocks.COBBLESTONE, 1),
                'L', new ItemStack(Items.LAVA_BUCKET, 1), 'P', new ItemStack(Items.IRON_PICKAXE, 1)
        );
    }

    @Override
    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
    }
}
