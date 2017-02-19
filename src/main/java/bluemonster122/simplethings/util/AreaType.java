package bluemonster122.simplethings.util;

import net.minecraft.util.math.BlockPos;

import java.util.Set;
public enum AreaType
{
	SMALL, MEDIUM, LARGE, XL, XXL;
	public static final AreaType[] VALUES = new AreaType[]{SMALL, MEDIUM, LARGE, XL, XXL};
	
	public static int getIndex(AreaType type)
	{
		switch (type)
		{
			case SMALL:
				return 0;
			case MEDIUM:
				return 1;
			case LARGE:
				return 2;
			case XL:
				return 3;
			case XXL:
				return 4;
			default:
				return 0;
		}
	}
	
	public static AreaType getFromIndex(int index)
	{
		return VALUES[index];
	}
	
	public Set<BlockPos> getArea()
	{
		return null;
	}
}
