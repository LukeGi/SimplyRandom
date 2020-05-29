package luhegi.mods.simplyrandom.basis.config;

import luhegi.mods.simplyrandom.basis.setup.ModSetupManager;
import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public class BasisConfig {

    public static class Client {
        public Client(ForgeConfigSpec.Builder spec) {
            ModSetupManager.INSTANCE.onClientConfig(spec);
        }
    }

    public static class Server {
        public Server(ForgeConfigSpec.Builder spec) {
            ModSetupManager.INSTANCE.onServerConfig(spec);
        }
    }


    public static final ForgeConfigSpec clientSpec;
    public static final Client CLIENT;

    static {
        final Pair<Client, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Client::new);
        clientSpec = specPair.getRight();
        CLIENT = specPair.getLeft();
    }

    public static final ForgeConfigSpec serverSpec;
    public static final Server SERVER;

    static {
        final Pair<Server, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Server::new);
        serverSpec = specPair.getRight();
        SERVER = specPair.getLeft();
    }
}
