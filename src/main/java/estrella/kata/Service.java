package estrella.kata;

import java.util.Random;

public class Service {
    public Integer loadNumber() {
        int n = rand.nextInt(1000);
        sleep(n);
        return n;
    }

    private static void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private final Random rand = new Random();
}
