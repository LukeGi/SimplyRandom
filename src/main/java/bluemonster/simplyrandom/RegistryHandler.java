package bluemonster.simplyrandom;

import bluemonster.simplyrandom.metals.*;
import com.google.common.collect.Sets;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
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
import net.minecraftforge.oredict.ShapelessOreRecipe;

import java.util.Collections;
import java.util.HashSet;

import static bluemonster.simplyrandom.RegistryHandler.Objects.*;

@Mod.EventBusSubscriber
public class RegistryHandler {

    public static final HashSet<Runnable> MODELS_TO_REGISTER = Sets.newHashSet();

    @SubscribeEvent
    public static void onRegisterBlocks(RegistryEvent.Register<Block> event) {
        event.getRegistry().registerAll(
                new Ore(Names.TIN, TIN_DIRTY_DUST, false),
                new MetalBlock(Names.TIN),
                new Ore(Names.COPPER, COPPER_DIRTY_DUST, false),
                new MetalBlock(Names.COPPER),
                new Ore(Names.LEAD, LEAD_DIRTY_DUST, true),
                new MetalBlock(Names.LEAD)
        );
    }

    @SubscribeEvent
    public static void onRegisterItems(RegistryEvent.Register<Item> event) {
        event.getRegistry().registerAll(
                new MetalBlockItem(TIN_BLOCK),
                new DirtyDust(Names.TIN),
                new Dust(Names.TIN),
                new Ingot(Names.TIN),
                new Nugget(Names.TIN),
                new MetalBlockItem(COPPER_BLOCK),
                new DirtyDust(Names.COPPER),
                new Dust(Names.COPPER),
                new Ingot(Names.COPPER),
                new Nugget(Names.COPPER),
                new MetalBlockItem(LEAD_BLOCK),
                new DirtyDust(Names.LEAD),
                new Dust(Names.LEAD),
                new Ingot(Names.LEAD),
                new Nugget(Names.LEAD)
        );
    }

    @SubscribeEvent
    public static void onRegisterRecipes(RegistryEvent.Register<IRecipe> event) {
        ResourceLocation rl = getRL("metalRecipes");
        event.getRegistry().registerAll(
                new ShapelessOreRecipe(rl, COPPER_INGOT.getItemStack(9), "blockCopper").setRegistryName(getRL("copper_block_to_ingot")),
                new ShapelessOreRecipe(rl, LEAD_INGOT.getItemStack(9), "blockLead").setRegistryName(getRL("lead_block_to_ingot")),
                new ShapelessOreRecipe(rl, TIN_INGOT.getItemStack(9), "blockTin").setRegistryName(getRL("tin_block_to_ingot")),
                new ShapelessOreRecipe(rl, COPPER_NUGGET.getItemStack(9), "ingotCopper").setRegistryName(getRL("copper_ingot_to_nugget")),
                new ShapelessOreRecipe(rl, LEAD_NUGGET.getItemStack(9), "ingotLead").setRegistryName(getRL("lead_ingot_to_nugget")),
                new ShapelessOreRecipe(rl, TIN_NUGGET.getItemStack(9), "ingotTin").setRegistryName(getRL("tin_ingot_to_nugget")),
                new ShapelessOreRecipe(rl, COPPER_BLOCK.getItemStack(), getX(9, "ingotCopper")).setRegistryName(getRL("copper_ingot_to_block")),
                new ShapelessOreRecipe(rl, LEAD_BLOCK.getItemStack(), getX(9, "ingotLead")).setRegistryName(getRL("lead_ingot_to_block")),
                new ShapelessOreRecipe(rl, TIN_BLOCK.getItemStack(), getX(9, "ingotTin")).setRegistryName(getRL("tin_ingot_to_block")),
                new ShapelessOreRecipe(rl, COPPER_INGOT.getItemStack(), getX(9, "nuggetCopper")).setRegistryName(getRL("copper_nugget_to_ingot")),
                new ShapelessOreRecipe(rl, LEAD_INGOT.getItemStack(), getX(9, "nuggetLead")).setRegistryName(getRL("copper_nugget_to_ingot")),
                new ShapelessOreRecipe(rl, TIN_INGOT.getItemStack(), getX(9, "nuggetTin")).setRegistryName(getRL("copper_nugget_to_ingot"))
        );
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public static void onRegisterModels(ModelRegistryEvent event) {
        MODELS_TO_REGISTER.forEach(Runnable::run);
    }

    private static ResourceLocation getRL(String location) {
        return new ResourceLocation(ModInfo.MOD_ID, location);
    }

    private static Object[] getX(int x, Object value) {
        return Collections.nCopies(x, value).toArray();
    }

    @ObjectHolder(ModInfo.MOD_ID)
    public static class Objects {

        public static final Ore TIN_ORE = null;
        public static final MetalBlock TIN_BLOCK = null;
        @ObjectHolder("tin_block")
        public static final MetalBlockItem TIN_BLOCK_ITEM = null;
        public static final DirtyDust TIN_DIRTY_DUST = null;
        public static final Dust TIN_DUST = null;
        public static final Ingot TIN_INGOT = null;
        public static final Nugget TIN_NUGGET = null;
        public static final Ore COPPER_ORE = null;
        public static final MetalBlock COPPER_BLOCK = null;
        @ObjectHolder("copper_block")
        public static final MetalBlockItem COPPER_BLOCK_ITEM = null;
        public static final DirtyDust COPPER_DIRTY_DUST = null;
        public static final Dust COPPER_DUST = null;
        public static final Ingot COPPER_INGOT = null;
        public static final Nugget COPPER_NUGGET = null;
        public static final Ore LEAD_ORE = null;
        public static final MetalBlock LEAD_BLOCK = null;
        @ObjectHolder("lead_block")
        public static final MetalBlockItem LEAD_BLOCK_ITEM = null;
        public static final DirtyDust LEAD_DIRTY_DUST = null;
        public static final Dust LEAD_DUST = null;
        public static final Ingot LEAD_INGOT = null;
        public static final Nugget LEAD_NUGGET = null;
    }
}
