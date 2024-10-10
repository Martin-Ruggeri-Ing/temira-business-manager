package ar.edu.um.temira.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class SleepDetectorTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static SleepDetector getSleepDetectorSample1() {
        return new SleepDetector().id(1L);
    }

    public static SleepDetector getSleepDetectorSample2() {
        return new SleepDetector().id(2L);
    }

    public static SleepDetector getSleepDetectorRandomSampleGenerator() {
        return new SleepDetector().id(longCount.incrementAndGet());
    }
}
