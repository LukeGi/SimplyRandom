package bluemonster122.mods.simplethings.core;

import bluemonster122.mods.simplethings.SimpleThings;
import net.minecraft.item.Item;

public class ItemST extends Item {
    public ItemST(String name, boolean hasSubTypes) {
        setRegistryName(name);
        setHasSubtypes(hasSubTypes);
        setCreativeTab(SimpleThings.theTab);
    }
}
