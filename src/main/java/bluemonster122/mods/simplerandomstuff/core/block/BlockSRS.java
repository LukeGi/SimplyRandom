package bluemonster122.mods.simplerandomstuff.core.block;

import bluemonster122.mods.simplerandomstuff.SRS;
import bluemonster122.mods.simplerandomstuff.core.ItemBlockST;
import bluemonster122.mods.simplerandomstuff.reference.ModInfo;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockSRS
        extends Block {
    public BlockSRS(String name, Material blockMaterialIn, MapColor blockMapColorIn) {
        super(blockMaterialIn, blockMapColorIn);
        setup(name);
    }

    public BlockSRS(String name, Material materialIn) {
        super(materialIn);
        setup(name);
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        TileEntity tileEntity = worldIn.getTileEntity(pos);
        if (tileEntity instanceof IHaveInventory) {
            ((IHaveInventory) tileEntity).dropContents(worldIn, pos);
        }
        super.breakBlock(worldIn, pos, state);
    }

    private void setup(String name) {
        setRegistryName(ModInfo.MOD_ID, name);
        setUnlocalizedName(getRegistryName().getResourceDomain() + "." + name);
        setCreativeTab(SRS.theTab);
    }

    public ItemBlock createItemBlock() {
        return new ItemBlockST(this);
    }
}
