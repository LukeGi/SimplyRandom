package bluemonster.simplyrandom.metals;

import bluemonster.simplyrandom.core.ItemBase;

public class Dust extends ItemBase {

    public Dust(String type) {
        super(type + "_dust");
        setCreativeTab(MetalsCT.INSTANCE);
    }
}
