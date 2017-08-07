package bluemonster122.mods.simplerandomstuff.core;

import bluemonster122.mods.simplerandomstuff.SRS;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.MinecraftForge;

public class TileEntityTickingSRS
  extends TileEntitySRS
  implements ITickable
{
  protected boolean work = false;
  
  private long lastTime = -1;
  
  @Override
  public void update()
  {
    if (!getWorld().isRemote)
    {
      long sinceLastTime = (System.currentTimeMillis() - lastTime);
      SRS.logger.info(sinceLastTime);
      if (lastTime == -1 || sinceLastTime > 50)
      {
        lastTime = System.currentTimeMillis();
        work = true;
      }
      else
      {
        work = false;
        if (sinceLastTime < 1 && !MinecraftForge.EVENT_BUS.post(new EventSuperBrokenUpdate(this)))
        {
          world.destroyBlock(getPos(), true);
        }
      }
    }
  }
}
