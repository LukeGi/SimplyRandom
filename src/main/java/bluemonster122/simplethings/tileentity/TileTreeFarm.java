package bluemonster122.simplethings.tileentity;

import bluemonster122.simplethings.handler.ConfigurationHandler;
import bluemonster122.simplethings.tileentity.things.IMachine;
import bluemonster122.simplethings.util.AreaType;
import com.google.common.collect.ImmutableSet;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class TileTreeFarm extends TileEntityST implements ITickable, IMachine
{
    public static Set<Block> ALLOWED_FARMING_BLOCKS = ImmutableSet.of(Blocks.DIRT, Blocks.FARMLAND, Blocks.GRASS);
    private AreaType farmedArea;
    private ItemStackHandler inventory = new ItemStackHandler(72);
    private EnergyStorage battery = new EnergyStorage(1000000);

    @Override
    public void update()
    {
        if (farmedArea == null) return;
        List<BlockPos> blocksToBreak = new ArrayList<>();
        //check for empty space that will allow for saplings
        //try to plant saplings
        //check for trees that aren't scheduled
        // commit a few blocks for chopping
        /*
         * check that there is enough energy to chop the block
         * check that there is enough room in the inventory for drops
         * if all good, proceed to chop the block.
         */
        handleBlockBreaking(blocksToBreak);

    }

    private void handleBlockBreaking(List<BlockPos> blocksToBreak)
    {
        if (blocksToBreak.isEmpty()) return;
        List<ItemStack> drops = new ArrayList<>();
        blocksToBreak.forEach(b -> drops.addAll(getWorld().getBlockState(b).getBlock().getDrops(getWorld(), b, getWorld().getBlockState(b), 0)));
        int energyCost = blocksToBreak.size() * ConfigurationHandler.tree_farm_break_energy;
        if (getBattery().getEnergyStored() >= energyCost)
        {
            boolean roomFlag = true;
            ItemStackHandler handler = inventory;
            for (ItemStack drop : drops)
                roomFlag = roomFlag && ItemHandlerHelper.insertItem(handler, drop, false) == ItemStack.field_190927_a;
            if (roomFlag)
            {
                drops.stream().map(i -> new EntityItem(getWorld(), pos.getX(), pos.getY() + 1, pos.getZ(), i)).forEach(getWorld()::spawnEntityInWorld);
                blocksToBreak.forEach(blockPos -> getWorld().destroyBlock(blockPos, false));
                extractPower(energyCost, false);
            }
        }
    }

    public void breakSaplings()
    {
        if (farmedArea == null) return;
        farmedArea.getArea().stream().filter(b -> getWorld().getBlockState(b).getBlock() instanceof BlockBush).forEach(blockPos -> getWorld().destroyBlock(blockPos, true));
    }

    private boolean isValidBlock(BlockPos down)
    {
        return ALLOWED_FARMING_BLOCKS.contains(getWorld().getBlockState(down).getBlock());
    }

    @Override
    public SPacketUpdateTileEntity getUpdatePacket()
    {
        NBTTagCompound nbt = new NBTTagCompound();
        this.writeToNBT(nbt);

        return new SPacketUpdateTileEntity(getPos(), 0, nbt);

    }

    @Override
    @SideOnly(Side.CLIENT)
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt)
    {
        this.readFromNBT(pkt.getNbtCompound());
    }

    @Override
    @Nonnull
    public NBTTagCompound getUpdateTag()
    {
        NBTTagCompound nbt = super.getUpdateTag();
        writeToNBT(nbt);
        return nbt;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        setFarmedArea(AreaType.getFromIndex(compound.getInteger("areaType")));
        super.readFromNBT(compound);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        compound.setInteger("areaType", AreaType.getIndex(farmedArea));
        return super.writeToNBT(compound);
    }

    public void setFarmedArea(AreaType setArea)
    {
        farmedArea = setArea;
    }

    @Override
    public EnergyStorage getBattery()
    {
        return battery;
    }

    @Override
    public ItemStackHandler getInventory()
    {
        return inventory;
    }
}
