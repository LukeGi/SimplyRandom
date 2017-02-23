package bluemonster122.simplethings.tileentity.core;

public interface IAcceptPower extends IHaveBattery
{
	default int recievePower(int toRecieve, boolean fake)
	{
		return getBattery().receiveEnergy(toRecieve, fake);
	}
}
