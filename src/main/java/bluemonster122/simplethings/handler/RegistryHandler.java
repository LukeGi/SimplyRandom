package bluemonster122.simplethings.handler;

import bluemonster122.simplethings.block.*;
import bluemonster122.simplethings.item.ItemSpear;
import bluemonster122.simplethings.item.SpearMaterial;
import bluemonster122.simplethings.util.IInitModelVarients;
import bluemonster122.simplethings.util.ITileEntityProvider1;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Mod.EventBusSubscriber
public class RegistryHandler
{
	public static final List<Block> BLOCKS = new ArrayList<>();
	public static final List<Item> ITEMS = new ArrayList<>();

	public static SimpleBlockBase tree_farm = new BlockTreeFarm();
	public static SimpleBlockBase cobblestone_generator = new BlockCobblestoneGenerator();
	public static SimpleBlockBase lightning_rod = new BlockLightningRod();
	public static SimpleBlockBase power_generator_fire = new BlockEnergyGeneratorFire();
	public static SimpleBlockBase power_cable = new BlockPowerCable();
	public static SimpleBlockBase power_generator_sugar = new BlockEnergyGeneratorSugar();
	public static SimpleBlockBase machine_block = new SimpleBlockBase(Material.IRON, "machine_block");
	public static Item[] spears = new ItemSpear[SpearMaterial.values().length];

	static
	{
		for (int i = 0; i < SpearMaterial.values().length; i++)
		{
			SpearMaterial mat = SpearMaterial.values()[i];
			spears[i] = new ItemSpear(mat, (mat.name() + "_spear").toLowerCase());
		}
	}

	@SubscribeEvent
	public static void regsiterBlocks(RegistryEvent.Register<Block> event)
	{
		BLOCKS.forEach(event.getRegistry()::register);
	}

	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> event)
	{
		ITEMS.forEach(event.getRegistry()::register);
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public static void registerModels(ModelRegistryEvent event)
	{
		for (Block block : BLOCKS)
			if (block instanceof IInitModelVarients)
			{
				((IInitModelVarients) block).initModelsAndVariants();
			} else
			{
				registerModelBasic(Item.getItemFromBlock(block));
			}
		for (Item item : ITEMS.stream().filter(item -> !(item instanceof ItemBlock)).collect(Collectors.toList()))
			if (item instanceof IInitModelVarients)
			{
				((IInitModelVarients) item).initModelsAndVariants();
			} else
			{
				registerModelBasic(item);
			}
	}

	@SideOnly(Side.CLIENT)
	private static void registerModelBasic(Item item)
	{
		ModelLoader.setCustomModelResourceLocation(
		  item, 0, new ModelResourceLocation(item.getRegistryName(), "inventory"));
	}

	public static void registerTileEntities()
	{
		BLOCKS.stream().filter(b -> b instanceof ITileEntityProvider1).forEach(b -> GameRegistry.registerTileEntity(((ITileEntityProvider1)b).getTileClass(), b.getRegistryName().toString()));
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

	}
}
