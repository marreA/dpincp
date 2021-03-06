package solver;

import com.google.ortools.constraintsolver.*;

import java.util.ArrayList;


public class BinaryKnapsackCPandDPgoogle extends KnapsackSolver {
    static { System.loadLibrary("jniortools"); }

    Solver model;
    int count = 0;
    private ArrayList<IntVar> list = new ArrayList<>();
    private IntVar K[][] ;
    private boolean flag[][];
    public BinaryKnapsackCPandDPgoogle(int size) {
            super(size);
            this.name = "DP encoded in CP Google";
    }

    public void solve(int[] w, int[] c, int v) {
        model  = new Solver("CPScheduler");
        weight = w;
        cost = c;
        volume = v;
        K = new IntVar[size][volume +1];
        flag = new boolean[size][volume+1];
        //        long startTime = System.nanoTime();

        variableCreation(0,0);

        for (int i = 0; i < size -1; i++) {
            for (int j = 0; j < volume + 1; j++) {
                if (flag[i][j]) {
                    if (j + weight[i] > volume) {

                        //model.allEqual(K[i][j],K[i+1][j]).post();
                        model.addConstraint(model.makeEquality(K[i][j],K[i+1][j]));
                    } else {
                        model.addConstraint(model.makeEquality(K[i][j],
                                model.makeMax(K[i+1][j], model.makeSum(K[i+1][j+weight[i]], cost[i]))));

                    }
                }
            }
        }


        IntVar vars[] = new IntVar[list.size()];
        vars = list.toArray(vars);
        DecisionBuilder db = model.makePhase(vars, model.CHOOSE_RANDOM, model.ASSIGN_MIN_VALUE);

        SolutionCollector solCol = model.makeLastSolutionCollector();


        solCol.add(K[0][0]);
        SearchMonitor tl = model.makeTimeLimit(timeLimit * 1000);
        SearchMonitor[] sm = {solCol, tl};

        model.newSearch(db,sm);

        model.nextSolution();

//        System.out.println(K[0][0].value());

        if(K[0][0].bound()) {
            optimalValue = (int) K[0][0].value();
        } else {
            optimalValue = -1;
        }
        model.endSearch();
        model.delete();
        model = null;

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
                list.add(K[item][usedVolume]);
            } else {
                K[item][usedVolume] = model.makeIntConst(0, "C[" + item + "][" + usedVolume + "]");
                list.add(K[item][usedVolume]);
            }
            return;
        }

        K[item][usedVolume] = model.makeIntVar(0,50000, "C[" + item + "][" + usedVolume + "]");
        list.add(K[item][usedVolume]);
        variableCreation(item + 1, usedVolume);

        if (!(usedVolume + weight[item] > volume )) {
            variableCreation(item+1, usedVolume + weight[item]);
        }

    }


}
