package bluemonster.simplerandomstuff.core;

import bluemonster.simplerandomstuff.event.EventFastBreak;
import net.minecraft.util.ITickable;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.common.MinecraftForge;

public class TileCoreTicking extends TileCore implements ITickable {

    public long lastTime = 0;

    @Override
    public void update() {
        if (!getWorld().isRemote) {
            tick();
        }
    }

    private void tick() {
        long now = getWorld().getTotalWorldTime();
        if (lastTime == now) {
            if (!MinecraftForge.EVENT_BUS.post(new EventFastBreak(this, player))) {
                world.destroyBlock(getPos(), true);
                player.sendStatusMessage(new TextComponentString("This block does not support ticking more than once per game tick!"), true);
            }
        }
        lastTime = now;
    }
}
