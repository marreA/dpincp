package solver;

import javafx.util.Pair;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

// Developed with dictionary, useful to compare the dimension problem
public class KnapsackDPv2dictionary extends KnapsackSolver{
    private Map<Pair<Integer, Integer>, Integer> dictionary = new HashMap<Pair<Integer, Integer>, Integer>();
    public KnapsackDPv2dictionary(int size){
        super(size);
        this.name = "DP with dictionary";
    }
    public void solve(int[] w, int[] c, int v) {
        weight = w;
        cost = c;
        volume = v;

        int K[][] = new int[size+1][volume+1];
        // Fill each row with 1.0
        for (int[] row: K)
            Arrays.fill(row, -1);
        optimalValue = recursiveCost(0,0);

    }

    private int recursiveCost(int item, int usedVolume) {
        Pair<Integer, Integer> position = new Pair<>(item, usedVolume);

        if (item == size)
            return 0;
        if (dictionary.containsKey(position))
            return dictionary.get(position);

        int result = 0;
        if (usedVolume + weight[item] > volume )  {
            result = recursiveCost(++item, usedVolume);

        } else {
            result = Math.max(recursiveCost(item + 1, usedVolume), recursiveCost(item + 1, usedVolume + weight[item]) + cost[item]);
        }

        dictionary.put(position, result);
        return result;
    }

}
