package bluemonster.simplyrandom.metals;

import bluemonster.simplyrandom.core.ItemBase;

public class DirtyDust extends ItemBase {

    public DirtyDust(String type) {
        super(type + "_dirty_dust");
        setCreativeTab(MetalsCT.INSTANCE);
    }
}
