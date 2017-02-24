package bluemonster122.simplethings.tileentity;

import bluemonster122.simplethings.handler.ConfigurationHandler;
import bluemonster122.simplethings.network.NetworkManager;
import bluemonster122.simplethings.network.message.MessageParticle;
import bluemonster122.simplethings.tileentity.core.IMachine;
import bluemonster122.simplethings.tileentity.core.TileEntityST;
import bluemonster122.simplethings.util.AreaType;
import com.google.common.collect.ImmutableSet;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
public class TileTreeFarm extends TileEntityST implements ITickable, IMachine
{
	public static Set<Block> ALLOWED_FARMING_BLOCKS = ImmutableSet.of(Blocks.DIRT, Blocks.FARMLAND, Blocks.GRASS);
	private final Predicate<BlockPos> isWood = p -> getWorld().getBlockState(p).getMaterial().equals(
	  Material.WOOD) && getWorld().getBlockState(p).getBlock() instanceof BlockLog;
	private final Predicate<BlockPos> isLeaves = p -> getWorld().getBlockState(p).getMaterial().equals(
	  Material.LEAVES) && getWorld().getBlockState(p).getBlock() instanceof BlockLeaves;
	private AreaType farmedArea = AreaType.SMALL;
	private ItemStackHandler inventory = new ItemStackHandler(72);
	private EnergyStorage battery = new EnergyStorage(1000000);
	private Queue<WorkingPos> toBreak = new PriorityQueue<>();
	private long workToTime;
	
	@Override
	public void update()
	{
		if (getWorld().isRemote)
		{
			return;
		}
		if (farmedArea == null)
		{
			return;
		}
		workToTime = System.currentTimeMillis() + 100;
		if (getWorld().getTotalWorldTime() % 10 == 0)
		{
			List<BlockPos> farmSpots = farmedArea.getArea().stream().map(p -> getPos().add(p)).collect(
			  Collectors.toList());
			List<BlockPos> air = farmSpots.stream().filter(getWorld()::isAirBlock).filter(this::isValidBlock).collect(
			  Collectors.toList());
			if (!air.isEmpty())
			{
				fillAnAir(air);
			}
			List<BlockPos> grown = farmSpots.stream().filter(p -> !toBreak.contains(p)).filter(isWood).collect(
			  Collectors.toList());
			if (grown.size() > 0)
			{
				scan(grown.get(0));
			}
		}
		while (!toBreak.isEmpty() && work())
		{
			handleBlockBreaking(toBreak.poll());
		}
	}
	
	private boolean work()
	{
		return System.currentTimeMillis() < workToTime;
	}
	
	private void scan(BlockPos blockPos)
	{
		List<WorkingPos> treeBlocks = new ArrayList<>();
		Stack<BlockPos> toScan = new Stack<>();
		toScan.add(blockPos);
		while (!toScan.isEmpty())
		{
			BlockPos element = toScan.pop();
			if (isWood.test(element) || isLeaves.test(element))
			{
				treeBlocks.add(new WorkingPos(element));
			}
			for (BlockPos offset : AreaType.AROUND.getArea())
			{
				for (int i = -1; i <= 1; i++)
				{
					BlockPos current = element.add(offset.getX(), offset.getY() + i, offset.getZ());
					if (!toScan.contains(current) && !treeBlocks.contains(current) && isLeaves.or(isWood).test(current))
					{
						toScan.add(current);
					}
				}
			}
		}
		toBreak.addAll(treeBlocks.stream().filter(bp -> !toBreak.contains(bp)).collect(Collectors.toList()));
	}
	
	private void fillAnAir(List<BlockPos> air)
	{
		if (getBattery().getEnergyStored() < ConfigurationHandler.tree_farm_place_energy)
		{
			return;
		}
		int i = 0;
		BlockPos blockPos = air.get(i);
		while (!getWorld().isAirBlock(blockPos))
		{
			if (i < air.size())
			{
				blockPos = air.get(i++);
			}
		}
		for (int j = 0; j < getInventory().getSlots(); j++)
		{
			ItemStack stack = getInventory().getStackInSlot(j);
			if (stack != ItemStack.EMPTY && stack.getItem() instanceof ItemBlock)
			{
				Block block = ((ItemBlock) stack.getItem()).getBlock();
				if (block == Blocks.SAPLING)
				{
					BlockPlanks.EnumType type = BlockPlanks.EnumType.byMetadata(stack.getItemDamage());
					getWorld().setBlockState(
					  blockPos, block.getDefaultState().withProperty(BlockSapling.TYPE, type), 3);
					getInventory().extractItem(j, 1, false);
					getBattery().extractEnergy(ConfigurationHandler.tree_farm_place_energy, false);
				}
			}
		}
		sendUpdate();
	}
	
	private void handleBlockBreaking(WorkingPos blockPos)
	{
		List<ItemStack> drops;
		IBlockState blockState;
		Block block;
		if (getBattery().getEnergyStored() < ConfigurationHandler.tree_farm_break_energy)
		{
			return;
		}
		blockState = getWorld().getBlockState(blockPos);
		block = blockState.getBlock();
		drops = block.getDrops(getWorld(), blockPos, blockState, 0);
		if (drops.isEmpty() || ItemHandlerHelper.insertItem(getInventory(), drops.get(0), true) == ItemStack.EMPTY)
		{
			getWorld().setBlockToAir(blockPos);
			drops.forEach(itemStack -> ItemHandlerHelper.insertItem(getInventory(), itemStack, false));
			getBattery().extractEnergy(ConfigurationHandler.tree_farm_break_energy, false);
			if (world.rand.nextBoolean())
			{
				for (int i = 0; i < world.rand.nextInt(5); i++)
				{
					NetworkManager.INSTANCE.sendToAllAround(
					  new MessageParticle(EnumParticleTypes.PORTAL, blockPos.getX(), blockPos.getY(), blockPos.getZ(),
					                      world.rand.nextDouble() / 100, world.rand.nextDouble() / 10,
					                      world.rand.nextDouble() / 100
					  ), new NetworkRegistry.TargetPoint(getWorld().provider.getDimension(), blockPos.getX(),
					                                     blockPos.getY(), blockPos.getZ(), 32
					  ));
				}
			}
		}
		else
		{
			toBreak.add(blockPos);
		}
	}
	
	public void breakSaplings()
	{
		if (farmedArea == null)
		{
			return;
		}
		farmedArea.getArea().stream().map(getPos()::add).filter(
		  b -> getWorld().getBlockState(b).getBlock() instanceof BlockBush).forEach(
		  blockPos -> getWorld().destroyBlock(blockPos, true));
	}
	
	private boolean isValidBlock(BlockPos down)
	{
		return ALLOWED_FARMING_BLOCKS.contains(getWorld().getBlockState(down.down()).getBlock());
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
	
	private class WorkingPos extends BlockPos
	{
		public WorkingPos(BlockPos vec)
		{
			super(vec);
		}
		
		@Override
		public int compareTo(Vec3i that)
		{
			return that.getY() == this.getY() ? (that.getZ() == this.getZ() ? that.getX() - this.getX() : that.getZ() - this.getZ()) : that.getY() - this.getY();
		}
	}
}
