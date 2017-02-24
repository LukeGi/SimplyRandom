package bluemonster122.simplethings.network;

import bluemonster122.simplethings.SimpleThings;
import bluemonster122.simplethings.network.message.MessageParticle;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
public class NetworkManager
{
	public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(SimpleThings.MOD_ID);
	
	public static void init() {
		int id = 0;
		INSTANCE.registerMessage(MessageParticle.MessageHandler.class, MessageParticle.class, id++, Side.CLIENT);
	}
}
