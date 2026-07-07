package com.enderium.physicsswapping.client.render;

import java.util.Random;

public class ItemRandom extends Random {

    public int nextInt(int origin, int bound) {
        if (origin == bound) return origin;
        return super.nextInt(origin, bound);
    }

    @Override
    public long nextLong(long origin, long bound) {
        if (origin == bound) return origin;
        return super.nextLong(origin, bound);
    }

    public float nextFloat(float origin, float bound) {
        if (origin == bound) return origin;
        return super.nextFloat(origin, bound);
    }

    @Override
    public double nextDouble(double origin, double bound) {
        if (origin == bound) return origin;
        return super.nextDouble(origin, bound);
    }
}
