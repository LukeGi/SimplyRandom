package bluemonster122.simplethings.item;

import bluemonster122.simplethings.SimpleThings;
import com.google.common.collect.Multimap;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
public class ItemSpear extends Item
{
	private final float attackDamage;
	
	public ItemSpear(SpearMaterial mat, String name)
	{
		super();
		setMaxStackSize(1);
		setMaxDamage(mat.durability);
		setRegistryName(SimpleThings.MOD_ID, name);
		setUnlocalizedName(getRegistryName().toString());
		setCreativeTab(SimpleThings.theTab);
		this.attackDamage = 3.0F + mat.damage;
	}
	
	@Override
	public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker)
	{
		stack.damageItem(1, attacker);
		target.attackEntityFrom(new DamageSource("spear"), attackDamage);
		return true;
	}
	
	@Override
	public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer player, EntityLivingBase hit, EnumHand hand)
	{
		if (hit.isDead)
		{
			return false;
		}
		ItemStack heldItem = player.getHeldItem(hand);
		player.setHeldItem(hand, ItemStack.field_190927_a);
		hitEntity(heldItem, hit, player);
		heldItem.damageItem(9, player);
		if (heldItem != ItemStack.field_190927_a && !hit.getEntityWorld().isRemote)
		{
			EntityItem spear = new EntityItem(
			  hit.getEntityWorld(), hit.posX, hit.posY + (hit.getEyeHeight() * 3 / 4), hit.posZ, heldItem);
			spear.setVelocity(0, 0, 0);
			hit.getEntityWorld().spawnEntityInWorld(spear);
		}
		return true;
	}
	
	@SideOnly(Side.CLIENT)
	public boolean isFull3D()
	{
		return true;
	}
	
	public Multimap<String, AttributeModifier> getAttributeModifiers(EntityEquipmentSlot slot, ItemStack stack)
	{
		Multimap<String, AttributeModifier> multimap = super.getItemAttributeModifiers(slot);
		if (slot == EntityEquipmentSlot.MAINHAND)
		{
			multimap.put(SharedMonsterAttributes.ATTACK_DAMAGE.getAttributeUnlocalizedName(),
			             new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Weapon modifier", (double) this.attackDamage, 0)
			);
			multimap.put(SharedMonsterAttributes.ATTACK_SPEED.getAttributeUnlocalizedName(),
			             new AttributeModifier(ATTACK_SPEED_MODIFIER, "Weapon modifier", 2, 0)
			);
		}
		return multimap;
	}
}
