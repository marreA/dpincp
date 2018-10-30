package solver;

import com.google.ortools.linearsolver.MPConstraint;
import com.google.ortools.linearsolver.MPObjective;
import com.google.ortools.linearsolver.MPSolver;
import com.google.ortools.linearsolver.MPVariable;

public class KnapsackBasicMIP extends KnapsackSolver {

    static { System.loadLibrary("jniortools");}
    String MIPSolver = "CBC_MIXED_INTEGER_PROGRAMMING";

    public KnapsackBasicMIP(int size) {
        super(size);
        this.name = "basic MIP";
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
        MPSolver solver = new MPSolver("MIPSolver", MPSolver.OptimizationProblemType.valueOf(MIPSolver));

        MPVariable[] occurences = new MPVariable[size];
        MPObjective objective = solver.objective();
        for (int j = 0; j < size; j++) {
            occurences[j] = solver.makeIntVar(minVal[j], maxVal[j],"X_" + j);
        }
        MPConstraint constraint = solver.makeConstraint(0, volume);
        for (int j = 0; j < size; j++) {
            constraint.setCoefficient(occurences[j], weight[j]);
            objective.setCoefficient(occurences[j], cost[j]);
        }
        objective.setMaximization();

        solver.setTimeLimit(timeLimit * 1000);
        final MPSolver.ResultStatus resultStatus = solver.solve();


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
        solver.delete();


    }

}
