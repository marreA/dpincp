package solver;

import org.chocosolver.solver.*;
import org.chocosolver.solver.variables.*;
import java.util.stream.IntStream;

public class KnapsackConstraint extends KnapsackSolver{


    public KnapsackConstraint(int size){
        super(size);
        this.name = "Choco constraint";
    }
    public void solve(int[] w, int[] c, int v) {
        weight = w;
        cost = c;
        volume = v;

        Model model = new Model("A simple knapsack constraint");

        IntVar objective = model.intVar("Objective", 0 , 30000);
        IntVar capacity = model.intVar("Capacity", volume , volume);

        IntVar[] occurences = IntStream
                .range(0,size)
                .mapToObj(i -> model.intVar("X_" +i, 0,1))
                .toArray(IntVar[]::new);


        model.knapsack(occurences, capacity, objective, weight, cost).post();

        Solver solver = model.getSolver();
        Solution solution = solver.findOptimalSolution(objective, true);
//        if(solution != null){
//            System.out.println(solution.toString());
//        }
        optimalValue = objective.getValue();

        for (int j = 0; j < size; j++) {
            sol[j] = occurences[j].getValue();
        }
    }

}
