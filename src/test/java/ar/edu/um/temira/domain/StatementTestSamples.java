package ar.edu.um.temira.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class StatementTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Statement getStatementSample1() {
        return new Statement().id(1L).destination("destination1").pathCsv("pathCsv1");
    }

    public static Statement getStatementSample2() {
        return new Statement().id(2L).destination("destination2").pathCsv("pathCsv2");
    }

    public static Statement getStatementRandomSampleGenerator() {
        return new Statement()
            .id(longCount.incrementAndGet())
            .destination(UUID.randomUUID().toString())
            .pathCsv(UUID.randomUUID().toString());
    }
}
