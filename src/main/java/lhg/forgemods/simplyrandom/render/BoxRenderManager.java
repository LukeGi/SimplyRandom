package lhg.forgemods.simplyrandom.render;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

/**
 * This singleton class needs to be registered on the <@code>MinecraftForge#EVENT_BUS</@code>
 * and is the class that handles the rendering of all the cuboids each tick.
 */
@OnlyIn(Dist.CLIENT)
public class BoxRenderManager
{
    public static final BoxRenderManager INSTANCE = new BoxRenderManager();
    private static List<BoxRender> renders = new ArrayList<>();

    private BoxRenderManager()
    {
    }

    public void addBox(BoxRender r)
    {
        renders.add(r);
    }

    public void removeBox(BoxRender r)
    {
        renders.remove(r);
    }

    @SubscribeEvent
    public void unloadHook(WorldEvent.Unload e)
    {
        for (int i = renders.size() - 1; i >= 0; i--)
        {
            renders.get(i).cleanUp();
        }
    }

    @SubscribeEvent
    public void renderHook(RenderWorldLastEvent e)
    {
        Minecraft mc = Minecraft.getInstance();
        PlayerEntity player = mc.player;
        double playerX = player.prevPosX + (player.posX - player.prevPosX) * e.getPartialTicks();
        double playerY = player.prevPosY + (player.posY - player.prevPosY) * e.getPartialTicks();
        double playerZ = player.prevPosZ + (player.posZ - player.prevPosZ) * e.getPartialTicks();

        GL11.glPushMatrix();
        GL11.glTranslated(-playerX, -playerY, -playerZ);

        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        GlStateManager.disableDepthTest();
        renders.forEach(BoxRender::render);
        GlStateManager.enableDepthTest();

        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glPopMatrix();
    }
}
