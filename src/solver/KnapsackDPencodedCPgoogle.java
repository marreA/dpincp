package solver;

import com.google.ortools.constraintsolver.*;

import java.util.ArrayList;


public class KnapsackDPencodedCPgoogle extends KnapsackSolver {
    static { System.loadLibrary("jniortools"); }

    Solver model;
    private IntVar K[][] ;
    private ArrayList<IntVar> list = new ArrayList<>();
    public KnapsackDPencodedCPgoogle(int size) {
            super(size);
            this.name = "DP encoded in CP Google";
    }

    public void solve(int[] w, int[] c, int v) {
        model  = new Solver("CPScheduler");
        weight = w;
        cost = c;
        volume = v;
        K = new IntVar[size][volume +1];

        variableCreation(0,0);

        for (int i = 0; i < size -1; i++) {
            for (int j = 0; j < volume + 1; j++) {
                if (K[i][j] != null) {
                    ArrayList<IntVar> children = new ArrayList<>();
                    ArrayList<Integer> coeff = new ArrayList<>();
                    for (int k = minVal[i]; k <= Math.min((volume - j) / weight[i], maxVal[i]); k++) {
                        children.add(K[i+1][j + weight[i] * k]);
                        coeff.add(k);
                    }

                    if (children.size() == 1) {
                        model.addConstraint(model.makeEquality(K[i][j],
                                model.makeSum(children.get(0), coeff.get(0) * cost[i])));
                    } else {
                        IntVar vars[] = new IntVar[children.size()];
                        for (int k = 0; k < children.size(); k++) {
                            vars[k]= model.makeSum(children.get(k), coeff.get(k) * cost[i]).var();
                        }

                        model.addConstraint(model.makeEquality(K[i][j], model.makeMax(vars)));

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

        if(K[0][0].bound()) {
            optimalValue = (int) K[0][0].value();
        } else {
            optimalValue = -1;
        }
        model.endSearch();
        model.delete();


    }

    private void variableCreation(int item, int usedVolume) {
        if (item == size) {
            return;
        }
        if (K[item][usedVolume]!= null)
            return;

        if (item == size -1) {
            K[item][usedVolume] = model.makeIntConst(
                    cost[item] * Math.min((int) Math.floor((volume - usedVolume) / weight[item]), maxVal[item]),
                    "C[" + item + "][" + usedVolume + "]");
            list.add(K[item][usedVolume]);
            return;
        }


        K[item][usedVolume] = model.makeIntVar(0,50000, "C[" + item + "][" + usedVolume + "]");
        list.add(K[item][usedVolume]);
        for (int k = minVal[item]; k <= Math.min((volume - usedVolume) / weight[item], maxVal[item]); k++) {
            variableCreation(item + 1, usedVolume + weight[item] * k);
        }
    }


}
