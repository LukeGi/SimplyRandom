package bluemonster.simplyrandom;

import bluemonster.simplyrandom.core.ItemBlockBase;
import bluemonster.simplyrandom.metals.*;
import bluemonster.simplyrandom.util.RecipeConverter;
import com.google.common.collect.Sets;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;

import java.util.Collections;
import java.util.HashSet;

import static bluemonster.simplyrandom.RegistryHandler.Objects.*;

@Mod.EventBusSubscriber
public class RegistryHandler {

    public static final HashSet<Runnable> MODELS_TO_REGISTER = Sets.newHashSet();

    @SubscribeEvent
    public static void onRegisterBlocks(RegistryEvent.Register<Block> event) {
        event.getRegistry().registerAll(
                // Copper
                new Ore(Names.COPPER, COPPER_DIRTY_DUST, false),
                new MetalBlock(Names.COPPER),
                // Lead
                new Ore(Names.LEAD, LEAD_DIRTY_DUST, true),
                new MetalBlock(Names.LEAD),
                // Tin
                new Ore(Names.TIN, TIN_DIRTY_DUST, false),
                new MetalBlock(Names.TIN)
        );
    }

    @SubscribeEvent
    public static void onRegisterItems(RegistryEvent.Register<Item> event) {
        event.getRegistry().registerAll(
                // Copper
                new ItemBlockBase(COPPER_BLOCK),
                new DirtyDust(Names.COPPER),
                new Dust(Names.COPPER),
                new Ingot(Names.COPPER),
                new Nugget(Names.COPPER),
                // Lead
                new MetalItemBlock(LEAD_BLOCK),
                new DirtyDust(Names.LEAD),
                new Dust(Names.LEAD),
                new Ingot(Names.LEAD),
                new Nugget(Names.LEAD),
                // Tin
                new MetalItemBlock(TIN_BLOCK),
                new DirtyDust(Names.TIN),
                new Dust(Names.TIN),
                new Ingot(Names.TIN),
                new Nugget(Names.TIN)
        );
    }

    public static void onRegisterOreDict() {
        // Copper
        OreDictionary.registerOre("blockCopper", getItemStack(COPPER_BLOCK));
        OreDictionary.registerOre("dirtyDustCopper", getItemStack(COPPER_DIRTY_DUST));
        OreDictionary.registerOre("dustCopper", getItemStack(COPPER_DUST));
        OreDictionary.registerOre("ingotCopper", getItemStack(COPPER_INGOT));
        OreDictionary.registerOre("nuggetCopper", getItemStack(COPPER_NUGGET));
        // Lead
        OreDictionary.registerOre("blockLead", getItemStack(LEAD_BLOCK));
        OreDictionary.registerOre("dirtyDustLead", getItemStack(LEAD_DIRTY_DUST));
        OreDictionary.registerOre("dustLead", getItemStack(LEAD_DUST));
        OreDictionary.registerOre("ingotLead", getItemStack(LEAD_INGOT));
        OreDictionary.registerOre("nuggetLead", getItemStack(LEAD_NUGGET));
        // Tin
        OreDictionary.registerOre("blockTin", getItemStack(TIN_BLOCK));
        OreDictionary.registerOre("dirtyDustTin", getItemStack(TIN_DIRTY_DUST));
        OreDictionary.registerOre("dustTin", getItemStack(TIN_DUST));
        OreDictionary.registerOre("ingotTin", getItemStack(TIN_INGOT));
        OreDictionary.registerOre("nuggetTin", getItemStack(TIN_NUGGET));
    }

    public static ItemStack getItemStack(Block block) {
        return getItemStack(block, 1);
    }

    public static ItemStack getItemStack(Item item) {
        return getItemStack(item, 1);
    }

    public static ItemStack getItemStack(Block block, int count) {
        return new ItemStack(block, count);
    }

    public static ItemStack getItemStack(Item item, int count) {
        return new ItemStack(item, count);
    }

    @SubscribeEvent
    public static void onRegisterRecipes(RegistryEvent.Register<IRecipe> event) {
        // Block -> Ingot
        RecipeConverter.addShapelessRecipe(getItemStack(COPPER_INGOT, 9), "blockCopper");
        RecipeConverter.addShapelessRecipe(getItemStack(LEAD_INGOT, 9), "blockLead");
        RecipeConverter.addShapelessRecipe(getItemStack(TIN_INGOT, 9), "blockTin");
        // Ingot -> Block
        RecipeConverter.addShapelessRecipe(getItemStack(COPPER_BLOCK), getX(9, "ingotCopper"));
        RecipeConverter.addShapelessRecipe(getItemStack(LEAD_BLOCK), getX(9, "ingotLead"));
        RecipeConverter.addShapelessRecipe(getItemStack(TIN_BLOCK), getX(9, "ingotTin"));
        // Ingot -> Nugget
        RecipeConverter.addShapelessRecipe(getItemStack(COPPER_NUGGET, 9), "ingotCopper");
        RecipeConverter.addShapelessRecipe(getItemStack(LEAD_NUGGET, 9), "ingotLead");
        RecipeConverter.addShapelessRecipe(getItemStack(TIN_NUGGET, 9), "ingotTin");
        // Nugget -> Ingot
        RecipeConverter.addShapelessRecipe(getItemStack(COPPER_INGOT), getX(9, "nuggetCopper"));
        RecipeConverter.addShapelessRecipe(getItemStack(LEAD_INGOT), getX(9, "nuggetLead"));
        RecipeConverter.addShapelessRecipe(getItemStack(TIN_INGOT), getX(9, "nuggetTin"));
        // Dirty Dust -> Dust
        RecipeConverter.addShapelessRecipe(getItemStack(COPPER_DUST), "dirtyDustCopper", Items.WATER_BUCKET);
        RecipeConverter.addShapelessRecipe(getItemStack(LEAD_DUST), "dirtyDustLead", Items.WATER_BUCKET);
        RecipeConverter.addShapelessRecipe(getItemStack(TIN_DUST), "dirtyDustTin", Items.WATER_BUCKET);
        RecipeConverter.addShapelessRecipe(getItemStack(COPPER_DUST), "dirtyDustCopper", Items.POTIONITEM.getDefaultInstance());
        RecipeConverter.addShapelessRecipe(getItemStack(LEAD_DUST), "dirtyDustLead", Items.POTIONITEM.getDefaultInstance());
        RecipeConverter.addShapelessRecipe(getItemStack(TIN_DUST), "dirtyDustTin", Items.POTIONITEM.getDefaultInstance());
        // Group Ingredients
        RecipeConverter.generateConstants();
    }

    private static Object[] getX(int x, Object value) {
        return Collections.nCopies(x, value).toArray();
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public static void onRegisterModels(ModelRegistryEvent event) {
        MODELS_TO_REGISTER.forEach(Runnable::run);
    }

    private static ResourceLocation getRL(String location) {
        return new ResourceLocation(ModInfo.MOD_ID, location);
    }

    @ObjectHolder(ModInfo.MOD_ID)
    public static class Objects {

        public static final Block TIN_ORE = Blocks.AIR;
        public static final Block TIN_BLOCK = Blocks.AIR;
        @ObjectHolder("tin_block")
        public static final Item TIN_BLOCK_ITEM = Items.AIR;
        public static final Item TIN_DIRTY_DUST = Items.AIR;
        public static final Item TIN_DUST = Items.AIR;
        public static final Item TIN_INGOT = Items.AIR;
        public static final Item TIN_NUGGET = Items.AIR;
        public static final Block COPPER_ORE = Blocks.AIR;
        public static final Block COPPER_BLOCK = Blocks.AIR;
        @ObjectHolder("copper_block")
        public static final Item COPPER_BLOCK_ITEM = Items.AIR;
        public static final Item COPPER_DIRTY_DUST = Items.AIR;
        public static final Item COPPER_DUST = Items.AIR;
        public static final Item COPPER_INGOT = Items.AIR;
        public static final Item COPPER_NUGGET = Items.AIR;
        public static final Block LEAD_ORE = Blocks.AIR;
        public static final Block LEAD_BLOCK = Blocks.AIR;
        @ObjectHolder("lead_block")
        public static final Item LEAD_BLOCK_ITEM = Items.AIR;
        public static final Item LEAD_DIRTY_DUST = Items.AIR;
        public static final Item LEAD_DUST = Items.AIR;
        public static final Item LEAD_INGOT = Items.AIR;
        public static final Item LEAD_NUGGET = Items.AIR;
    }
}
