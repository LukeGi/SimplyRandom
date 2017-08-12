package bluemonster.simplerandomstuff.overlayoverhaul;

import bluemonster.simplerandomstuff.reference.ModInfo;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.FoodStats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.client.GuiIngameForge;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.ISpecialArmor;
import net.minecraftforge.event.entity.player.PlayerPickupXpEvent;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class OverlayOverriders {

    private static final int BAR_WIDTH = 202;
    private static final int BAR_HEIGHT = 5;
    private static final int SMALL_BAR_WIDTH = 182;
    private static final int SMALL_BAR_HEIGHT = 3;
    private static final ResourceLocation BARS = new ResourceLocation(ModInfo.MOD_ID, "textures/gui/bars.png");
    private static boolean inCombat = false;
    private static int height = 23;

    private static void addHeight(int toAdd) {
        setHeight(getHeight() + toAdd);
    }

    public static void AirGui(Minecraft mc) {
        //15x29
        ScaledResolution sr = new ScaledResolution(mc);
        int top = (sr.getScaledHeight() - 22) / 2;
        int left = (sr.getScaledWidth() + 55) / 2;

        EntityPlayerSP player = mc.player;
        if (!player.isInsideOfMaterial(Material.WATER)) return;
        int perc = Math.round(29 * player.getAir() / 300f); // PERC
        mc.getTextureManager()
                .bindTexture(BARS);

        // Draw a blank small bar
        GlStateManager.enableBlend();
        Gui.drawModalRectWithCustomSizedTexture(left, top, 226, 0, 15, 29, 256, 256);
        // Draw the air bar
        Gui.drawModalRectWithCustomSizedTexture(left, top + 29 - perc, 241, 29 - perc, 15, perc, 256, 256);
        GlStateManager.disableBlend();

        mc.getTextureManager()
                .bindTexture(Gui.ICONS);
    }

    private static void MountHealthGui(Minecraft mc) {
        EntityPlayer player = mc.player;

        if (!(player.getRidingEntity() instanceof EntityLivingBase)) return;

        ScaledResolution sr = new ScaledResolution(mc);
        int top = sr.getScaledHeight() - BAR_HEIGHT - getHeight();
        int left = (sr.getScaledWidth() - BAR_WIDTH) / 2;

        addHeight(BAR_HEIGHT);

        EntityLivingBase ridingEntity = (EntityLivingBase) player.getRidingEntity();
        float perc = ridingEntity.getHealth() / ridingEntity.getMaxHealth();
        int y = 50;

        mc.getTextureManager()
                .bindTexture(BARS);
        GlStateManager.enableBlend();
        Gui.drawModalRectWithCustomSizedTexture(left, top, 0, 123, BAR_WIDTH, BAR_HEIGHT, 256, 256);
        GlStateManager.disableBlend();
        Gui.drawModalRectWithCustomSizedTexture(left, top, 0, y, Math.round(BAR_WIDTH * perc), BAR_HEIGHT, 256, 256);
        mc.getTextureManager()
                .bindTexture(Gui.ICONS);
    }

    private static void JumpBarGui(Minecraft mc) {
        //13x47
        ScaledResolution sr = new ScaledResolution(mc);
        int top = (sr.getScaledHeight() - 47) / 2;
        int left = (sr.getScaledWidth() - 100) / 2;

        EntityPlayerSP player = mc.player;

        if (player.getHorseJumpPower() <= 0) return;

        int perc = Math.round(player.getHorseJumpPower() * 47f); // PERC
        mc.getTextureManager()
                .bindTexture(BARS);

        // Draw a blank small bar
        GlStateManager.enableBlend();
        Gui.drawModalRectWithCustomSizedTexture(left, top, 230, 29, 13, 47, 256, 256);
        // Draw the air bar
        Gui.drawModalRectWithCustomSizedTexture(left, top + 47 - perc, 243, 76f - perc, 13, perc, 256, 256);
        GlStateManager.disableBlend();

        mc.getTextureManager()
                .bindTexture(Gui.ICONS);
    }

    public boolean HealthGui(Minecraft mc) {
        EntityPlayer player = mc.player;
        boolean wither = player.isPotionActive(MobEffects.WITHER);
        boolean poison = player.isPotionActive(MobEffects.POISON);
        boolean regen = player.isPotionActive(MobEffects.REGENERATION);
        boolean isInCombat = !player.world.getEntitiesWithinAABB(
                EntityMob.class,
                new AxisAlignedBB(player.getPosition()).expand(8, 5, 8)
        ).isEmpty();
        if (!(player.getHealth() == player.getMaxHealth() && player.getAbsorptionAmount() == 0 && !wither && !poison && !regen && !isInCombat)) {
            ScaledResolution sr = new ScaledResolution(mc);
            int top = sr.getScaledHeight() - BAR_HEIGHT - getHeight();
            int left = (sr.getScaledWidth() - BAR_WIDTH) / 2;

            addHeight(BAR_HEIGHT);

            float perc = player.getHealth() / player.getMaxHealth();
            int y;

            if (wither) {
                y = 20;
            } else if (poison) {
                y = 25;
            } else if (regen) {
                y = 30;
            } else if (perc <= 0.15f) {
                y = 5;
            } else if (perc <= 0.60f) {
                y = 10;
            } else {
                y = 15;
            }

            mc.getTextureManager()
                    .bindTexture(BARS);
            GlStateManager.enableBlend();
            Gui.drawModalRectWithCustomSizedTexture(left, top, 0, 123, BAR_WIDTH, BAR_HEIGHT, 256, 256);
            GlStateManager.disableBlend();
            Gui.drawModalRectWithCustomSizedTexture(left, top, 0, y, Math.round(BAR_WIDTH * perc), BAR_HEIGHT, 256, 256);
            if (player.getAbsorptionAmount() > 0) {
                Gui.drawModalRectWithCustomSizedTexture(
                        left,
                        top,
                        0,
                        35,
                        (int) (BAR_WIDTH * (player.getAbsorptionAmount() / 20f)),
                        BAR_HEIGHT,
                        256,
                        256
                );
            }
            mc.getTextureManager().bindTexture(Gui.ICONS);
        }
        return isInCombat;
    }

    public void FoodGui(Minecraft mc) {
        EntityPlayerSP player = mc.player;
        boolean hunger = player.isPotionActive(MobEffects.HUNGER);
        FoodStats foodStats = player.getFoodStats();
        if (foodStats.getFoodLevel() >= 20 && !hunger) return;
        float perc = (float) foodStats.getFoodLevel() / 20f;

        ScaledResolution sr = new ScaledResolution(mc);
        float saturationLevel = foodStats.getSaturationLevel();
        int top = sr.getScaledHeight() - getHeight() - (saturationLevel > 0 ? 1 : 0) - SMALL_BAR_HEIGHT;
        int left = (sr.getScaledWidth() - SMALL_BAR_WIDTH) / 2;

        addHeight(SMALL_BAR_HEIGHT);
        if (saturationLevel > 0) {
            addHeight(2);
        }

        int y;

        if (hunger) {
            y = 131;
        } else if (perc <= 0.15f) {
            y = 5;
        } else {
            y = 128;
        }

        mc.getTextureManager()
                .bindTexture(BARS);
        GlStateManager.enableBlend();
        Gui.drawModalRectWithCustomSizedTexture(left, top, 0, 253, SMALL_BAR_WIDTH, SMALL_BAR_HEIGHT, 256, 256);
        GlStateManager.disableBlend();
        Gui.drawModalRectWithCustomSizedTexture(
                left,
                top,
                0,
                y,
                Math.round(SMALL_BAR_WIDTH * perc),
                SMALL_BAR_HEIGHT,
                256,
                256
        );
        if (saturationLevel > 0f) {
            perc = saturationLevel / 20f;
            y = 45;
            Gui.drawModalRectWithCustomSizedTexture(
                    left - 1,
                    top - 1,
                    0,
                    y,
                    Math.round((SMALL_BAR_WIDTH + 2) * perc),
                    SMALL_BAR_HEIGHT + 2,
                    256,
                    256
            );
        }
        mc.getTextureManager()
                .bindTexture(Gui.ICONS);
    }

    public void XPGui(Minecraft mc) {
        ScaledResolution sr = new ScaledResolution(mc);
        int top = sr.getScaledHeight() - getHeight() - SMALL_BAR_HEIGHT;
        int left = (sr.getScaledWidth() - SMALL_BAR_WIDTH) / 2;

        addHeight(SMALL_BAR_HEIGHT);

        float experience = mc.player.experience; // PERC

        mc.getTextureManager()
                .bindTexture(BARS);
        GlStateManager.enableBlend();
        Gui.drawModalRectWithCustomSizedTexture(left, top, 0, 253, SMALL_BAR_WIDTH, SMALL_BAR_HEIGHT, 256, 256);
        GlStateManager.disableBlend();
        Gui.drawModalRectWithCustomSizedTexture(
                left,
                top,
                0,
                134,
                Math.round(SMALL_BAR_WIDTH * experience),
                SMALL_BAR_HEIGHT,
                256,
                256
        );
        mc.getTextureManager()
                .bindTexture(Gui.ICONS);
    }

    public void LevelGui(Minecraft mc) {
        EntityPlayerSP player = mc.player;
        int level = player.experienceLevel;
        if (!player.isSneaking() || player.posX - player.lastTickPosX + player.posY - player.lastTickPosY + player.posZ - player.lastTickPosZ != 0) {
            return;
        }

        ScaledResolution sr = new ScaledResolution(mc);
        FontRenderer font = mc.fontRenderer;
        int top = sr.getScaledHeight() - getHeight() - font.FONT_HEIGHT;
        int left = (sr.getScaledWidth() - font.getStringWidth(String.valueOf(level))) / 2;

        addHeight(font.FONT_HEIGHT);

        font.drawString(String.valueOf(level), left, top, 0x4AD126);
        GlStateManager.color(1, 1, 1, 1);
        mc.getTextureManager()
                .bindTexture(Gui.ICONS);
    }

    public void ArmorGui(Minecraft mc) {
        ScaledResolution sr = new ScaledResolution(mc);
        int top = sr.getScaledHeight() - getHeight() - SMALL_BAR_HEIGHT;
        int left = (sr.getScaledWidth() - SMALL_BAR_WIDTH) / 2;

        addHeight(SMALL_BAR_HEIGHT);

        EntityPlayerSP player = mc.player;
        float armor = ForgeHooks.getTotalArmorValue(player) / 20f; // PERC

        float armorDurability = 0f;
        int armorpieces = player.inventory.armorInventory.size();
        for (int x = 0; x < armorpieces; x++) {
            ItemStack stack = player.inventory.armorInventory.get(x);
            if (stack.getItem() instanceof ISpecialArmor || stack.getItem() instanceof ItemArmor) {
                armorDurability += ((stack.getMaxDamage() - (float) stack.getItemDamage()) / (float) stack.getMaxDamage()) / armorpieces;
            }
        }

        mc.getTextureManager()
                .bindTexture(BARS);

        // Draw a blank small bar
        GlStateManager.enableBlend();
        Gui.drawModalRectWithCustomSizedTexture(left, top, 0, 253, SMALL_BAR_WIDTH, SMALL_BAR_HEIGHT, 256, 256);
        GlStateManager.disableBlend();
        // Draw the armor bar
        Gui.drawModalRectWithCustomSizedTexture(
                left,
                top,
                0,
                140,
                Math.round(SMALL_BAR_WIDTH * armor),
                SMALL_BAR_HEIGHT,
                256,
                256
        );
        // Draw the armor damage bar
        Gui.drawModalRectWithCustomSizedTexture(
                left,
                top,
                0,
                137,
                Math.round(SMALL_BAR_WIDTH * armorDurability),
                SMALL_BAR_HEIGHT,
                256,
                256
        );

        mc.getTextureManager()
                .bindTexture(Gui.ICONS);
    }

    @SubscribeEvent
    public void render(RenderGameOverlayEvent event) {
        if (!FROverlays.INSTANCE.shouldUseCustom()) return;
        Minecraft mc = FMLClientHandler.instance()
                .getClient();
        switch (event.getType()) {
            case ALL:
                setHeight(23);
                break;
            case HEALTH:
                setInCombat(false);
                if (GuiIngameForge.renderHealth) setInCombat(HealthGui(mc));
                event.setCanceled(true);
                break;
            case ARMOR:
                if (GuiIngameForge.renderArmor && isInCombat()) ArmorGui(mc);
                event.setCanceled(true);
                break;
            case FOOD:
                if (GuiIngameForge.renderFood) FoodGui(mc);
                event.setCanceled(true);
                break;
            case EXPERIENCE:
                if (GuiIngameForge.renderExperiance) {
                    XPGui(mc);
                    LevelGui(mc);
                }
                event.setCanceled(true);
                break;
            case AIR:
                if (GuiIngameForge.renderAir) AirGui(mc);
                event.setCanceled(true);
                break;
            case HEALTHMOUNT:
                if (GuiIngameForge.renderHealthMount) MountHealthGui(mc);
                event.setCanceled(true);
                break;
            case JUMPBAR:
                if (GuiIngameForge.renderJumpBar) JumpBarGui(mc);
                event.setCanceled(true);
                break;
            default:
                break;
        }
    }

    @SubscribeEvent
    public void levelUp(PlayerPickupXpEvent event) {
        if (!FROverlays.INSTANCE.shouldShowLevel()) return;
        if (FMLCommonHandler.instance()
                .getSide()
                .isClient()) {
            int level = event.getEntityPlayer().experienceLevel;
            event.getEntityPlayer()
                    .addExperience(event.getOrb().xpValue);
            event.getOrb()
                    .setDead();
            if (event.getEntityPlayer().experienceLevel != level) {
                event.getEntityPlayer()
                        .sendStatusMessage(new TextComponentString("You are now Level " + level), true);
            }
            event.setCanceled(true);
        }
    }

    OverlayOverriders() {
        super();
    }

    private static boolean isInCombat() {
        return inCombat;
    }

    private static void setInCombat(boolean value) {
        inCombat = value;
    }

    private static int getHeight() {
        return height;
    }

    private static void setHeight(int value) {
        height = value;
    }
}
