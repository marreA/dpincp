package benchmark;


public abstract class InstanceGenerator {
    protected int weight[];
    protected int cost[];
    protected int volume;

    public int getVolume() {
        return volume;
    }



    public int[] getWeight() {
        return weight;
    }

    public int[] getCost() {
        return cost;
    }


    public abstract void generate(int n, int vol);

}
