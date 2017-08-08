package bluemonster122.mods.simplerandomstuff.treefarm;

import bluemonster122.mods.simplerandomstuff.core.block.BlockSRS;
import bluemonster122.mods.simplerandomstuff.reference.Names;
import bluemonster122.mods.simplerandomstuff.util.IFeatureRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockPlanks;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.registries.IForgeRegistry;

import static bluemonster122.mods.simplerandomstuff.util.ModelHelpers.registerBlockModelAsItem;

public class FRTreeFarm
        implements IFeatureRegistry {
    public static final FRTreeFarm INSTANCE = new FRTreeFarm();

    public static final BlockSRS tree_farm = new BlockTreeFarm();

    private int tree_farm_break_energy = 50;

    private FRTreeFarm() {
    }

    @Override
    public void registerBlocks(IForgeRegistry<Block> registry) {
        registry.registerAll(tree_farm);
    }

    @Override
    public void registerItems(IForgeRegistry<Item> registry) {
        registry.registerAll(tree_farm.createItemBlock());
    }

    @Override
    public void registerTileEntities() {
        GameRegistry.registerTileEntity(TileTreeFarm.class, "simplerandomstuff:tree_farm");
    }

    @Override
    public void loadConfigs(Configuration configuration) {
        setBreakEnergy(configuration.getInt(Names.Features.Configs.TREE_FARM_BREAK_ENERGY,
                Names.Features.TREE_FARM,
                50,
                0,
                1000,
                "Set to 0 to make the farm cost no power."
        ));
    }

    @Override
    public void registerEvents() {
        /* NO OPERATION */
    }

    @Override
    public void registerOreDict() {
        for (BlockPlanks.EnumType type : BlockPlanks.EnumType.values()) {
            String name = type.getName();
            OreDictionary.registerOre("sapling", new ItemStack(Blocks.SAPLING, 1, type.getMetadata()));
            OreDictionary.registerOre("sapling" + name.substring(0, 1)
                            .toUpperCase() + name.substring(1),
                    new ItemStack(Blocks.SAPLING, 1, type.getMetadata())
            );
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerRenders() {
        registerBlockModelAsItem(tree_farm);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerClientEvents() {
        /* NO OPERATION */
    }

    @Override
    public String getName() {
        return Names.Features.TREE_FARM;
    }

    public int getBreakEnergy() {
        return tree_farm_break_energy;
    }

    public void setBreakEnergy(int energy) {
        this.tree_farm_break_energy = energy;
    }
}
