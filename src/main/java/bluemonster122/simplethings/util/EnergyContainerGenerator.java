package bluemonster122.simplethings.util;

public class EnergyContainerGenerator extends EnergyContainer
{
    public EnergyContainerGenerator(int capacity, int maxExtract)
    {
        super(capacity, 0, maxExtract);
    }

    public void generate(int i)
    {
        this.energy += i;
    }
}
