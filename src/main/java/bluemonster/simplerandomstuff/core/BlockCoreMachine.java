package bluemonster.simplerandomstuff.core;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public abstract class BlockCoreMachine extends BlockCoreTile {
    public BlockCoreMachine(String name, Material blockMaterialIn, MapColor blockMapColorIn) {
        super(name, blockMaterialIn, blockMapColorIn);
    }

    public BlockCoreMachine(String name, Material materialIn) {
        super(name, materialIn);
    }

    public BlockCoreMachine(String name) {
        super(name);
    }

    @Override
    public ItemBlock createItemBlock() {
        return new ItemBlockCoreMachine(this);
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        if (stack.hasTagCompound()) {
            TileEntity tile = worldIn.getTileEntity(pos);
            if (tile instanceof TileCoreMachine)
                ((TileCoreMachine) tile).readFromNBTLight(stack.getTagCompound());
        }
        super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        ItemStack stack = createItemStack(state);
        TileEntity tile = worldIn.getTileEntity(pos);
        if (tile instanceof TileCoreMachine)
            stack.setTagCompound(((TileCoreMachine) tile).writeToNBTLight(new NBTTagCompound()));
        worldIn.spawnEntity(new EntityItem(worldIn, pos.getX() + .5, pos.getY() + .5, pos.getZ() + .5, stack));
        super.breakBlock(worldIn, pos, state);
    }

    @Override
    public int quantityDropped(Random random) {
        return 0;
    }
}
