package benchmark;
import java.util.Random;

public class SubsetSum extends InstanceGenerator {

    @Override
    public void generate(int n, int r, double volPerc, int seed) {
        Random rand = new Random(seed);
        size = n;
        int totVol = 0;

        weight = new int[n];
        cost = new int[n];
        minVal = new int[n];
        maxVal = new int[n];

        for (int i = 0; i < n; i++){
            weight[i] = rand.nextInt(r) + 1;
            totVol += weight[i];
            cost[i] = weight[i];
        }
        volume = (int) (totVol * volPerc / 100);

        for (int i = 0; i < n; i++){

            minVal[i] = 0;
            if(binary) {
                maxVal[i] = 1;
            } else {
                maxVal[i] = rand.nextInt(Math.abs((int) Math.floor(volume / weight[i]))) + 1;
            }
        }
        //System.out.println("Caisn" + totVol + "  " + volPerc +"   " + volume);


    }
}
