package solver;

import javafx.util.Pair;

import java.util.HashMap;
import java.util.Map;

public class BinaryKnapsackDP extends KnapsackSolver {

    private Map<Pair<Integer, Integer>, Integer> dictionary = new HashMap<Pair<Integer, Integer>, Integer>();
    public BinaryKnapsackDP(int size){
        super(size);
        this.name = "standard DP";
    }
    public void solve(int[] w, int[] c, int v) {
        weight = w;
        cost = c;
        volume = v;
//        System.out.println(w.length);
//        System.out.println(c.length);
//        System.out.println(v);
        optimalValue = knapSack();

    }

    private int knapSack()
    {
        int i, w;
        int K[][] = new int[size+1][volume+1];

        // Build table K[][] in bottom up manner
        for (i = 0; i <= size; i++)
        {
            for (w = 0; w <= volume; w++)
            {
                if (i==0 || w==0)
                    K[i][w] = 0;
                else if (weight[i-1] <= w)
                    K[i][w] = Math.max(cost[i-1] + K[i-1][w-weight[i-1]],  K[i-1][w]);
                else
                    K[i][w] = K[i-1][w];
            }
        }

        return K[size][volume];
    }
//    private int recursiveCost(int item, int usedVolume) {
//        if (item >= size)
//            return 0;
//        Pair<Integer, Integer> position = new Pair<>(item, usedVolume);
//        if (dictionary.containsKey(position))
//            return dictionary.get(position);
//
//        if (usedVolume + weight[item] > volume )
//            return recursiveCost(++item, volume);
//
//        int result = Math.max(recursiveCost(++item, volume), recursiveCost(++item, volume + weight[item]) + cost[item] );
//        dictionary.put(position, result);
//        System.out.println(position.toString() + "  " + result);
//
//        return result;
//    }

}
