package bluemonster.simplerandomstuff.miner;

import bluemonster.simplerandomstuff.core.block.BlockSRS;
import bluemonster.simplerandomstuff.reference.Names;
import bluemonster.simplerandomstuff.util.IFeatureRegistry;
import bluemonster.simplerandomstuff.util.ModelHelpers;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;

public class FRMiner
        implements IFeatureRegistry {
    public static final FRMiner INSTANCE = new FRMiner();
    private int scanPower = 0;
    private int breakPower = 0;
    private BlockSRS miner = new BlockMiner();

    @Override
    public void registerBlocks(IForgeRegistry<Block> registry) {
        registry.registerAll(miner);
    }

    @Override
    public void registerItems(IForgeRegistry<Item> registry) {
        registry.registerAll(miner.createItemBlock());
    }

    @Override
    public void registerTileEntities() {
        GameRegistry.registerTileEntity(TileMiner.class, miner.getRegistryName().toString());
    }

    @Override
    public void loadConfigs(Configuration configuration) {
        scanPower = configuration.getInt(Names.Features.Configs.MINER_SCAN_POWER,
                getName(),
                10,
                0,
                5000,
                "This is the amount of power the miner uses to scan a block. \nThe limit is 5000 as that is the size of the machine's internal battery. \nIf set to 0, the scanning of a block will be free."
        );
        breakPower = configuration.getInt(Names.Features.Configs.MINER_BREAK_POWER,
                getName(),
                100,
                0,
                5000,
                "This is the amount of power the miner uses to break a block. \nThe limit is 5000 as that is the size of the machine's internal battery. \nIf set to 0, the breaking of a block will be free"
        );
    }

    @Override
    public void registerEvents() {
        /* NO OPERATION */
    }

    @Override
    public void registerOreDict() {
        /* NO OPERATION */
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerRenders() {
        ModelHelpers.registerBlockModelAsItem(miner);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerClientEvents() {
        /* NO OPERATION */
    }

    @Override
    public String getName() {
        return Names.Features.MINER;
    }

    private FRMiner() {
    }

    public BlockSRS getMiner() {
        return miner;
    }

    public void setMiner(BlockSRS miner) {
        this.miner = miner;
    }

    public int getScanPower() {
        return scanPower;
    }

    public int getBreakPower() {
        return breakPower;
    }
}
