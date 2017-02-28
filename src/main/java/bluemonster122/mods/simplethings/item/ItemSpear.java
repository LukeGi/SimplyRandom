package bluemonster122.mods.simplethings.item;

import bluemonster122.mods.simplethings.SimpleThings;
import com.google.common.collect.Multimap;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;
import java.util.stream.Collectors;

public class ItemSpear extends SimpleItemBase {
    private static final DamageSource SPEAR_DAMAGE = new DamageSource("spear");
    private float attackDamage;

    public ItemSpear(String name) {
        super(name);
        setMaxStackSize(1);
        setCreativeTab(SimpleThings.theTab);
    }

    public ItemSpear setMaterial(SpearMaterial mat) {
        setMaxDamage(mat.durability);
        attackDamage = 3.0F + mat.damage;
        return this;
    }

    public enum SpearMaterial {
        WOOD(ToolMaterial.WOOD),
        STONE(ToolMaterial.STONE),
        IRON(ToolMaterial.IRON),
        GOLD(ToolMaterial.GOLD),
        DIAMOND(ToolMaterial.DIAMOND);
        public final int durability;
        public final float damage;

        SpearMaterial(ToolMaterial material) {
            durability = material.getMaxUses();
            damage = material.getDamageVsEntity();
        }
    }

    @Override
    public boolean onEntitySwing(EntityLivingBase entityLiving, ItemStack stack) {
        List<EntityLivingBase> entities = entityLiving.world.getEntitiesWithinAABBExcludingEntity(
                entityLiving, entityLiving.getEntityBoundingBox().expandXyz(20)).stream().filter(
                entity -> entity instanceof EntityLivingBase).map(entity -> (EntityLivingBase) entity).filter(
                EntityLivingBase::canBeCollidedWith).collect(Collectors.toList());
        for (EntityLivingBase entity : entities) {
            Vec3d vec3d = entityLiving.getLook(1.0F).normalize();
            Vec3d vec3d1 = new Vec3d(entity.posX - entityLiving.posX,
                    entity.getEntityBoundingBox().minY + (double) entity.getEyeHeight() - (entityLiving.posY + (double) entityLiving.getEyeHeight()),
                    entity.posZ - entityLiving.posZ
            );
            double d0 = vec3d1.lengthVector();
            vec3d1 = vec3d1.normalize();
            double d1 = vec3d.dotProduct(vec3d1);
            if (d1 > 1.0D - 0.025D / d0 && entity.getPosition().distanceSq(entityLiving.getPosition()) < 100) {
                hitEntity(stack, entity, entityLiving);
            }
        }
        return false;
    }

    @Override
    public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
        if (attacker.getHeldItem(EnumHand.MAIN_HAND).getItem() instanceof ItemSpear && attacker.getHeldItem(
                EnumHand.OFF_HAND).getItem() instanceof ItemSpear) {
            for (EnumHand hand : EnumHand.values()) {
                spearAttack(attacker, target, attacker.getHeldItem(hand));
            }
        } else {
            spearAttack(attacker, target, stack);
        }
        return true;
    }

    private void spearAttack(EntityLivingBase attacker, EntityLivingBase target, ItemStack stack) {
        stack.damageItem(1, attacker);
        target.attackEntityFrom(SPEAR_DAMAGE, attackDamage);
    }

    @Override
    public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer player, EntityLivingBase hit, EnumHand hand) {
        if (hit.isDead) {
            return false;
        }
        if (player.getHeldItem(EnumHand.MAIN_HAND).getItem() instanceof ItemSpear && player.getHeldItem(
                EnumHand.OFF_HAND).getItem() instanceof ItemSpear) {
            for (EnumHand enumHand : EnumHand.values()) {
                ItemStack heldItem = player.getHeldItem(enumHand);
                player.setHeldItem(enumHand, ItemStack.EMPTY);
                spearAttack(player, hit, heldItem);
                heldItem.damageItem(9, player);
                if (heldItem != ItemStack.EMPTY && !hit.getEntityWorld().isRemote) {
                    EntityItem spear = new EntityItem(
                            hit.getEntityWorld(), hit.posX, hit.posY + (hit.getEyeHeight() * 3 / 4), hit.posZ, heldItem);
                    spear.motionX = spear.motionY = spear.motionZ = 0;
                    hit.getEntityWorld().spawnEntity(spear);
                }
            }
        }
        return true;
    }

    @SideOnly(Side.CLIENT)
    public boolean isFull3D() {
        return true;
    }

    public Multimap<String, AttributeModifier> getAttributeModifiers(EntityEquipmentSlot slot, ItemStack stack) {
        Multimap<String, AttributeModifier> multimap = super.getAttributeModifiers(slot, stack);
        if (slot == EntityEquipmentSlot.MAINHAND) {
            multimap.put(SharedMonsterAttributes.ATTACK_DAMAGE.getName(),
                    new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Weapon modifier", (double) this.attackDamage, 0)
            );
            multimap.put(SharedMonsterAttributes.ATTACK_SPEED.getName(),
                    new AttributeModifier(ATTACK_SPEED_MODIFIER, "Weapon modifier", 2, 0)
            );
        }
        return multimap;
    }


}
