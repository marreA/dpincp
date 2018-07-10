package solver;

import com.google.ortools.linearsolver.*;

public class KnapsackBasicMIP extends KnapsackSolver{

    static { System.loadLibrary("jniortools");}

    public KnapsackBasicMIP(int size) {
        super(size);
        this.name = "basic MIP";
    }

    public void solve(int[] w, int[] c, int v) {
        weight = w;
        cost = c;
        volume = v;
        MPSolver solver = new MPSolver("MIPSolver", MPSolver.OptimizationProblemType.valueOf("CBC_MIXED_INTEGER_PROGRAMMING"));

        MPVariable[] occurences = new MPVariable[size];
        MPObjective objective = solver.objective();
        for (int j = 0; j < size; j++) {
            occurences[j] = solver.makeBoolVar("X_" + j);
        }
        MPConstraint constraint = solver.makeConstraint(0, volume);
        for (int j = 0; j < size; j++) {
            constraint.setCoefficient(occurences[j], weight[j]);
            objective.setCoefficient(occurences[j], cost[j]);
        }
        objective.setMaximization();
        final MPSolver.ResultStatus resultStatus = solver.solve();
        optimalValue = (int) solver.objective().value();

//        System.out.println("Iterations value = " + solver.iterations());
//        System.out.println("Contrints value = " + solver.numConstraints());
//        System.out.println("Var value = " + solver.numVariables());
//        for (int j = 0; j < size; j++) {
//            sol[j] = (int) occurences[j].solutionValue();
//        }
        solver.delete();


    }

}
