package bluemonster.simplerandomstuff.client.renderer;

import cofh.api.item.IToolHammer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

/**
 * This singleton class needs to be registered on the <@code>MinecraftForge#EVENT_BUS</@code>
 * and is the class that handles the rendering of all the cuboids each tick.
 */
@SideOnly(Side.CLIENT)
public class BoxRendererManager {
    public static final BoxRendererManager INSTANCE = new BoxRendererManager();

    private static List<BoxRender> renders = new ArrayList<>();

    public void addBox(BoxRender r) {
        renders.add(r);
    }

    public void removeBox(BoxRender r) {
        renders.remove(r);
    }

    @SubscribeEvent
    public void unloadHook(WorldEvent.Unload e) {
        for (int i = renders.size() - 1; i >= 0; i--) {
            renders.get(i)
                    .cleanUp();
        }
    }

    @SubscribeEvent
    public void renderHook(RenderWorldLastEvent e) {
        Minecraft mc = FMLClientHandler.instance()
                .getClient();
        EntityPlayer player = mc.player;
        if (player.getHeldItem(EnumHand.MAIN_HAND).getItem() instanceof IToolHammer ||
                player.getHeldItem(EnumHand.OFF_HAND).getItem() instanceof IToolHammer) {
            double playerX = player.prevPosX + (player.posX - player.prevPosX) * e.getPartialTicks();
            double playerY = player.prevPosY + (player.posY - player.prevPosY) * e.getPartialTicks();
            double playerZ = player.prevPosZ + (player.posZ - player.prevPosZ) * e.getPartialTicks();

            GL11.glPushMatrix();
            GL11.glTranslated(-playerX, -playerY, -playerZ);

            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

            GlStateManager.disableDepth();
            renders.forEach(BoxRender::render);
            GlStateManager.enableDepth();

            GL11.glEnable(GL11.GL_TEXTURE_2D);
            GL11.glDisable(GL11.GL_BLEND);
            GL11.glPopMatrix();
        }
    }

    private BoxRendererManager() {
    }
}
