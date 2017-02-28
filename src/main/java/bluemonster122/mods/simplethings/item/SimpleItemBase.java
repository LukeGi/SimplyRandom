package bluemonster122.mods.simplethings.item;

import net.minecraft.item.Item;

public class SimpleItemBase extends Item {
    public SimpleItemBase(String name) {
        setRegistryName(name);
        setUnlocalizedName(getRegistryName().getResourceDomain() + "." + name);
    }
}
