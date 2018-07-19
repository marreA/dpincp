package solver;

import com.google.ortools.linearsolver.MPConstraint;
import com.google.ortools.linearsolver.MPObjective;
import com.google.ortools.linearsolver.MPSolver;
import com.google.ortools.linearsolver.MPVariable;

public class BinaryKnapsackDPencodedMIP extends KnapsackSolver {

    static { System.loadLibrary("jniortools");}

    MPSolver solver;

    boolean flags[][][];
    MPVariable vars[][][];
    public BinaryKnapsackDPencodedMIP(int size) {
        super(size);
        this.name = "DP encoded MIP";
    }

    public void solve(int[] w, int[] c, int v) {
        weight = w;
        cost = c;
        volume = v;

        solver = new MPSolver("MIPSolver", MPSolver.OptimizationProblemType.valueOf("CBC_MIXED_INTEGER_PROGRAMMING"));
        vars = new MPVariable[size][volume+1][2];

        variableCreation(0,0);


        MPObjective objective = solver.objective();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j <= volume; j++) {
                if (vars[i][j][1] != null)
                    objective.setCoefficient(vars[i][j][1],cost[i]);
            }
        }
        MPConstraint source = solver.makeConstraint(1,1);
        source.setCoefficient(vars[0][0][0], 1);
        source.setCoefficient(vars[0][0][1], 1);
        for (int i = 1; i < size; i++) {
            for (int j = 0; j < volume; j++) {
                if (vars[i][j][0] != null) {
                    MPConstraint cstr = solver.makeConstraint(0,0);
                    cstr.setCoefficient(vars[i][j][0],1);
                    if (vars[i-1][j][0] != null)
                        cstr.setCoefficient(vars[i-1][j][0],-1);

                    if (j - weight[i-1] >= 0)
                        cstr.setCoefficient(vars[i-1][j - weight[i-1]][1],-1);

                    if (vars[i][j][1] != null)
                        cstr.setCoefficient(vars[i][j][1],1);

                }

            }
        }
        objective.setMaximization();
        solver.setTimeLimit(timeLimit * 1000);

        if (solver.solve() == MPSolver.ResultStatus.OPTIMAL) {
            optimalValue = (int) solver.objective().value();
        } else {
            optimalValue = -1;
        }

//        System.out.println("Iterations value = " + binarysolver.iterations());
//        System.out.println("Contrints value = " + binarysolver.numConstraints());
//        System.out.println("Var value = " + binarysolver.numVariables());
//        for (int j = 0; j < size; j++) {
//            sol[j] = (int) occurences[j].solutionValue();
//        }
//        for (int i = 0; i < size; i++) {
//            for (int j = 0; j < volume; j++) {
//                if (vars[i][j][0] != null) {
//                    System.out.println(vars[i][j][0].name() +" " + (int) vars[i][j][0].solutionValue());
//                    if (vars[i][j][1] != null)
//                        System.out.println(vars[i][j][1].name() +" " + (int) vars[i][j][1].solutionValue());
//
//                }
//
//            }
//        }
        solver.delete();



    }

    private void variableCreation(int item, int usedVolume) {
        if (item == size) {
            return;
        }
        if (vars[item][usedVolume][0] != null)
            return;

//        if (item == size -1) {
//            if (volume - usedVolume > weight[item]) {
//                K[item][usedVolume] = model.makeIntConst(cost[item], "C[" + item + "][" + usedVolume + "]");
//                count++;
//                list.add(K[item][usedVolume]);
//            } else {
//                K[item][usedVolume] = model.makeIntConst(0, "C[" + item + "][" + usedVolume + "]");
//                count++;
//                list.add(K[item][usedVolume]);
//            }
//            return;
//        }

        vars[item][usedVolume][0] = solver.makeBoolVar("var[" + item + "]["+ usedVolume + "][0]");

        variableCreation(item + 1, usedVolume);

        if (!(usedVolume + weight[item] > volume )) {
            vars[item][usedVolume][1] = solver.makeBoolVar("var[" + item + "]["+ usedVolume + "][1]");
            variableCreation(item+1, usedVolume + weight[item]);
        }

    }



}
