package bluemonster122.simplethings.handler;

import bluemonster122.simplethings.block.*;
import bluemonster122.simplethings.item.ItemSpear;
import bluemonster122.simplethings.item.SpearMaterial;
import bluemonster122.simplethings.tileentity.*;
import bluemonster122.simplethings.util.MiscEvents;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
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

import java.util.Arrays;
@Mod.EventBusSubscriber
public class RegistryHandler
{
	public static SimpleBlockBase tree_farm = new BlockTreeFarm();
	public static Item item_tree_farm = tree_farm.getItemBlock();
	public static SimpleBlockBase cobblestone_generator = new BlockCobblestoneGenerator();
	public static Item item_cobblestone_generator = cobblestone_generator.getItemBlock();
	public static SimpleBlockBase lightning_rod = new BlockLightningRod();
	public static Item item_lightning_rod = lightning_rod.getItemBlock();
	public static SimpleBlockBase power_generator_fire = new BlockEnergyGeneratorFire();
	public static Item item_power_generator_fire = power_generator_fire.getItemBlock();
	public static SimpleBlockBase power_cable = new BlockPowerCable();
	public static Item item_power_cable = power_cable.getItemBlock();
	public static SimpleBlockBase power_generator_sugar = new BlockEnergyGeneratorSugar();
	public static Item item_power_generator_sugar = power_generator_sugar.getItemBlock();
	public static Item[] spears = new ItemSpear[SpearMaterial.values().length];
	
	static {
		for (int i = 0; i < SpearMaterial.values().length; i++)
		{
			SpearMaterial mat = SpearMaterial.values()[i];
			spears[i] = new ItemSpear(mat, (mat.name() + "_spear").toLowerCase());
		}
	}
	
	@SubscribeEvent
	public static void regsiterBlocks(RegistryEvent.Register<Block> event)
	{
		event.getRegistry().registerAll(tree_farm, cobblestone_generator, lightning_rod, power_generator_fire,
		                                power_generator_sugar, power_cable
		);
	}
	
	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> event)
	{
		event.getRegistry().registerAll(item_tree_farm, item_cobblestone_generator, item_power_generator_fire,
		                                item_lightning_rod, item_power_generator_sugar, item_power_cable
		);
		Arrays.asList(spears).forEach(event.getRegistry()::register);
	}
	
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public static void registerModels(ModelRegistryEvent event)
	{
		registerModelBasic(item_tree_farm);
		registerModelBasic(item_cobblestone_generator);
		registerModelBasic(item_lightning_rod);
		registerModelBasic(item_power_generator_fire);
		registerModelBasic(item_power_generator_sugar);
		registerModelBasic(item_power_cable);
		Arrays.asList(spears).forEach(RegistryHandler::registerModelBasic);
	}
	
	@SideOnly(Side.CLIENT)
	private static void registerModelBasic(Item item)
	{
		ModelLoader.setCustomModelResourceLocation(
		  item, 0, new ModelResourceLocation(item.getRegistryName(), "inventory"));
	}
	
	public static void registerTileEntities()
	{
		GameRegistry.registerTileEntity(TileTreeFarm.class, tree_farm.getRegistryName().toString());
		GameRegistry.registerTileEntity(
		  TileCobblestoneGenerator.class, cobblestone_generator.getRegistryName().toString());
		GameRegistry.registerTileEntity(TileLightningRod.class, lightning_rod.getRegistryName().toString());
		GameRegistry.registerTileEntity(
		  TileEnergyGeneratorFire.class, power_generator_fire.getRegistryName().toString());
		GameRegistry.registerTileEntity(
		  TileEnergyGeneratorSugar.class, power_generator_sugar.getRegistryName().toString());
		GameRegistry.registerTileEntity(TilePowerCable.class, power_cable.getRegistryName().toString());
	}
	
	public static void registerRecipes()
	{
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
	
	public static void registerEvents()
	{
		MinecraftForge.TERRAIN_GEN_BUS.register(MiscEvents.class);
	}
}
