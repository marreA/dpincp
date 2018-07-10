package solver;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solution;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.exception.ContradictionException;
import org.chocosolver.solver.variables.IntVar;


public class KnapsackCPandDP extends KnapsackSolver{

    private Model model =  new Model("DP encoded in CP no dictionary");
    int count = 0;
    private IntVar K[][];
    private boolean flag[][];
    public KnapsackCPandDP(int size) {
            super(size);
            this.name = "DP encoded in CP no dictionary";
    }

    public void solve(int[] w, int[] c, int v) {
        weight = w;
        cost = c;
        volume = v;
        K = new IntVar[size][volume +1];
        flag = new boolean[size][volume+1];
        //        long startTime = System.nanoTime();

        variableCreation(0,0);
//        long estimatedTime = System.nanoTime() - startTime;
//        System.out.println("Time: " + estimatedTime  / 1000000 +"\n");

        for (int i = 0; i < size -1; i++) {
            for (int j = 0; j < volume + 1; j++) {
                if (flag[i][j]) {
                    if (j + weight[i] > volume) {

                        //model.allEqual(K[i][j],K[i+1][j]).post();
                        K[i][j].eq(K[i+1][j]).post();
                    } else {
                        K[i][j].eq(K[i+1][j].max(K[i+1][j+weight[i]].add(cost[i]))).post();
                    }
                }
            }
        }



//        estimatedTime = System.nanoTime() - startTime;
//        System.out.println("Time: " + estimatedTime  / 1000000 +"\n");
        IntVar objective = K[0][0];


        Solver solver = model.getSolver();
        try  {
            solver.propagate();
        } catch (ContradictionException e) {
            e.printStackTrace();
        }
//        System.out.println(objective.getValue());

        Solution solution = solver.findSolution();
        if(solution != null){
            //System.out.println(solution.toString());
        }
        optimalValue = objective.getValue();
//        System.out.println("COunt " +count);
//        estimatedTime = System.nanoTime() - startTime;
//        System.out.println("Time: " + estimatedTime  / 1000000 +"\n");

    }

    private void variableCreation(int item, int usedVolume) {
        if (flag[item][usedVolume])
            return;
        if (item == size) {
            return;
        }
        flag[item][usedVolume] = true;
        if (item == size -1) {
            if (volume - usedVolume > weight[item]) {
                K[item][usedVolume] = model.intVar("C[" + item + "][" + usedVolume + "]", cost[item] , cost[item]);
                count++;
            } else {
                K[item][usedVolume] = model.intVar("C[" + item + "][" + usedVolume + "]", 0 , 0);
                count++;
            }
            return;
        }

        K[item][usedVolume] = model.intVar("C[" + item + "][" + usedVolume + "]", 0 , 30000);
        count++;
        variableCreation(item + 1, usedVolume);

        if (!(usedVolume + weight[item] > volume )) {
            variableCreation(item+1, usedVolume + weight[item]);
        }

    }


}
