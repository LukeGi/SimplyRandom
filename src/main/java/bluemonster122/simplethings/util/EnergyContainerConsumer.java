package bluemonster122.simplethings.util;

public class EnergyContainerConsumer extends EnergyContainer
{
    public EnergyContainerConsumer(int capacity, int maxReceive)
    {
        super(capacity, maxReceive, 0);
    }

    public void consume(int i) {
        energy -= i;
    }
}
