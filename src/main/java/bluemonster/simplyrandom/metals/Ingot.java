package bluemonster.simplyrandom.metals;

import bluemonster.simplyrandom.core.ItemBase;

public class Ingot extends ItemBase {

    public Ingot(String type) {
        super(type + "_ingot");
        setCreativeTab(MetalsCT.INSTANCE);
    }
}
