package benchmark;


public abstract class InstanceGenerator {
    protected int weight[];
    protected int cost[];
    protected int volume;
    protected int[] minVal;
    protected int[] maxVal;

    public boolean isBinary() {
        return binary;
    }

    public void setBinary(boolean binary) {
        this.binary = binary;
    }

    protected boolean binary = false;

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    protected int size;

    public int[] getMinVal() {
        return minVal;
    }

    public int[] getMaxVal() {
        return maxVal;
    }


    public int getVolume() {
        return volume;
    }



    public int[] getWeight() {
        return weight;
    }

    public int[] getCost() {
        return cost;
    }


    public abstract void generate(int n, int vol, int seed);

}
