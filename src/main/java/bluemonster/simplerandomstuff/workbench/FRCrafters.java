package bluemonster.simplerandomstuff.workbench;

import bluemonster.simplerandomstuff.core.block.BlockSRS;
import bluemonster.simplerandomstuff.reference.Names;
import bluemonster.simplerandomstuff.util.IFeatureRegistry;
import bluemonster.simplerandomstuff.util.ModelHelpers;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.registries.IForgeRegistry;

import static bluemonster.simplerandomstuff.util.ModelHelpers.registerBlockModelAsItem;

public class FRCrafters
        implements IFeatureRegistry {
    public static final FRCrafters INSTANCE = new FRCrafters();

    public BlockSRS crafting_table = new BlockCraftingTable();

    public BlockSRS crafting_table_auto = new BlockCraftingTable.BlockCraftingTableAuto();

    @Override
    public void registerBlocks(IForgeRegistry<Block> registry) {
        registry.registerAll(crafting_table, crafting_table_auto);
    }

    @Override
    public void registerItems(IForgeRegistry<Item> registry) {
        registry.registerAll(crafting_table.createItemBlock(), crafting_table_auto.createItemBlock());
    }

    @Override
    public void registerTileEntities() {
        GameRegistry.registerTileEntity(TileCraftingTable.class, "simplerandomstuff:crafting_table");
        GameRegistry.registerTileEntity(TileCraftingTable.TileCraftingTableAuto.class,
                "simplerandomstuff:crafting_table_auto"
        );
    }

    @Override
    public void loadConfigs(Configuration configuration) {
    }

    @Override
    public void registerEvents() {
        /* NO OPERATION */
    }

    @Override
    public void registerOreDict() {
        OreDictionary.registerOre("workbench", new ItemStack(Blocks.CRAFTING_TABLE));
        OreDictionary.registerOre("pressurePlateWood", new ItemStack(Blocks.WOODEN_PRESSURE_PLATE));
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerRenders() {
        ModelHelpers.registerBlockModelAsItem(crafting_table);
        ModelHelpers.registerBlockModelAsItem(crafting_table_auto);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerClientEvents() {
        /* NO OPERATION */
    }

    @Override
    public String getName() {
        return Names.Features.CRAFTERS;
    }

    private FRCrafters() {
    }
}
