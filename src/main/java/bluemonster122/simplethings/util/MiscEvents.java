package bluemonster122.simplethings.util;

import bluemonster122.simplethings.tileentity.TileTreeFarm;
import net.minecraftforge.event.terraingen.SaplingGrowTreeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
@Mod.EventBusSubscriber
public class MiscEvents
{
    @SubscribeEvent
    public static void stopGrowth(SaplingGrowTreeEvent evt)
    {
        for (int i = -3; i <= 3 && !evt.getResult().equals(Event.Result.DENY); i++)
        {
            for (int j = -3; j <= 3; j++)
            {
                if (evt.getWorld().getTileEntity(evt.getPos().add(i, 0, j)) instanceof TileTreeFarm)
                {
                    evt.setResult(Event.Result.DENY);
                    break;
                }
            }
        }
    }
}
