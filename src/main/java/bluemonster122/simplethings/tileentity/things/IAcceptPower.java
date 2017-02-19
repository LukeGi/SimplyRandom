package bluemonster122.simplethings.tileentity.things;

public interface IAcceptPower extends IHaveBattery
{
    default int recievePower(int toRecieve, boolean fake)
    {
        return getBattery().receiveEnergy(toRecieve, fake);
    }
}
