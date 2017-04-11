package bluemonster122.mods.simplethings.tileentity;

import bluemonster122.mods.simplethings.client.renderer.BoxRender;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.awt.*;

public class TileTest extends TileEntity {
    private BoxRender[] render;
    private float[][] offsets = {{-1, 1}, {0, 1}, {1, 1}, {-1, 0}, {1, 0}, {-1, -1}, {0, -1}, {1, -1}};

    @SideOnly(Side.CLIENT)
    public void genBoxRender() {
        if (FMLCommonHandler.instance().getSide().isClient()) {
            if (render == null) render = new BoxRender[8];
            for (int i = 0; i < 8; i++) {
                if (render[i] != null) render[i].cleanUp();
                BlockPos pos = getPos();
                render[i] = BoxRender.create(new Color(0, 255, 255, 50),
                        new Vec3d(pos.getX() + 0.25f + offsets[i][0], pos.getY() + 0.25f, pos.getZ() + 0.25f + offsets[i][1]),
                        new Vec3d(pos.getX() + 0.75f + offsets[i][0], pos.getY() + 0.75f, pos.getZ() + 0.75f + offsets[i][1]));
                render[i].show();
            }
        }
    }

    @SideOnly(Side.CLIENT)
    public void cleanUp() {
        for (BoxRender boxRender : render) {
            boxRender.cleanUp();
        }
    }
}
