package solver;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solution;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.variables.IntVar;

import java.util.stream.IntStream;

public class KnapsackBasicCP extends KnapsackSolver{

    public KnapsackBasicCP(int size){
        super(size);
        this.name = "Basic CP";
    }
    public void solve(int[] w, int[] c, int v) {
        weight = w;
        cost = c;
        volume = v;

        Model model = new Model("Implementation through scalar product");

        IntVar objective = model.intVar("Objective", 0 , 99999);
        IntVar[] occurences = IntStream
                .range(0,size)
                .mapToObj(i -> model.intVar("X_" +i, 0,1))
                .toArray(IntVar[]::new);


        model.scalar(occurences, weight, "<=", volume).post();
        model.scalar(occurences, cost, "=", objective).post();

        Solver solver = model.getSolver();
        Solution solution = solver.findOptimalSolution(objective, true);
//        if(solution != null){
//            System.out.println(solution.toString());
//        }
        optimalValue = solution.getIntVal(objective);
    }

}
