package bluemonster122.simplethings.tileentity.things;

public interface IProvidePower extends IHaveBattery
{
	/**
	 * Forward method to internal battery.
	 */
	default int extractPower(int amount, boolean fake)
	{
		return getBattery().extractEnergy(amount, fake);
	}
}
