package bluemonster.simplerandomstuff.client.renderer;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

/**
 * This Class is used to help render different parts of the cuboids
 */
@SideOnly(Side.CLIENT)
public class RenderHelper {

    /**
     * Draws a box.
     *
     * @param wr   the World Renderer.
     * @param r    Red color
     * @param g    Green color
     * @param b    Blue color
     * @param a    Alpha channel
     * @param xmin Minimum x value
     * @param ymin Minimum y value
     * @param zmin Minimum z value
     * @param xmax Maximum x value
     * @param ymax Maximum y value
     * @param zmax Maximum z value
     */
    public static void drawBox(
            BufferBuilder wr,
            float r,
            float g,
            float b,
            float a,
            double xmin,
            double ymin,
            double zmin,
            double xmax,
            double ymax,
            double zmax
    ) {
        GL11.glColor4d(r, g, b, a);
        GlStateManager.color(r, g, b, a);

        wr.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
        wr.setTranslation(0, 0, 0);

        wr.pos(xmin, ymin, zmin)
                .endVertex();
        wr.pos(xmin, ymax, zmin)
                .endVertex();
        wr.pos(xmax, ymax, zmin)
                .endVertex();
        wr.pos(xmax, ymin, zmin)
                .endVertex();

        wr.pos(xmax, ymin, zmax)
                .endVertex();
        wr.pos(xmax, ymax, zmax)
                .endVertex();
        wr.pos(xmin, ymax, zmax)
                .endVertex();
        wr.pos(xmin, ymin, zmax)
                .endVertex();

        wr.pos(xmin, ymin, zmin)
                .endVertex();
        wr.pos(xmin, ymin, zmax)
                .endVertex();
        wr.pos(xmin, ymax, zmax)
                .endVertex();
        wr.pos(xmin, ymax, zmin)
                .endVertex();

        wr.pos(xmax, ymax, zmin)
                .endVertex();
        wr.pos(xmax, ymax, zmax)
                .endVertex();
        wr.pos(xmax, ymin, zmax)
                .endVertex();
        wr.pos(xmax, ymin, zmin)
                .endVertex();

        wr.pos(xmin, ymin, zmin)
                .endVertex();
        wr.pos(xmax, ymin, zmin)
                .endVertex();
        wr.pos(xmax, ymin, zmax)
                .endVertex();
        wr.pos(xmin, ymin, zmax)
                .endVertex();

        wr.pos(xmin, ymax, zmax)
                .endVertex();
        wr.pos(xmax, ymax, zmax)
                .endVertex();
        wr.pos(xmax, ymax, zmin)
                .endVertex();
        wr.pos(xmin, ymax, zmin)
                .endVertex();

        Tessellator.getInstance()
                .draw();
    }

    /**
     * Draws a box with no top or bottom.
     *
     * @param wr   the World Renderer.
     * @param r    Red color
     * @param g    Green color
     * @param b    Blue color
     * @param a    Alpha channel
     * @param xmin Minimum x value
     * @param ymin Minimum y value
     * @param zmin Minimum z value
     * @param xmax Maximum x value
     * @param ymax Maximum y value
     * @param zmax Maximum z value
     */
    public static void drawBoxWithoutEnds(
            BufferBuilder wr,
            float r,
            float g,
            float b,
            float a,
            double xmin,
            double ymin,
            double zmin,
            double xmax,
            double ymax,
            double zmax
    ) {
        GL11.glColor4d(r, g, b, a);
        GlStateManager.color(r, g, b, a);

        wr.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
        wr.setTranslation(0, 0, 0);

        wr.pos(xmin, ymin, zmin)
                .endVertex();
        wr.pos(xmin, ymax, zmin)
                .endVertex();
        wr.pos(xmax, ymax, zmin)
                .endVertex();
        wr.pos(xmax, ymin, zmin)
                .endVertex();

        wr.pos(xmax, ymin, zmax)
                .endVertex();
        wr.pos(xmax, ymax, zmax)
                .endVertex();
        wr.pos(xmin, ymax, zmax)
                .endVertex();
        wr.pos(xmin, ymin, zmax)
                .endVertex();

        wr.pos(xmin, ymin, zmin)
                .endVertex();
        wr.pos(xmin, ymin, zmax)
                .endVertex();
        wr.pos(xmin, ymax, zmax)
                .endVertex();
        wr.pos(xmin, ymax, zmin)
                .endVertex();

        wr.pos(xmax, ymax, zmin)
                .endVertex();
        wr.pos(xmax, ymax, zmax)
                .endVertex();
        wr.pos(xmax, ymin, zmax)
                .endVertex();
        wr.pos(xmax, ymin, zmin)
                .endVertex();

        Tessellator.getInstance()
                .draw();
    }

    /**
     * Draws an outline for a box.
     *
     * @param wr   the World Renderer
     * @param xmin Minimum x value
     * @param ymin Minimum y value
     * @param zmin Minimum z value
     * @param xmax Maximum x value
     * @param ymax Maximum y value
     * @param zmax Maximum z value
     */
    public static void drawBoxLines(
            BufferBuilder wr,
            double xmin,
            double ymin,
            double zmin,
            double xmax,
            double ymax,
            double zmax
    ) {
        GL11.glColor4d(0, 0, 0, 1);
        GlStateManager.color(0, 0, 0, 1);

        wr.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION);
        wr.setTranslation(0, 0, 0);

        wr.pos(xmin, ymin, zmin)
                .endVertex();
        wr.pos(xmin, ymax, zmin)
                .endVertex();
        wr.pos(xmax, ymax, zmin)
                .endVertex();
        wr.pos(xmax, ymin, zmin)
                .endVertex();

        wr.pos(xmax, ymin, zmax)
                .endVertex();
        wr.pos(xmax, ymax, zmax)
                .endVertex();
        wr.pos(xmin, ymax, zmax)
                .endVertex();
        wr.pos(xmin, ymin, zmax)
                .endVertex();

        wr.pos(xmin, ymin, zmin)
                .endVertex();
        wr.pos(xmin, ymin, zmax)
                .endVertex();
        wr.pos(xmin, ymax, zmax)
                .endVertex();
        wr.pos(xmin, ymax, zmin)
                .endVertex();

        wr.pos(xmax, ymax, zmin)
                .endVertex();
        wr.pos(xmax, ymax, zmax)
                .endVertex();
        wr.pos(xmax, ymin, zmax)
                .endVertex();
        wr.pos(xmax, ymin, zmin)
                .endVertex();

        wr.pos(xmin, ymin, zmin)
                .endVertex();
        wr.pos(xmax, ymin, zmin)
                .endVertex();
        wr.pos(xmax, ymin, zmax)
                .endVertex();
        wr.pos(xmin, ymin, zmax)
                .endVertex();

        wr.pos(xmin, ymax, zmax)
                .endVertex();
        wr.pos(xmax, ymax, zmax)
                .endVertex();
        wr.pos(xmax, ymax, zmin)
                .endVertex();
        wr.pos(xmin, ymax, zmin)
                .endVertex();

        Tessellator.getInstance()
                .draw();
    }
}