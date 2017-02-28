package bluemonster122.mods.simplethings.block;

import bluemonster122.mods.simplethings.SimpleThings;
import net.minecraft.block.material.Material;

public class BlockMachineBlock extends BlockST {
    public BlockMachineBlock() {
        super("machine_block", Material.IRON);
        setCreativeTab(SimpleThings.theTab);

        setHardness(5f);
        setResistance(5f);
        setHarvestLevel("pickaxe", 1);
    }
}
