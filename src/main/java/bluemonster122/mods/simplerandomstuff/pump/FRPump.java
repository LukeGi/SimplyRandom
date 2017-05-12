package bluemonster122.mods.simplerandomstuff.pump;

import bluemonster122.mods.simplerandomstuff.core.FRCore;
import bluemonster122.mods.simplerandomstuff.core.ItemMisc;
import bluemonster122.mods.simplerandomstuff.core.block.BlockST;
import bluemonster122.mods.simplerandomstuff.reference.ModInfo;
import bluemonster122.mods.simplerandomstuff.reference.Names.OreDict;
import bluemonster122.mods.simplerandomstuff.tanks.BlockTank;
import bluemonster122.mods.simplerandomstuff.tanks.FRTank;
import bluemonster122.mods.simplerandomstuff.util.IFeatureRegistry;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;

import static bluemonster122.mods.simplerandomstuff.util.ModelHelpers.registerBlockModelAsItem;

public class FRPump implements IFeatureRegistry {

    public static final FRPump INSTANCE = new FRPump();
    private final BlockST pump = new BlockPump();
    private final BlockST floodgate = new BlockFloodGate();
    private static boolean shouldLoad = false;

    @Override

    public void registerBlocks( ) {
        GameRegistry.register(pump);
        GameRegistry.register(floodgate);
    }

    @Override
    public void registerItems( ) {
        GameRegistry.register(pump.createItemBlock());
        GameRegistry.register(floodgate.createItemBlock());
    }

    @Override
    public void registerRecipes( ) {
        //@formatter:off

        GameRegistry.addRecipe(new ShapedOreRecipe(
                new ItemStack(pump, 1),
                "SWS",
                "T|T",
                "RBR",
                'S', new ItemStack(FRCore.misc, 1, ItemMisc.Types.STONE_GEAR.getMeta()),
                'W', new ItemStack(FRCore.misc, 1, ItemMisc.Types.IRON_ROD.getMeta()),
                'T', new ItemStack(FRTank.tank, 1, BlockTank.Types.IRON.getMeta()),
                '|', new ItemStack(FRCore.wrench),
                'R', OreDict.REDSTONE,
                'B', new ItemStack(Items.BUCKET)
        ));

        GameRegistry.addRecipe(new ShapedOreRecipe(
                new ItemStack(pump, 1),
                "SWS",
                "T|T",
                "RSR",
                'S', new ItemStack(FRCore.misc, 1, ItemMisc.Types.STONE_GEAR.getMeta()),
                'W', new ItemStack(FRCore.misc, 1, ItemMisc.Types.IRON_ROD.getMeta()),
                'T', OreDict.IRON_BARS,
                '|', new ItemStack(FRTank.tank, 1, BlockTank.Types.IRON.getMeta()),
                'R', OreDict.REDSTONE
        ));

        //@formatter:on
    }

    @Override
    public void registerTileEntities( ) {
        GameRegistry.registerTileEntity(TilePump.class, "simplerandomstuff:pump");
        GameRegistry.registerTileEntity(TileFloodGate.class, "simplerandomstuff:floodgate");
    }

    @Override
    public void loadConfigs(Configuration configuration) {
        shouldLoad = configuration.getBoolean("Pump", ModInfo.CONFIG_FEATURES, true, "Set to false to disable the pump and floodgate");
        setPumpEnergy(configuration.getInt("RF use per pump-ed block", "Pump", 2, 0, 100, "Set this to 0 for free pump."));
    }

    @Override
    public void registerEvents( ) {
        /* NO OPERATION */
    }

    @Override
    public void registerOreDict( ) {
        OreDictionary.registerOre(OreDict.IRON_BARS, new ItemStack(Blocks.IRON_BARS));
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerRenders( ) {
        registerBlockModelAsItem(pump);
        registerBlockModelAsItem(floodgate);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerClientEvents( ) {
        /* NO OPERATION */
    }

    @Override
    public boolean shouldLoad( ) {
        return shouldLoad;
    }

    public int getPumpEnergy( ) {
        return Pump_RF;
    }

    public void setPumpEnergy(int pump_RF) {
        Pump_RF = pump_RF;
    }

    private FRPump( ) {
    }

    private int Pump_RF = 1;
}
