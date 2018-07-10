package solver;

import javafx.util.Pair;
import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solution;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.variables.IntVar;

import java.util.HashMap;
import java.util.Map;

public class KnapsackCPandDPv2 extends KnapsackSolver{

    private Model model =  new Model("DP encoded in CP");
    private Map<Pair<Integer, Integer>, IntVar> dictionary = new HashMap<Pair<Integer, Integer>, IntVar>();
    public KnapsackCPandDPv2(int size) {
            super(size);
            this.name = "DP encoded in CP";
    }

    public void solve(int[] w, int[] c, int v) {
        weight = w;
        cost = c;
        volume = v;
        variableCreation(0,0);
        System.out.println(" Dic Size " + dictionary.size());
        for (Map.Entry<Pair<Integer, Integer>, IntVar> e : dictionary.entrySet()) {
            Pair<Integer, Integer> p = e.getKey();
            int i = p.getKey();
            int k = p.getValue();
            if (i < size) {
                if (k + weight[i] > volume) {

                    IntVar var = dictionary.get(new Pair<>(i + 1, k));
                    model.allEqual(e.getValue(),var).post();
                } else {
                    IntVar varA = dictionary.get(new Pair<>(i + 1, k));
                    IntVar varB = dictionary.get(new Pair<>(i + 1, k + weight[i]));
                    e.getValue().eq(varA.max(varB.add(cost[i]))).post();
                }
            }

        }

        IntVar objective = dictionary.get(new Pair<>(0, 0));


        Solver solver = model.getSolver();
        Solution solution = solver.findSolution();
        if(solution != null){
            //System.out.println(solution.toString());
        }
        optimalValue = solution.getIntVal(objective);



    }

    private void variableCreation(int item, int usedVolume) {
        Pair<Integer, Integer> position = new Pair<>(item, usedVolume);
        if (item == size) {
            if (!dictionary.containsKey(position)) {
                IntVar v = model.intVar("C[" + item + "][" + usedVolume + "]", 0, 0);
                dictionary.put(position, v);
            }
            return;
        }
        if (dictionary.containsKey(position))
            return;
        IntVar v = model.intVar("C[" + item + "][" + usedVolume + "]", 0 , 30000);
        dictionary.put(position, v);
        variableCreation(item + 1, usedVolume);

        if (!(usedVolume + weight[item] > volume )) {
            variableCreation(item+1, usedVolume + weight[item]);
        }

    }


}
