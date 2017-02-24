package bluemonster122.simplethings.util;

import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
@SuppressWarnings("MethodCallSideOnly")
@Mod.EventBusSubscriber
public class MiscEvents
{
	@SubscribeEvent
	public static void onrc(PlayerInteractEvent.RightClickItem event)
	{
		if (event.getItemStack().hasTagCompound()) {
			System.out.println(event.getItemStack().getTagCompound());
		}
	}
}
