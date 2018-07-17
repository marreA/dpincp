import benchmark.InstanceGenerator;
import benchmark.StronglyCorrelated;
import solver.*;

import java.io.*;
import java.util.*;


public class main {

    //Constants
    public static void main(String[] args) throws IOException {

        args = new String[]{"correlated", "15", "5000", "20", "10",  "MIP", "CPgoogle", "DP", "DPinCPgoogle"};
        InstanceGenerator benchmark = new StronglyCorrelated();;
        switch(args[0]) {
            case "correlated" :
                benchmark = new StronglyCorrelated();
                break;

        }
        int[] seeds = {1234, 1989, 290889, 251091, 240664, 190364, 120863, 101295, 31089, 3573113, 30994, 7153, 897332, 174714, 53550, 108109, 1942, 42, 6462, 2001};
        int n = Integer.valueOf(args[1]);
        int volume = Integer.valueOf(args[2]);
        int r = Integer.valueOf(args[3]);
        int timeLimit = Integer.valueOf(args[4]);

        String[] solverNames =  Arrays.copyOfRange(args, 5, args.length);





//        int n = 25;
//        int volume = 5000;
//        int r = 20;
//        int timeLimit = 1;

        int[] weight;
        int[] cost;


        int solSize = solverNames.length;
        KnapsackSolver solver = new KnapsackBasicCP(n);;

        long[][] results = new long[r][solSize];

        for (int s = 0 ; s < solSize; s++) {
            System.out.print(solverNames[s] + ",");

        }
        System.out.print("\n");
        for (int i = 0; i < r; i ++) {
//            System.out.println("Repetition "+ (i + 1) + " of " + r);
            int solVal = 0;
            benchmark.generate(n,volume, seeds[i]);
            weight = benchmark.getWeight();
            cost = benchmark.getCost();
            int v = benchmark.getVolume();
//            System.out.println(n);
//            System.out.println(v);
            for (int s = 0 ; s < solSize; s++) {
//                System.out.println("Start solver " + solvers.get(s).getName());
                switch (solverNames[s]) {
                    case "CPchoco":
                        solver = new KnapsackBasicCP(n);
                        break;
                    case "CPgoogle":
                        solver = new KnapsackBasicCPgoogle(n);
                        break;
                    case "MIP":
                        solver = new KnapsackBasicMIP(n);
                        break;
                    case "ConstraintChoco":
                        solver = new KnapsackConstraint(n);
                        break;
                    case "DPinCPchoco":
                        solver = new KnapsackCPandDP(n);
                        break;
                    case "DPinCPgoogle":
                        solver = new KnapsackCPandDPgoogle(n);
                        break;
                    case "DPinCPgooglev2":
                        solver = new KnapsackCPandDPgooglev2(n);
                        break;
                    case "DPinCPgooglev3":
                        solver = new KnapsackCPandDPgooglev3(n);
                        break;
                    case "DP":
                        solver = new KnapsackDP(n);
                        break;
                    case "DPv2":
                        solver = new KnapsackDPv2(n);
                        break;
                    case "DPv2dic":
                        solver = new KnapsackDPv2dictionary(n);
                        break;
                    case "DPinMIP":
                        solver = new KnapsackDPencodedMIP(n);
                        break;
                    default:
                        System.out.println("Solver not recognised");
                }
                solver.setTimeLimit(timeLimit);

                long startTime = System.nanoTime();

                solver.solve(weight,cost,v);
                long estimatedTime = System.nanoTime() - startTime;
                if (s == 0)
                    solVal = solver.getOptimalValue();
//                System.out.println("Risultato " + solvers.get(s).getOptimalValue() + "\tTime: " + estimatedTime  / 1000000 +"\n");
                if (solVal - solver.getOptimalValue() > 10) {// (solvers.get(s).getOptimalValue() == -1 ) {
                    System.out.print(-estimatedTime/1000000 + ",");
                    results[i][s] = -estimatedTime;
                } else {
                    System.out.print(estimatedTime/1000000 + ",");
                    results[i][s] = estimatedTime;
                }


            }
            System.out.print("\n");



        }

        try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream("results_" + args[0] + "_"+ n + "_"+ r + "_"+ volume + ".csv"), "utf-8"))) {
            for (int s = 0 ; s < solSize; s++) {
                writer.write(solverNames[s] + ",");
            }
            writer.write("\n");


            for (int i = 0; i < r; i ++) {
                for (int s = 0 ; s < solSize; s++) {
                    writer.write(results[i][s]  + ",");
                }
                writer.write("\n");
            }
        }




    }

}