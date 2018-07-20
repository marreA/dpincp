import benchmark.InstanceGenerator;
import benchmark.StronglyCorrelated;
import solver.*;

import java.io.*;
import java.util.*;


public class main {

    //Constants
    public static void main(String[] args) throws IOException {

//        args = new String[]{"binary", "correlated", "200", "5000", "20", "10", "binDP"};


        InstanceGenerator benchmark = new StronglyCorrelated();

        switch (args[1]) {
            case "correlated":
                benchmark = new StronglyCorrelated();
                benchmark.setBinary("binary" == args[0]);
                break;

        }
        int[] seeds = {1234, 1989, 290889, 251091, 240664, 190364, 120863, 101295, 31089, 3573113, 30994, 7153, 897332, 174714, 53550, 108109, 1942, 42, 6462, 2001};
        int n = Integer.valueOf(args[2]);
        int volume = Integer.valueOf(args[3]);
        int r = Integer.valueOf(args[4]);
        int timeLimit = Integer.valueOf(args[5]);

        String[] solverNames = Arrays.copyOfRange(args, 6, args.length);


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

        long[][] results = new long[r][solSize];

        for (int s = 0; s < solSize; s++) {
            System.out.print(solverNames[s] + ",");

        }
        System.out.print("\n");
        for (int i = 0; i < r; i++) {
//            System.out.println("Repetition "+ (i + 1) + " of " + r);
            int solVal = 0;
            benchmark.generate(n, volume, seeds[i]);
            weight = benchmark.getWeight();
            cost = benchmark.getCost();
            minVal = benchmark.getMinVal();
            maxVal = benchmark.getMaxVal();
            int v = benchmark.getVolume();
            printInstance(benchmark);
            for (int s = 0; s < solSize; s++) {

                solver = getSolver(solverNames[s], n);
                solver.setTimeLimit(timeLimit);
                solver.setMaxVal(maxVal);
                solver.setMinVal(minVal);

                long startTime = System.nanoTime();
                solver.solve(weight, cost, v);
                long estimatedTime = System.nanoTime() - startTime;

//                System.out.println("Risultato " + solver.getOptimalValue() + "\tTime: " + estimatedTime  / 1000000 +"\n");

                if (s == 0)
                    solVal = solver.getOptimalValue();
                if (solVal - solver.getOptimalValue() > 10) {// (solvers.get(s).getOptimalValue() == -1 ) {
                    System.out.print(-estimatedTime / 1000000 + ",");
                    results[i][s] = -estimatedTime;
                } else {
                    System.out.print(estimatedTime / 1000000 + ",");
                    results[i][s] = estimatedTime;
                }


            }
            System.out.print("\n");


        }

        try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream("results_" + args[0] + "_"+ args[1] + "_" + n + "_" + r + "_" + volume + ".csv"), "utf-8"))) {
            for (int s = 0; s < solSize; s++) {
                writer.write(solverNames[s] + ",");
            }
            writer.write("\n");


            for (int i = 0; i < r; i++) {
                for (int s = 0; s < solSize; s++) {
                    writer.write(results[i][s] + ",");
                }
                writer.write("\n");
            }
        }


    }

    static KnapsackSolver getSolver(String name, int n) {
        switch (name) {
            case "CPgoogle":
                return new KnapsackBasicCPgoogle(n);
            case "MIP":
                return new KnapsackBasicMIP(n);
            case "ConstraintChoco":
                return new KnapsackConstraint(n);
            case "DPinCPgoogle":
                return new KnapsackDPencodedCPgoogle(n);
            case "DP":
                return new KnapsackDP(n);
            case "DPinMIP":
                return new KnapsackDPencodedMIP(n);
            case "binCPchoco":
                return new BinaryKnapsackBasicCP(n);
            case "binCPgoogle":
                return new BinaryKnapsackBasicCPgoogle(n);
            case "binMIP":
                return new BinaryKnapsackBasicMIP(n);
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