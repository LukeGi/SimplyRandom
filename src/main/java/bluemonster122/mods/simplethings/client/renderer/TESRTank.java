package bluemonster122.mods.simplethings.client.renderer;

import bluemonster122.mods.simplethings.tanks.TileTank;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import org.lwjgl.opengl.GL11;

public class TESRTank extends TileEntitySpecialRenderer<TileTank> {

    protected static Minecraft mc = Minecraft.getMinecraft();
    private static float d = 0.005f;
    private static float l = 0.0625f;

    @Override
    public void renderTileEntityAt(TileTank te, double x, double y, double z, float partialTicks, int destroyStage) {
        FluidTank tank = te.tank;
        FluidStack liquid = tank.getFluid();

        if (liquid == null) return;

        float height = ((float) liquid.amount - d) / (float) tank.getCapacity();


        int brightness = mc.world.getCombinedLight(te.getPos(), liquid.getFluid().getLuminosity(liquid));
        int color = liquid.getFluid().getColor(liquid);

        GlStateManager.pushMatrix();


        net.minecraft.client.renderer.RenderHelper.disableStandardItemLighting();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        if(Minecraft.isAmbientOcclusionEnabled()) {
            GL11.glShadeModel(GL11.GL_SMOOTH);
        }
        else {
            GL11.glShadeModel(GL11.GL_FLAT);
        }

        Tessellator tessellator = Tessellator.getInstance();
        VertexBuffer renderer = tessellator.getBuffer();
        renderer.begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);
        mc.renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);

        TextureAtlasSprite still = mc.getTextureMapBlocks().getTextureExtry(liquid.getFluid().getStill(liquid).toString());
        TextureAtlasSprite flowing = mc.getTextureMapBlocks().getTextureExtry(liquid.getFluid().getFlowing(liquid).toString());

        double x1 = x + d + l;
        double y1 = y + d;
        double z1 = z + d + l;
        double x2 = x + 1 - d - l;
        double y2 = y + height - d;
        double z2 = z + 1 - d - l;
        double u1 = flowing.getInterpolatedU(1);
        double v1 = flowing.getInterpolatedV(16 * (y2 - y1));
        double u2 = flowing.getInterpolatedU(15);
        double v2 = flowing.getInterpolatedV(0);
        int a = color >> 0x18 & 0xFF;
        int r = color >> 0x10 & 0xFF;
        int g = color >> 0x8 & 0xFF;
        int b = color & 0xFF;
        int l1 = brightness >> 0x10 & 0xFFFF;
        int l2 = brightness & 0xFFFF;

        // NORTH
        renderer.pos(x1, y1, z1).color(r, g, b, a).tex(u1, v1).lightmap(l1, l2).endVertex();
        renderer.pos(x1, y2, z1).color(r, g, b, a).tex(u1, v2).lightmap(l1, l2).endVertex();
        renderer.pos(x2, y2, z1).color(r, g, b, a).tex(u2, v2).lightmap(l1, l2).endVertex();
        renderer.pos(x2, y1, z1).color(r, g, b, a).tex(u2, v1).lightmap(l1, l2).endVertex();

        // SOUTH
        renderer.pos(x2, y1, z2).color(r, g, b, a).tex(u1, v1).lightmap(l1, l2).endVertex();
        renderer.pos(x2, y2, z2).color(r, g, b, a).tex(u1, v2).lightmap(l1, l2).endVertex();
        renderer.pos(x1, y2, z2).color(r, g, b, a).tex(u2, v2).lightmap(l1, l2).endVertex();
        renderer.pos(x1, y1, z2).color(r, g, b, a).tex(u2, v1).lightmap(l1, l2).endVertex();

        u1 = flowing.getInterpolatedU(15);
        v1 = flowing.getInterpolatedV(16 * (y2 - y1));
        u2 = flowing.getInterpolatedU(1);
        v2 = flowing.getInterpolatedV(0);

        // WEST
        renderer.pos(x1, y1, z1).color(r, g, b, a).tex(u2, v1).lightmap(l1, l2).endVertex();
        renderer.pos(x1, y1, z2).color(r, g, b, a).tex(u1, v1).lightmap(l1, l2).endVertex();
        renderer.pos(x1, y2, z2).color(r, g, b, a).tex(u1, v2).lightmap(l1, l2).endVertex();
        renderer.pos(x1, y2, z1).color(r, g, b, a).tex(u2, v2).lightmap(l1, l2).endVertex();

        // EAST
        renderer.pos(x2, y1, z1).color(r, g, b, a).tex(u1, v1).lightmap(l1, l2).endVertex();
        renderer.pos(x2, y2, z1).color(r, g, b, a).tex(u1, v2).lightmap(l1, l2).endVertex();
        renderer.pos(x2, y2, z2).color(r, g, b, a).tex(u2, v2).lightmap(l1, l2).endVertex();
        renderer.pos(x2, y1, z2).color(r, g, b, a).tex(u2, v1).lightmap(l1, l2).endVertex();

        u1 = still.getInterpolatedU(1);
        v1 = still.getInterpolatedV(1);
        u2 = still.getInterpolatedU(15);
        v2 = still.getInterpolatedV(15);

        // DOWN
        renderer.pos(x1, y1, z1).color(r, g, b, a).tex(u1, v1).lightmap(l1, l2).endVertex();
        renderer.pos(x2, y1, z1).color(r, g, b, a).tex(u2, v1).lightmap(l1, l2).endVertex();
        renderer.pos(x2, y1, z2).color(r, g, b, a).tex(u2, v2).lightmap(l1, l2).endVertex();
        renderer.pos(x1, y1, z2).color(r, g, b, a).tex(u1, v2).lightmap(l1, l2).endVertex();

        // UP
        renderer.pos(x1, y2, z2).color(r, g, b, a).tex(u1, v1).lightmap(l1, l2).endVertex();
        renderer.pos(x2, y2, z2).color(r, g, b, a).tex(u1, v2).lightmap(l1, l2).endVertex();
        renderer.pos(x2, y2, z1).color(r, g, b, a).tex(u2, v2).lightmap(l1, l2).endVertex();
        renderer.pos(x1, y2, z1).color(r, g, b, a).tex(u2, v1).lightmap(l1, l2).endVertex();

        Tessellator.getInstance().draw();

        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
        net.minecraft.client.renderer.RenderHelper.enableStandardItemLighting();

    }
}
