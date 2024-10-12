package ar.edu.um.temira.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class VehicleTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Vehicle getVehicleSample1() {
        return new Vehicle().id(1L).model(1).name("name1");
    }

    public static Vehicle getVehicleSample2() {
        return new Vehicle().id(2L).model(2).name("name2");
    }

    public static Vehicle getVehicleRandomSampleGenerator() {
        return new Vehicle().id(longCount.incrementAndGet()).model(intCount.incrementAndGet()).name(UUID.randomUUID().toString());
    }
}
