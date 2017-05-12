package bluemonster122.mods.simplerandomstuff.client.renderer;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import java.awt.*;

/**
 * This is the INSTANCE class for BoxRenders. It is a Factory class with a .create method.
 * You MUST remember to .show() a box after creation.
 */
@SideOnly(Side.CLIENT)
public class BoxRender {

    private float r;
    private float g;
    private float b;
    private float a;
    private BoxMode mode;
    private int renderList;
    private double[] position;

    /**
     * This is a factory method that will create a box with the <code>BoxMode#NORMAL</code> mode.
     *
     * @param color the colour of the faces of the box.
     * @param pos1  one corner of the box.
     * @param pos2  the opposite corner of the box.
     * @return The created box.
     */
    public static BoxRender create(Color color, Vec3d pos1, Vec3d pos2) {
        return create(color, pos1, pos2, BoxMode.NORMAL);
    }

    /**
     * This is a factory method that will create a box
     *
     * @param color the colour of the faces of the box.
     * @param pos1  one corner of the box.
     * @param pos2  the opposite corner of the box.
     * @param mode  the mode in which the box is drawn
     * @return The created box.
     */
    public static BoxRender create(Color color, Vec3d pos1, Vec3d pos2, BoxMode mode) {
        BoxRender box = new BoxRender(color);
        box.position[0] = pos1.xCoord;
        box.position[1] = pos1.yCoord;
        box.position[2] = pos1.zCoord;
        box.position[3] = pos2.xCoord;
        box.position[4] = pos2.yCoord;
        box.position[5] = pos2.zCoord;
        box.mode = mode;
        return box;
    }

    /**
     * Method used by the <code>BoxRendererManager.class</code> to render each box.
     */
    public void render( ) {
        if (GL11.glIsList(renderList)) GL11.glCallList(renderList);
    }

    /**
     * This method will generate the box in OpenGL memory, leading to it being rendered.
     */
    public void show( ) {
        double xmin = position[0];
        double ymin = position[1];
        double zmin = position[2];
        double xmax = position[3];
        double ymax = position[4];
        double zmax = position[5];

        renderList = GL11.glGenLists(1);
        GL11.glNewList(renderList, GL11.GL_COMPILE);
        GlStateManager.enableDepth();

        VertexBuffer wr = Tessellator.getInstance().getBuffer();

        switch (mode) {
            case NORMAL:
                RenderHelper.drawBox(wr, r, g, b, a, xmin, ymin, zmin, xmax, ymax, zmax);
                RenderHelper.drawBox(wr, r, g, b, a, xmax, ymax, zmax, xmin, ymin, zmin);
                RenderHelper.drawBoxLines(wr, xmin, ymin, zmin, xmax, ymax, zmax);
                break;
            case ENDLESS:
                RenderHelper.drawBoxWithoutEnds(wr, r, g, b, a, xmin, ymin, zmin, xmax, ymax, zmax);
                RenderHelper.drawBoxWithoutEnds(wr, r, g, b, a, xmax, ymax, zmax, xmin, ymin, zmin);
                RenderHelper.drawBoxLines(wr, xmin, ymin, zmin, xmax, ymax, zmax);
                break;
            case NORMAL_NO_LINES:
                RenderHelper.drawBox(wr, r, g, b, a, xmin, ymin, zmin, xmax, ymax, zmax);
                RenderHelper.drawBox(wr, r, g, b, a, xmax, ymax, zmax, xmin, ymin, zmin);
                break;
            case ENDLESS_NO_LINES:
                RenderHelper.drawBoxWithoutEnds(wr, r, g, b, a, xmin, ymin, zmin, xmax, ymax, zmax);
                RenderHelper.drawBoxWithoutEnds(wr, r, g, b, a, xmax, ymax, zmax, xmin, ymin, zmin);
                break;
        }
        GlStateManager.disableDepth();
        GL11.glEndList();
    }

    /**
     * This method will remove the box from the <code>BoxRendererManager.class</code>
     */
    public void cleanUp( ) {
        BoxRendererManager.INSTANCE.removeBox(this);
    }

    /**
     * Property enum used to store the mode of the box
     */
    public enum BoxMode {
        NORMAL, ENDLESS, NORMAL_NO_LINES, ENDLESS_NO_LINES
    }

    private BoxRender(Color color) {
        this.r = color.getRed() / 255f;
        this.g = color.getGreen() / 255f;
        this.b = color.getBlue() / 255f;
        this.a = color.getAlpha() / 255f;
        position = new double[6];
        BoxRendererManager.INSTANCE.addBox(this);
    }
}
