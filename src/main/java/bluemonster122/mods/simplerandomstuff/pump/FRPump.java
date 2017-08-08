package bluemonster122.mods.simplerandomstuff.pump;

import bluemonster122.mods.simplerandomstuff.core.block.BlockSRS;
import bluemonster122.mods.simplerandomstuff.reference.Names;
import bluemonster122.mods.simplerandomstuff.reference.Names.OreDict;
import bluemonster122.mods.simplerandomstuff.util.IFeatureRegistry;
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

import static bluemonster122.mods.simplerandomstuff.util.ModelHelpers.registerBlockModelAsItem;

public class FRPump
        implements IFeatureRegistry {

    public static final FRPump INSTANCE = new FRPump();

    private final BlockSRS pump = new BlockPump();

    private final BlockSRS floodgate = new BlockFloodGate();

    private int Pump_RF = 1;

    private FRPump() {
    }

    @Override

    public void registerBlocks(IForgeRegistry<Block> registry) {
        registry.registerAll(pump, floodgate);
    }

    @Override
    public void registerItems(IForgeRegistry<Item> registry) {
        registry.registerAll(pump.createItemBlock(), floodgate.createItemBlock());
    }

    @Override
    public void registerTileEntities() {
        GameRegistry.registerTileEntity(TilePump.class, "simplerandomstuff:pump");
        GameRegistry.registerTileEntity(TileFloodGate.class, "simplerandomstuff:floodgate");
    }

    @Override
    public void loadConfigs(Configuration configuration) {
        setPumpEnergy(configuration.getInt(Names.Features.Configs.PUMP_ENERGY,
                Names.Features.PUMP,
                2,
                0,
                100,
                "Set this to 0 for free pump."
        ));
    }

    @Override
    public void registerEvents() {
        /* NO OPERATION */
    }

    @Override
    public void registerOreDict() {
        OreDictionary.registerOre(OreDict.IRON_BARS, new ItemStack(Blocks.IRON_BARS));
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerRenders() {
        registerBlockModelAsItem(pump);
        registerBlockModelAsItem(floodgate);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerClientEvents() {
        /* NO OPERATION */
    }

    @Override
    public String getName() {
        return Names.Features.PUMP;
    }

    public int getPumpEnergy() {
        return Pump_RF;
    }

    public void setPumpEnergy(int pump_RF) {
        Pump_RF = pump_RF;
    }
}
