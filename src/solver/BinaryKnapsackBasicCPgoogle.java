package solver;

import com.google.ortools.constraintsolver.*;

import java.util.stream.IntStream;

public class BinaryKnapsackBasicCPgoogle extends KnapsackSolver {
    static { System.loadLibrary("jniortools"); }

    public BinaryKnapsackBasicCPgoogle(int size){
        super(size);
        this.name = "Basic CP with google ";
    }
    public void solve(int[] w, int[] c, int v) {
        weight = w;
        cost = c;
        volume = v;

        Solver model = new Solver("CPScheduler");

        IntVar o = model.makeIntVar(0,99999,"Objective");

        IntVar[] occurences = IntStream
                .range(0,size)
                .mapToObj(i -> model.makeIntVar(0,1,"X[" + i +"]" ))
                .toArray(IntVar[]::new);


        IntVar vars[] = new IntVar[size+1];
        for (int i = 0; i < size; i++) {
            vars[i] = occurences[i];
        }
        vars[size] = o;


        model.addConstraint(model.makeScalProdLessOrEqual(occurences, weight,volume));
        model.addConstraint(model.makeScalProdEquality(occurences,cost,o));
        OptimizeVar obj = model.makeMaximize(o, 1);

        DecisionBuilder db = model.makePhase(vars, model.INT_VAR_DEFAULT, model.INT_VALUE_DEFAULT);

        SolutionCollector solCol = model.makeLastSolutionCollector();


        solCol.add(vars);

        SearchMonitor tl = model.makeTimeLimit(timeLimit * 1000);
        SearchMonitor[] sm = {obj,solCol, tl};


        model.solve(db,sm);
//        System.out.println(model.solutions());

        Assignment bestSol = solCol.solution(0);

//        System.out.println(bestSol.value(o));

        optimalValue = (int) bestSol.value(o);

        model.endSearch();
        model.delete();

    }

}
