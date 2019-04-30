import benchmark.*;
import solver.*;

import java.io.*;
import java.util.Arrays;


public class main {

    //Constants
    public static void main(String[] args) throws IOException {


        /*
        String values = "binary weaklycorrelated 175 50 100 10 600 binDP binDPv2 binCPchoco binMIP binMIPCPLEX binMIPGUROBI binConstraintChoco binDPinCPgoogle binDPinCPgooglev2 binDPinCPgooglev3 binDPinMIPCPLEX";
        values = "normal subsetsum 5 50 100 10 600 DP CPgoogle MIP MIPCPLEX ConstraintChoco DPinCPgoogle DPinMIP DPinMIPCPLEX DPinMIPGUROBI";
        args = values.split(" ");
        /* Optimisation type: posible values "normal" or "binary"
         * Type of instances: "uncorrelated", "weaklycorrelated", "stronglycorrelated", "subsetsum"
         * Number of items: n
         * Perc volume: from 1 to 100
         * r maximum weight of the items
         * Number of repetitions of the experiment
         * Time limit in  seconds
         * List of solvers
         */

        InstanceGenerator benchmark = new StronglyCorrelated();

        switch (args[1]) {
            case "weaklycorrelated":
                benchmark = new WeaklyCorrelated();
                benchmark.setBinary("binary" == args[0]);
                break;
            case "stronglycorrelated":
                benchmark = new StronglyCorrelated();
                benchmark.setBinary("binary" == args[0]);
                break;
            case "uncorrelated":
                benchmark = new Uncorrelated();
                benchmark.setBinary("binary" == args[0]);
                break;
            case "subsetsum":
                benchmark = new SubsetSum();
                benchmark.setBinary("binary" == args[0]);
                break;
        }
        int[] seeds = {1234, 1989, 290889, 251091, 240664, 190364, 120863, 101295, 31089, 3573113, 30994, 7153, 897332, 174714, 53550, 108109, 1942, 42, 6462, 2001};
        int n = Integer.valueOf(args[2]);
        int percVolume = Integer.valueOf(args[3]);
        int r = Integer.valueOf(args[4]);
        int repetitions = Integer.valueOf(args[5]);
        int timeLimit = Integer.valueOf(args[6]);

        String[] solverNames = Arrays.copyOfRange(args, 7, args.length);


//        int[] weight = {10, 15, 25, 10};
//        int[] cost = {7, 6, 9, 10};
//        int[] minVal = {1, 0, 0, 0};
//        int[] maxVal = {3, 1, 3, 1};
//        KnapsackSolver solver = new KnapsackDP(n);
//        solver.solve(weight,cost,volume, minVal,maxVal);
//        KnapsackSolver solverB = new KnapsackBasicCPgoogle(n);
//        solverB.setTimeLimit(1000);
//        solverB.solve(weight,cost,volume,minVal,maxVal);
//        KnapsackSolver solverC = new KnapsackDPencodedMIP(n);
//        solverC.setTimeLimit(1000);
//        solverC.solve(weight,cost,volume,minVal,maxVal);
//        System.out.println("DP " + solver.getOptimalValue() + "   CP  " + solverB.getOptimalValue() + "  MIP   " + solverC.getOptimalValue());


        int[] weight;
        int[] cost;
        int[] minVal;
        int[] maxVal;

        int solSize = solverNames.length;

        KnapsackSolver solver;

        long[][] results = new long[repetitions][solSize];

        for (int s = 0; s < solSize; s++) {
            System.out.print(solverNames[s] + ",");

        }
        System.out.print("\n");
        for (int i = 0; i < repetitions; i++) {
//            System.out.println("Repetition "+ (i + 1) + " of " + r);
            int solVal = 0;
            benchmark.generate(n, r, percVolume, seeds[i]);
            weight = benchmark.getWeight();
            cost = benchmark.getCost();
            minVal = benchmark.getMinVal();
            maxVal = benchmark.getMaxVal();
            int v = benchmark.getVolume();
            //printInstance(benchmark);
            for (int s = 0; s < solSize; s++) {

                solver = getSolver(solverNames[s], n);
                solver.setTimeLimit(timeLimit);
                solver.setMaxVal(maxVal);
                solver.setMinVal(minVal);

                long startTime = System.nanoTime();
                solver.solve(weight, cost, v);
                long estimatedTime = System.nanoTime() - startTime;

                //System.out.println("Risultato " + solver.getOptimalValue() + "\tTime: " + estimatedTime  / 1000000 +"\n");

                if (s == 0)
                    solVal = solver.getOptimalValue();
                if (solVal - solver.getOptimalValue() > 10) {// (solvers.get(s).getOptimalValue() == -1 ) {
                    System.out.print(-estimatedTime / 10000 + ",");
                    results[i][s] = -estimatedTime;
                } else {
                    System.out.print(estimatedTime / 10000 + ",");
                    results[i][s] = estimatedTime;
                }
                solver = null;
                Runtime.getRuntime().runFinalization();
                Runtime.getRuntime().gc();
                System.gc();
                System.gc();
            }
            System.out.print("\n");


        }

        try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream("results_" + args[0] + "_"+ args[1] + "_" + n + "_" + r + "_" + percVolume + ".csv"), "utf-8"))) {
            for (int s = 0; s < solSize; s++) {
                writer.write(solverNames[s] + ",");
            }
            writer.write("\n");


            for (int i = 0; i < repetitions; i++) {
                for (int s = 0; s < solSize; s++) {
                    writer.write(results[i][s] + ",");
                }
                writer.write("\n");
            }
        }


    }

    static KnapsackSolver getSolver(String name, int n) {
        KnapsackSolver ks;
        switch (name) {
            case "CPgoogle":
                return new KnapsackBasicCPgoogle(n);
            case "MIP":
                return new KnapsackBasicMIP(n);
            case "MIPCPLEX":
                ks =  new KnapsackBasicMIP(n);
                ks.setSolverName("CPLEX_MIXED_INTEGER_PROGRAMMING");
                return ks;
            case "MIPGUROBI":
                ks =  new KnapsackBasicMIP(n);
                ks.setSolverName("GUROBI_MIXED_INTEGER_PROGRAMMING");
                return ks;
            case "ConstraintChoco":
                return new KnapsackConstraint(n);
            case "DPinCPgoogle":
                return new KnapsackDPencodedCPgoogle(n);
            case "DP":
                return new KnapsackDP(n);
            case "DPinMIP":
                return new KnapsackDPencodedMIP(n);
            case "DPinMIPCPLEX":
                ks =  new KnapsackDPencodedMIP(n);
                ks.setSolverName("CPLEX_MIXED_INTEGER_PROGRAMMING");
                return ks;
            case "DPinMIPGUROBI":
                ks =  new KnapsackDPencodedMIP(n);
                ks.setSolverName("GUROBI_MIXED_INTEGER_PROGRAMMING");
                return ks;
            case "binCPchoco":
                return new BinaryKnapsackBasicCP(n);
            case "binCPgoogle":
                return new BinaryKnapsackBasicCPgoogle(n);
            case "binMIP":
                return new BinaryKnapsackBasicMIP(n);
            case "binMIPCPLEX":
                ks =  new BinaryKnapsackBasicMIP(n);
                ks.setSolverName("CPLEX_MIXED_INTEGER_PROGRAMMING");
                return ks;
            case "binMIPGUROBI":
                ks =  new BinaryKnapsackBasicMIP(n);
                ks.setSolverName("GUROBI_MIXED_INTEGER_PROGRAMMING");
                return ks;
            case "binConstraintChoco":
                return new BinaryKnapsackConstraint(n);
            case "binDPinCPchoco":
                return new BinaryKnapsackCPandDP(n);
            case "binDPinCPgoogle":
                return new BinaryKnapsackCPandDPgoogle(n);
            case "binDPinCPgooglev2":
                return new BinaryKnapsackCPandDPgooglev2(n);
            case "binDPinCPgooglev3":
                return new BinaryKnapsackCPandDPgooglev3(n);
            case "binDP":
                return new BinaryKnapsackDP(n);
            case "binDPv2":
                return new BinaryKnapsackDPv2(n);
            case "binDPv2dic":
                return new BinaryKnapsackDPv2Dictionary(n);
            case "binDPinMIP":
                return new BinaryKnapsackDPencodedMIP(n);
            case "binDPinMIPCPLEX":
                ks =  new BinaryKnapsackDPencodedMIP(n);
                ks.setSolverName("CPLEX_MIXED_INTEGER_PROGRAMMING");
                return ks;
            case "binDPinMIPGUROBI":
                ks =  new BinaryKnapsackDPencodedMIP(n);
                ks.setSolverName("GUROBI_MIXED_INTEGER_PROGRAMMING");
                return ks;
            default:
                System.out.println("Solver not recognised");
        }
        return null;
    }

    static void printInstance(InstanceGenerator g) {

        int size = g.getSize();
        System.out.println("\nSize:" + size + "\tVolume:" + g.getVolume());

        System.out.println("\nWeights:");
        for (int i = 0; i < size; i++) {
            System.out.print(g.getWeight()[i] + "\t");
        }
        System.out.println("\nCost:");
        for (int i = 0; i < size; i++) {
            System.out.print(g.getCost()[i] + "\t");
        }
        System.out.println("\nMinVal:");
        for (int i = 0; i < size; i++) {
            System.out.print(g.getMinVal()[i] + "\t");
        }
        System.out.println("\nMaxVal:");
        for (int i = 0; i < size; i++) {
            System.out.print(g.getMaxVal()[i] + "\t");
        }

    }
}