package island.utility;

import java.util.Random;

public class RandomGen {
    private final Random bag;
    private final long seed;
    public RandomGen(long seed) {
        bag = new Random(seed);
        this.seed = seed;
    }

    public RandomGen() {
        Random seedGen = new Random();

        long seed = seedGen.nextLong(1, Long.MAX_VALUE);

        bag = new Random(seed);
        this.seed = seed;
    }

    public int nextInt(int origin, int bound) {
        return bag.nextInt(origin, bound);
    }

    public double nextDouble(double origin, double bound) {
        return bag.nextDouble(origin, bound);
    }

    public long getSeed() {
        return seed;
    }
}
