package solver;

import com.google.ortools.constraintsolver.DecisionBuilder;
import com.google.ortools.constraintsolver.IntVar;
import com.google.ortools.constraintsolver.SolutionCollector;
import com.google.ortools.constraintsolver.Solver;

import java.util.ArrayList;


public class KnapsackCPandDPgooglev2 extends KnapsackSolver{
    static { System.loadLibrary("jniortools"); }

    Solver model = new Solver("CPScheduler");
    int count = 0;
    private ArrayList<IntVar> list = new ArrayList<>();
    private IntVar K[][];
    private int fill[] = new int[size];
    private boolean flag[][];
    public KnapsackCPandDPgooglev2(int size) {
            super(size);
            this.name = "DP encoded in CP Google with added node removal";
    }

    public void solve(int[] w, int[] c, int v) {
        weight = w;
        cost = c;
        volume = v;

        K = new IntVar[size][volume +1];
        flag = new boolean[size][volume+1];
        //        long startTime = System.nanoTime();

//        long estimatedTime = System.nanoTime() - startTime;
//        System.out.println("Time: " + estimatedTime  / 1000000 +"\n");
        fill[size-1] = Math.max(volume - weight[size - 1], 0);
        for (int i = size -2; i >= 0; i--)
        {
            fill[i] = Math.max(fill[i+1] - weight[i],0);
        }

        variableCreation(0,0);


        for (int i = 0; i < size -1; i++) {
            for (int j = 0; j < volume + 1; j++) {
                if (flag[i][j]) {
                    if (j + weight[i] > volume) {

                        //model.allEqual(K[i][j],K[i+1][j]).post();
                        model.addConstraint(model.makeEquality(K[i][j],K[i+1][j]));
                    } else {
                        if (j >= fill[i]) {
                            model.addConstraint(model.makeEquality(K[i][j],
                                    model.makeMax(K[i + 1][j], model.makeSum(K[i + 1][j + weight[i]], cost[i]))));
                        } else {
                            model.addConstraint(model.makeEquality(K[i][j],model.makeSum(K[i + 1][j + weight[i]], cost[i])));
                        }


                    }
                }
            }
        }

        IntVar vars[] = new IntVar[list.size()];
        vars = list.toArray(vars);
        DecisionBuilder db = model.makePhase(vars, model.CHOOSE_RANDOM, model.ASSIGN_MIN_VALUE);

        SolutionCollector solCol = model.makeLastSolutionCollector();


        solCol.add(K[0][0]);


        model.newSearch(db,solCol);
        model.nextSolution();

//        System.out.println(K[0][0].value());

        optimalValue = (int) K[0][0].value();
        model.endSearch();

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
                K[item][usedVolume] = model.makeIntConst(cost[item], "C[" + item + "][" + usedVolume + "]");
                count++;
                list.add(K[item][usedVolume]);
            } else {
                K[item][usedVolume] = model.makeIntConst(0, "C[" + item + "][" + usedVolume + "]");
                count++;
                list.add(K[item][usedVolume]);
            }
            return;
        }

        K[item][usedVolume] = model.makeIntVar(0,50000, "C[" + item + "][" + usedVolume + "]");
        count++;
        list.add(K[item][usedVolume]);
        if (usedVolume >= fill[item] ) {
            variableCreation(item + 1, usedVolume);
        }

        if (!(usedVolume + weight[item] > volume )) {
            variableCreation(item+1, usedVolume + weight[item]);
        }

    }


}
