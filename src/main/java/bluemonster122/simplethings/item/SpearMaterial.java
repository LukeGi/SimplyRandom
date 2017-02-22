package bluemonster122.simplethings.item;

import net.minecraft.item.Item;
public enum SpearMaterial
{
	WOOD(Item.ToolMaterial.WOOD), STONE(Item.ToolMaterial.STONE), IRON(Item.ToolMaterial.IRON), GOLD(
  Item.ToolMaterial.GOLD), DIAMOND(Item.ToolMaterial.DIAMOND);
	public final int durability;
	public final float damage;
	
	SpearMaterial(Item.ToolMaterial material)
	{
		durability = material.getMaxUses();
		damage = material.getDamageVsEntity();
	}
}
