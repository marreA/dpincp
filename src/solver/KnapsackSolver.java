package solver;

public abstract class KnapsackSolver {

    protected int size;
    protected int[] weight;
    protected int[] cost;
    protected int volume;

    public int[] getMinVal() {
        return minVal;
    }

    public void setMinVal(int[] minVal) {
        this.minVal = minVal;
    }

    public int[] getMaxVal() {
        return maxVal;
    }

    public void setMaxVal(int[] maxVal) {
        this.maxVal = maxVal;
    }

    protected int[] minVal;
    protected int[] maxVal;

    public void setSolverName(String solverName) {}

    protected int timeLimit;
    protected int optimalValue;
    protected int[] sol;
    protected String name;

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
        this.sol = new int[size];
    }

    public KnapsackSolver(int size) {
        this.size = size;
        this.sol = new int[size];
    }
    public KnapsackSolver() {
        this.size = 0;
        this.weight = null;
        this.cost = null;
        this.volume = 0;
    }

    public int[] getWeight() {
        return weight;
    }

    public void setWeight(int[] weight) {
        this.weight = weight;
    }

    public int[] getCost() {
        return cost;
    }

    public void setCost(int[] cost) {
        this.cost = cost;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public int getOptimalValue() {
        return optimalValue;
    }

    public abstract void solve(int[] weight, int[] cost, int volume);

    public String getName() {
        return name;
    }


    public int getTimeLimit() {
        return timeLimit;
    }

    public void setTimeLimit(int timeLimit) {
        this.timeLimit = timeLimit;
    }


}
