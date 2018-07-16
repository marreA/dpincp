import benchmark.InstanceGenerator;
import benchmark.StronglyCorrelated;
import solver.*;

import java.io.*;
import java.util.*;


public class main {

    //Constants
    public static void main(String[] args) throws IOException {

        args = new String[]{"correlated", "15", "5000", "20", "10", "CPgoogle", "MIP", "DP", "DPinCPgoogle", "DPinCPgooglev3"};
        InstanceGenerator benchmark = new StronglyCorrelated();;
        switch(args[0]) {
            case "correlated" :
                benchmark = new StronglyCorrelated();
                break;

        }
        int n = Integer.valueOf(args[1]);
        int volume = Integer.valueOf(args[2]);
        int r = Integer.valueOf(args[3]);
        int timeLimit = Integer.valueOf(args[4]);

        String[] solverNames =  Arrays.copyOfRange(args, 5, args.length);
        ArrayList<KnapsackSolver> solvers = new ArrayList<KnapsackSolver>();

        for (String name: solverNames) {
            switch (name) {
                case "CPchoco":
                    solvers.add(new KnapsackBasicCP(n));
                    break;
                case "CPgoogle":
                    solvers.add(new KnapsackBasicCPgoogle(n));
                    break;
                case "MIP":
                    solvers.add(new KnapsackBasicMIP(n));
                    break;
                case "ConstraintChoco":
                    solvers.add(new KnapsackConstraint(n));
                    break;
                case "DPinCPchoco":
                    solvers.add(new KnapsackCPandDP(n));
                    break;
                case "DPinCPgoogle":
                    solvers.add(new KnapsackCPandDPgoogle(n));
                    break;
                case "DPinCPgooglev2":
                    solvers.add(new KnapsackCPandDPgooglev2(n));
                    break;
                case "DPinCPgooglev3":
                    solvers.add(new KnapsackCPandDPgooglev3(n));
                    break;
                case "DP":
                    solvers.add(new KnapsackDP(n));
                    break;
                case "DPv2":
                    solvers.add(new KnapsackDPv2(n));
                    break;
                case "DPv2dic":
                    solvers.add(new KnapsackDPv2dictionary(n));
                    break;
                case "DPinMIP":
                    solvers.add(new KnapsackDPencodedMIP(n));
                    break;
                default:
                    System.out.println("Solver not recognised");
            }
        }




//        int n = 25;
//        int volume = 5000;
//        int r = 20;
//        int timeLimit = 1;

        int[] weight;
        int[] cost;


        int solSize = solvers.size();

        long[][] results = new long[r][solSize];

        for (int s = 0 ; s < solSize; s++) {
            System.out.print(solvers.get(s).getName() + ",");
            solvers.get(s).setTimeLimit(timeLimit);
        }
        System.out.print("\n");
        for (int i = 0; i < r; i ++) {
//            System.out.println("Repetition "+ (i + 1) + " of " + r);
            int solVal = 0;
            benchmark.generate(n,volume);
            weight = benchmark.getWeight();
            cost = benchmark.getCost();
            int v = benchmark.getVolume();
//            System.out.println(n);
//            System.out.println(v);
            for (int s = 0 ; s < solSize; s++) {
//                System.out.println("Start solver " + solvers.get(s).getName());


                long startTime = System.nanoTime();

                solvers.get(s).solve(weight,cost,v);
                long estimatedTime = System.nanoTime() - startTime;
                if (s == 0)
                    solVal = solvers.get(s).getOptimalValue();
//                System.out.println("Risultato " + solvers.get(s).getOptimalValue() + "\tTime: " + estimatedTime  / 1000000 +"\n");
                if (solVal - solvers.get(s).getOptimalValue() > 10) {// (solvers.get(s).getOptimalValue() == -1 ) {
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
                writer.write(solvers.get(s).getName() + ",");
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