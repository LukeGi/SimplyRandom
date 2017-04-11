package bluemonster122.mods.simplethings.tileentity;

import bluemonster122.mods.simplethings.SimpleThings;
import bluemonster122.mods.simplethings.network.message.MessageParticle;
import bluemonster122.mods.simplethings.tileentity.core.TileEntityST;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.common.network.NetworkRegistry;

import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class TileLightningRod extends TileEntityST implements ITickable {
    private boolean hasGold = false, hasWood = false, hasPiston = false, hasWater = false;
    private int workLeft = 60;
    private long lastTime = 60;

    @Override
    public void update() {
        if (getWorld().isRemote) return;


        if (getWorld().getBlockState(getPos().down()).getBlock().equals(Blocks.IRON_BLOCK) && lastTime <= 0 && hasWater && hasGold && hasPiston && hasWood) {
            if (workLeft % 5 == 0)
                getWorld().addWeatherEffect(
                        new EntityLightningBolt(
                                getWorld(),
                                getPos().getX(),
                                getPos().getY() + 1,
                                getPos().getZ(),
                                true)
                );
            SimpleThings.logger.info(">>> Spawning Particles Now...");
            workLeft--;
            lastTime = workLeft;
        } else {
            if (workLeft > 15) {
                for (int i = 0; i < 30 - workLeft / 2; i++) {
                    double xCoord = getPos().getX() + 0.5 + (world.rand.nextGaussian() * 0.5f);
                    double yCoord = getPos().getY() + 1 + (world.rand.nextGaussian() * 0.5f);
                    double zCoord = getPos().getZ() + 0.5 + (world.rand.nextGaussian() * 0.5f);
                    SimpleThings.channel.sendToAllAround(
                            new MessageParticle(
                                    EnumParticleTypes.PORTAL,
                                    xCoord,
                                    yCoord,
                                    zCoord,
                                    (xCoord - (getPos().getX() + 0.5)),
                                    (yCoord - (getPos().getY() + 1)),
                                    (zCoord - (getPos().getZ() + 0.5))
                            ), new NetworkRegistry.TargetPoint(
                                    getWorld().provider.getDimension(),
                                    getPos().getX(),
                                    getPos().getY(),
                                    getPos().getZ(),
                                    32
                            )
                    );
                }
            }
            lastTime--;
        }
        if (workLeft < 15 || (workLeft % 3 == 0 && workLeft < 60)) {
            SimpleThings.channel.sendToAllAround(
                    new MessageParticle(
                            EnumParticleTypes.FLAME,
                            getPos().getX() + 0.5,
                            getPos().getY() + 1,
                            getPos().getZ() + 0.5,
                            0,
                            1,
                            0
                    ), new NetworkRegistry.TargetPoint(
                            getWorld().provider.getDimension(),
                            getPos().getX(),
                            getPos().getY(),
                            getPos().getZ(),
                            32
                    )
            );
        }
        if (workLeft == 0) {
            getWorld().setBlockState(getPos().down(), SimpleThings.machine_block.getDefaultState());
            getWorld().destroyBlock(pos, false);
            for (int i = 0; i < 10; i++) {
                getWorld().addWeatherEffect(
                        new EntityLightningBolt(
                                getWorld(),
                                getPos().getX(),
                                getPos().getY(),
                                getPos().getZ(),
                                true)
                );
            }
        }
    }

    public boolean addWater() {
        if (hasWater) {
            return false;
        } else {
            hasWater = true;
            return true;
        }
    }

    public boolean addGold() {
        if (hasGold) {
            return false;
        } else {
            hasGold = true;
            return true;
        }
    }

    public boolean addWood() {
        if (hasWood) {
            return false;
        } else {
            hasWood = true;
            return true;
        }
    }

    public boolean addPiston() {
        if (hasPiston) {
            return false;
        } else {
            hasPiston = true;
            return true;
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound.setInteger("workLeft", workLeft);
        compound.setLong("lastTime", lastTime);
        compound.setBoolean("hasWater", hasWater);
        compound.setBoolean("hasGold", hasGold);
        compound.setBoolean("hasPiston", hasPiston);
        compound.setBoolean("hasWood", hasWood);
        return super.writeToNBT(compound);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        workLeft = compound.getInteger("workLeft");
        lastTime = compound.getLong("lastTime");
        hasWater = compound.getBoolean("hasWater");
        hasWood = compound.getBoolean("hasWood");
        hasPiston = compound.getBoolean("hasPiston");
        hasGold = compound.getBoolean("hasGold");
        super.readFromNBT(compound);
    }

    @Override
    public Set<Consumer<NBTTagCompound>> getAllWrites() {
        return null;
    }

    @Override
    public Set<Consumer<NBTTagCompound>> getAllReads() {
        return null;
    }

    @Override
    public Map<Capability, Supplier<Capability>> getCaps() {
        return null;
    }

    @Override
    public Set<Consumer<NBTTagCompound>> getMinWrites() {
        return null;
    }

    @Override
    public Set<Consumer<NBTTagCompound>> getMinReads() {
        return null;
    }

    // TODO: 11/19/2016 Make this functional
    // TODO: 11/19/2016 Make this be used to make machine block
    // TODO: 11/19/2016 add recipe that includes quartz and lapis
    // TODO: 11/19/2016 make the rod emit lots of end rod effects that change colour
}
