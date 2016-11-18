package bluemonster122.simplethings.handler;

import bluemonster122.simplethings.block.BlockCobblestoneGenerator;
import bluemonster122.simplethings.tileentity.TileCobblestoneGenerator;
import bluemonster122.simplethings.util.MiscEvents;
import bluemonster122.simplethings.block.BlockFireGenerator;
import bluemonster122.simplethings.tileentity.TileFireGenerator;
import bluemonster122.simplethings.block.BlockTreeFarm;
import bluemonster122.simplethings.tileentity.TileTreeFarm;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;

@Mod.EventBusSubscriber
public class RegistryHandler
{
    public static Block tree_farm = new BlockTreeFarm();
    public static Item item_tree_farm = new ItemBlock(tree_farm).setRegistryName(tree_farm.getRegistryName());

    public static Block cobblestone_generator = new BlockCobblestoneGenerator();
    public static Item item_cobblestone_generator = new ItemBlock(cobblestone_generator).setRegistryName(cobblestone_generator.getRegistryName());

    public static Block power_generator_fire = new BlockFireGenerator();
    public static Item item_power_generator_fire = new ItemBlock(power_generator_fire).setRegistryName(power_generator_fire.getRegistryName());

    @SubscribeEvent
    public static void regsiterBlocks(RegistryEvent.Register<Block> event)
    {
        event.getRegistry().registerAll(
                tree_farm,
                cobblestone_generator,
                power_generator_fire
        );
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event)
    {
        event.getRegistry().registerAll(
                item_tree_farm,
                item_cobblestone_generator,
                item_power_generator_fire
        );
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public static void registerModels(ModelRegistryEvent event)
    {
        registerModelBasic(item_tree_farm);
        registerModelBasic(item_cobblestone_generator);
        registerModelBasic(item_power_generator_fire);
    }

    @SideOnly(Side.CLIENT)
    private static void registerModelBasic(Item item)
    {
        ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName(), "inventory"));
    }

    public static void registerTileEntities()
    {
        GameRegistry.registerTileEntity(TileTreeFarm.class, tree_farm.getRegistryName().toString());
        GameRegistry.registerTileEntity(TileCobblestoneGenerator.class, cobblestone_generator.getRegistryName().toString());
        GameRegistry.registerTileEntity(TileFireGenerator.class, power_generator_fire.getRegistryName().toString());
    }

    public static void registerRecipes()
    {
        GameRegistry.addShapedRecipe(new ItemStack(tree_farm, 1), "SAS", "IOI", "SAS", 'S', new ItemStack(Blocks.SAPLING, 1, OreDictionary.WILDCARD_VALUE), 'A', new ItemStack(Items.IRON_AXE, 1), 'I', new ItemStack(Blocks.IRON_BLOCK, 1), 'O', new ItemStack(Blocks.OBSIDIAN, 1));
        GameRegistry.addShapedRecipe(new ItemStack(cobblestone_generator, 1), "PPP", "WCL", "PPP", 'W', new ItemStack(Items.WATER_BUCKET, 1), 'C', new ItemStack(Blocks.COBBLESTONE, 1), 'L', new ItemStack(Items.LAVA_BUCKET, 1), 'P', new ItemStack(Items.IRON_PICKAXE, 1));
    }

    public static void registerEvents()
    {
        MinecraftForge.TERRAIN_GEN_BUS.register(MiscEvents.class);
    }

}
