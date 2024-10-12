package ar.edu.um.temira.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class VehicleBrandTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static VehicleBrand getVehicleBrandSample1() {
        return new VehicleBrand().id(1L).name("name1");
    }

    public static VehicleBrand getVehicleBrandSample2() {
        return new VehicleBrand().id(2L).name("name2");
    }

    public static VehicleBrand getVehicleBrandRandomSampleGenerator() {
        return new VehicleBrand().id(longCount.incrementAndGet()).name(UUID.randomUUID().toString());
    }
}
