package bluemonster122.mods.simplethings.pump;

import bluemonster122.mods.simplethings.core.FRCore;
import bluemonster122.mods.simplethings.core.block.BlockST;
import bluemonster122.mods.simplethings.reference.Names.OreDict;
import bluemonster122.mods.simplethings.tanks.BlockTank;
import bluemonster122.mods.simplethings.tanks.FRTank;
import bluemonster122.mods.simplethings.util.IFeatureRegistry;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.ShapedOreRecipe;

import static bluemonster122.mods.simplethings.util.ModelHelpers.registerBlockModelAsItem;

public class FRPump implements IFeatureRegistry {

    public static final FRPump INSTANCE = new FRPump();
    private final BlockST pump = new BlockPump();
    private final BlockST floodgate = new BlockFloodGate();

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
                'S', OreDict.STICK,
                'W', new ItemStack(FRCore.wrench),
                'T', new ItemStack(FRTank.tank, 1, BlockTank.Types.IRON.getMeta()),
                '|', OreDict.STRING,
                'R', OreDict.REDSTONE,
                'B', new ItemStack(Items.BUCKET)
        ));

        //@formatter:on
    }

    @Override
    public void registerTileEntities( ) {
        GameRegistry.registerTileEntity(TilePump.class, "simplethings:pump");
        GameRegistry.registerTileEntity(TileFloodGate.class, "simplethings:floodgate");
    }

    @Override
    public void loadConfigs(Configuration configuration) {
        setPumpEnergy(configuration.getInt("RF use per pump-ed block", "Pump", 2, 0, 100, "Set this to 0 for free pump."));
    }

    @Override
    public void registerEvents( ) {
        /* NO OPERATION */
    }

    @Override
    public void registerOreDict( ) {
        /* NO OPERATION */
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
