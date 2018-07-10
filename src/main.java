import benchmark.InstanceGenerator;
import benchmark.StronglyCorrelated;
import solver.*;

import java.io.*;
import java.util.*;


public class main {

    //Constants
    public static void main(String[] args) throws IOException {
//
//        String PATHTTOEXAMPLE = "/Users/avisentin/intelliJ/knapsackCP/kplib/01WeaklyCorrelated/n00050/R01000/";
//
//        File file = new File(PATHTTOEXAMPLE +"s00010.kp");
//        Scanner scanner = new Scanner(file);
//        int nParts = scanner.nextInt();
//        int volume = scanner.nextInt();
//
//        int[] weight = new int[nParts];
//        int[] cost = new int[nParts];
//        for (int j = 0; j < nParts; j++) {
//            weight[j] = scanner.nextInt();
//            cost[j] = scanner.nextInt();
//        }
        InstanceGenerator benchmark = new StronglyCorrelated();



        int n = 75;
        int volume = 5000;
        int r = 20;
        int[] weight = new int[n];
        int[] cost = new int[n];
        ArrayList<KnapsackSolver> solvers = new ArrayList<KnapsackSolver>();


//        solvers.add(new KnapsackCPandDP(n));
//        solvers.add(new KnapsackConstraint(n));
//        solvers.add(new KnapsackBasicCPgoogle(n));
//        solvers.add(new KnapsackDPencodedMIP(n));
        solvers.add(new KnapsackCPandDPgoogle(n));
        solvers.add(new KnapsackCPandDPgooglev2(n));
        solvers.add(new KnapsackCPandDPgooglev3(n));
        solvers.add(new KnapsackDP(n));
        solvers.add(new KnapsackDPv2(n));
//        solvers.add(new KnapsackDPv2dictionary(n));
//        solvers.add(new KnapsackBasicCP(n));
        solvers.add(new KnapsackBasicMIP(n));

        int solSize = solvers.size();

        long[][] results = new long[r][solSize];

        for (int s = 0 ; s < solSize; s++) {
            System.out.print(solvers.get(s).getName() + ",");
        }
        System.out.print("\n");
        for (int i = 0; i < r; i ++) {
//            System.out.println("Repetition "+ (i + 1) + " of " + r);
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
//                System.out.println("Risultato " + solvers.get(s).getOptimalValue() + "\tTime: " + estimatedTime  / 1000000 +"\n");
                System.out.print(estimatedTime + ",");

                results[i][s] = estimatedTime;

            }
            System.out.print("\n");



        }

        try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream("results.csv"), "utf-8"))) {
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
