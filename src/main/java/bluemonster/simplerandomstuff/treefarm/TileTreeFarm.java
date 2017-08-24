package bluemonster.simplerandomstuff.treefarm;

import bluemonster.simplerandomstuff.SimpleRandomStuff;
import bluemonster.simplerandomstuff.client.renderer.BoxRenderer;
import bluemonster.simplerandomstuff.config.Configs;
import bluemonster.simplerandomstuff.core.Battery;
import bluemonster.simplerandomstuff.core.BatteryMachine;
import bluemonster.simplerandomstuff.core.TileCoreMachine;
import bluemonster.simplerandomstuff.util.IGuiProvider;
import bluemonster.simplerandomstuff.util.TileTimer;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.oredict.OreDictionary;

import java.awt.*;

@Mod.EventBusSubscriber
public class TileTreeFarm extends TileCoreMachine implements IGuiProvider {

    public static final NonNullList<ItemStack> saplings = OreDictionary.EMPTY_LIST;
    private static final int[] farmed_positions = {-3, -2, 2, 3};
    private int currentindex = 0;
    private TileTimer timer;
    private TreeFarmer farmer = new TreeFarmer(this);
    private BoxRenderer boxRenderer;

    @SubscribeEvent
    public static void catchSaplings(OreDictionary.OreRegisterEvent event) {
        if (event.getName().equals("sapling")) {
            saplings.add(event.getOre());
        }
    }

    private void cyclePosition() {
        currentindex++;
        if (currentindex >= farmed_positions.length * farmed_positions.length)
            currentindex = 0;
    }

    public BlockPos getCurrentPosition() {
        return getPos().add(farmed_positions[currentindex % farmed_positions.length], 0, farmed_positions[Math.floorDiv(currentindex, farmed_positions.length)]);
    }

    @Override
    public void update() {
        super.update();
        if (getWorld().isRemote) {
            //noinspection MethodCallSideOnly
            updateClient();
        } else {
            updateServer();
            notifyClient();
        }
    }

    @SideOnly(Side.CLIENT)
    private void updateClient() {
        if (currentindex == -1) return;
        if (boxRenderer != null) boxRenderer.cleanUp();
        BlockPos current = getCurrentPosition();
        boxRenderer = BoxRenderer.create(
                new Color(0, 255, 255, 50),
                new Vec3d(current.getX() - 0.0005f, current.getY() - 0.0005f, current.getZ() - 0.0005f),
                new Vec3d(current.getX() + 1.0005f, current.getY() + 1.0005f, current.getZ() + 1.0005f)
        );
        boxRenderer.show();
    }

    private void updateServer() {
        // Initialize timer if not already initialized.
        if (timer == null) {
            timer = new TileTimer(world.rand.nextInt(5) + 10);
        }

        // Work if the interval has passed.
        if (timer.tick()) {
            // Work
            if (SimpleRandomStuff.isDev) energyStorage.receiveEnergy(energyStorage.getMaxReceive(), false);
            cyclePosition();
            BlockPos current = getCurrentPosition();
            int treeBlocks = farmer.scanTree(current, false);
            int energyReq = treeBlocks * Configs.TREE_FARM.blockBreakRF;
            if (energyStorage.getEnergyStored() >= energyReq) {
                energyStorage.extractEnergy(energyReq, false);
                farmer.scanTree(current, true);
                if (world.isAirBlock(current)) plantSapling();
                timer.setInterval(treeBlocks);
            }
            timer.setInterval(5);
        }
    }

    private void plantSapling() {
        for (int i = 0; i < inventory.getSlots(); i++) {
            BlockPos thisPos = getCurrentPosition();
            ItemStack stack = inventory.getStackInSlot(i);
            if (stack == ItemStack.EMPTY) continue;
            Item item = stack.getItem();
            if (saplings.stream().filter(s -> stack.getItem() == s.getItem()).count() > 0 /*&& item instanceof ItemBlock*/) {
                Block block = ((ItemBlock) item).getBlock();
                if (block instanceof IPlantable) {
                    IBlockState dirt = getWorld().getBlockState(thisPos.down());
                    if (dirt.getBlock().canSustainPlant(dirt, getWorld(), thisPos.down(), EnumFacing.UP, (IPlantable) block) &&
                            world.setBlockState(thisPos, block.getStateFromMeta(stack.getMetadata()), 3)) {
                        stack.shrink(1);
                    }
                }
            }
        }
    }

    @Override
    protected Battery createBattery() {
        return new BatteryMachine(320000, 10000);
    }

    @Override
    protected ItemStackHandler createInventory() {
        return new ItemStackHandler(72);
    }

    @Override
    public Container createContainer(InventoryPlayer inventory) {
        return new ContainerTreeFarm(inventory, this);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public Gui createGui(InventoryPlayer inventory) {
        return new GuiTreeFarm(inventory, this);
    }
}
