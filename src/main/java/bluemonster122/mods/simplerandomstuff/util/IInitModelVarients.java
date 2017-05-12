package bluemonster122.mods.simplerandomstuff.util;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public interface IInitModelVarients {
    @SideOnly(Side.CLIENT)
    void initModelsAndVariants( );
}
