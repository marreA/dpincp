package benchmark;
import java.util.Random;

public class StronglyCorrelated extends InstanceGenerator {

    @Override
    public void generate(int n, int vol, int seed) {
        Random rand = new Random(seed);

        int maxVol = vol/n * 4;
        weight = new int[n];
        cost = new int[n];
        volume = (int) (vol + (rand.nextDouble() - 0.5) * vol * 0.2);
        int minVol = maxVol/10;

        for (int i = 0; i < n; i++){
            weight[i] = rand.nextInt(maxVol - minVol) + minVol;
            cost[i] = (int) ((rand.nextDouble() * 0.4 + 0.8) * weight[i]);
        }

    }
}
