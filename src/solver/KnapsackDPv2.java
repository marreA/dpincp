package solver;

import javafx.util.Pair;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

// Developed with dictionary, useful to compare the dimension problem
public class KnapsackDPv2 extends KnapsackSolver{

    private Map<Pair<Integer, Integer>, Integer> dictionary = new HashMap<Pair<Integer, Integer>, Integer>();
    public KnapsackDPv2(int size) {
        super(size);
        this.name = "DPv2";
    }
    public void solve(int[] w, int[] c, int v) {
        weight = w;
        cost = c;
        volume = v;

        int K[][] = new int[size+1][volume+1];
        // Fill each row with 1.0
        for (int[] row: K)
            Arrays.fill(row, -1);
        optimalValue = recursiveCost(0,0, K);

    }

    private int recursiveCost(int item, int usedVolume, int[][] K) {

        if (item == size)
            return 0;
        if (K[item][usedVolume] != -1 )
            return K[item][usedVolume];

        if (usedVolume + weight[item] > volume )  {
            K[item][usedVolume] = recursiveCost(++item, usedVolume, K);
            return K[item][usedVolume];
        }
        K[item][usedVolume] = Math.max(recursiveCost(item + 1, usedVolume, K),recursiveCost(item + 1, usedVolume + weight[item], K) + cost[item]);

        return K[item][usedVolume];
    }

}
