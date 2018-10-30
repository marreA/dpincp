package solver;

import com.google.ortools.linearsolver.MPConstraint;
import com.google.ortools.linearsolver.MPObjective;
import com.google.ortools.linearsolver.MPSolver;
import com.google.ortools.linearsolver.MPVariable;

import java.util.Arrays;


public class KnapsackDPencodedMIP extends KnapsackSolver {

    static { System.loadLibrary("jniortools");}
    String MIPSolver = "CBC_MIXED_INTEGER_PROGRAMMING";

    MPSolver solver;


    MPVariable vars[][][];
    public KnapsackDPencodedMIP(int size) {
        super(size);
        this.name = "DP encoded MIP";
    }
    public void setSolverName(String solverName)
    {
        this.MIPSolver = solverName;
        this.name = "MIP " + solverName;
    }

    public void solve(int[] w, int[] c, int v) {
        weight = w;
        cost = c;
        volume = v;
        int maxK = Arrays.stream(maxVal).max().getAsInt();


        MPSolver solver = new MPSolver("MIPSolver", MPSolver.OptimizationProblemType.valueOf(MIPSolver));
        vars = new MPVariable[size][volume+1][maxK+1];

        variableCreation(0,0);


        MPObjective objective = solver.objective();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j <= volume; j++) {
                for (int k = minVal[i]; k <= maxVal[i]; k++) {
                    if (vars[i][j][k] != null) {
                        objective.setCoefficient(vars[i][j][k], k * cost[i]);
                    }
                }
            }
        }
        MPConstraint source = solver.makeConstraint(1,1);
        for (int i = minVal[0]; i <= maxVal[0]; i++) {
            source.setCoefficient(vars[0][0][i], 1);
        }

        for (int i = 1; i < size; i++) {
            for (int j = 0; j < volume; j++) {
                if (vars[i][j][minVal[i]] != null) {
                    MPConstraint cstr = solver.makeConstraint(0,0);
                    for (int k = minVal[i]; k <= maxVal[i]; k++) {
                        if (j + weight[i] * k <= volume) {
                            cstr.setCoefficient(vars[i][j][k],1);
                        }
                    }
                    for (int k = minVal[i-1]; k <= maxVal[i-1]; k++) {
                        if ((j - weight[i-1] * k >= 0) && (vars[i-1][j - weight[i-1] * k][k] != null)) {
                            cstr.setCoefficient(vars[i-1][j - weight[i-1] * k][k],-1);
                        }
                    }

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
        if (vars[item][usedVolume][minVal[item]] != null)
            return;

        for (int i = minVal[item]; i <= maxVal[item]; i++) {
            if (usedVolume + weight[item] * i <= volume) {
                vars[item][usedVolume][i] = solver.makeBoolVar("var[" + item + "][" + usedVolume + "][ " + i + "]");
                variableCreation(item + 1, usedVolume + weight[item] * i);
            }
        }

    }



}
