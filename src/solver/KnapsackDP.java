package solver;

import javafx.util.Pair;
import java.util.HashMap;
import java.util.Map;

public class KnapsackDP extends KnapsackSolver {

    private Map<Pair<Integer, Integer>, Integer> dictionary = new HashMap<Pair<Integer, Integer>, Integer>();
    public KnapsackDP(int size){
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

        int i, j;
        int K[][] = new int[size+1][volume+1];
        for (i = 0; i <= size; i++) {
            K[i][0] = 0;
        }
        for (j = 1; j <= volume; j++) {
            K[0][j] = 0;
        }
        // Build table K[][] in bottom up manner
        for (i = 1; i <= size; i++)
        {
            for (j = 1; j <= volume; j++)
            {
                int currMax = 0;

                for (int k = minVal[i-1]; k <= Math.min(j / weight[i-1], maxVal[i-1]); k++) {
                    currMax = Math.max(currMax, K[i - 1][j - weight[i - 1] * k] + (cost[i - 1] * k));
                }
                K[i][j] = currMax;
            }
        }
        optimalValue = K[size][volume];

    }



}
