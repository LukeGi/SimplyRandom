package bluemonster.simplyrandom.metals;

import bluemonster.simplyrandom.core.ItemBase;

public class Nugget extends ItemBase {

    public Nugget(String type) {
        super(type + "_nugget");
        setCreativeTab(MetalsCT.INSTANCE);
    }
}
