package bluemonster.simplerandomstuff.registry;

import bluemonster.simplerandomstuff.core.BlockCore;
import bluemonster.simplerandomstuff.treefarm.BlockTreeFarm;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.HashMap;
import java.util.Map;

@Mod.EventBusSubscriber
public class RegistryHandler {
    public static CurrentStage currentStage = CurrentStage.NONE;
    public static BlockCore tree_farm = new BlockTreeFarm();

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) throws IllegalAccessException {
        currentStage = CurrentStage.BLOCKS;
        tree_farm.register();
        currentStage = CurrentStage.NONE;
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) throws IllegalAccessException {
        currentStage = CurrentStage.ITEMS;
        tree_farm.register();
        currentStage = CurrentStage.NONE;
    }

    @Mod.EventBusSubscriber
    @SideOnly(Side.CLIENT)
    public static class ClientRegistryHandler {
        public static final Map<ItemStack, ModelResourceLocation> REGISTRY = new HashMap<>();

        @SubscribeEvent
        public static void registerModels(ModelRegistryEvent event) throws IllegalAccessException {
            currentStage = CurrentStage.MODELS;
            tree_farm.register();
            currentStage = CurrentStage.NONE;
            REGISTRY.forEach((key, value) -> ModelLoader.setCustomModelResourceLocation(key.getItem(), key.getItemDamage(), value));
        }
    }

    public enum CurrentStage{ NONE, BLOCKS, ITEMS, MODELS }
}
