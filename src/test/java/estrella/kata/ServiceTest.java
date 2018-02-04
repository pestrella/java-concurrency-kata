package estrella.kata;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;

import static org.junit.Assert.assertTrue;

public class ServiceTest {
    @Test
    public void testRandom() {
        Service service = new Service();
        Integer n = service.loadNumber();
        System.out.println("n = " + n);
        assertTrue(n < 1000);
    }

    @Test
    public void testMultiply() {
        Core core = new Core();
        runMultiply(core::multiply);
        runMultiply(core::multiplyConcurrent);
    }

    private static void runMultiply(Function<List<Integer>, List<Integer>> multiply) {
        List<Integer> numbers = randomNumbers(10, 20);
        long start = System.currentTimeMillis();
        List<Integer> answers = multiply.apply(numbers);
        long elapsed = System.currentTimeMillis() - start;
        System.out.println(String.format("%d numbers loaded in %d ms: %s", answers.size(), elapsed, answers));
    }

    private static List<Integer> randomNumbers(int length, int bound) {
        List<Integer> numbers = new ArrayList();
        for (int i = 0; i < length; i++) {
            numbers.add(ThreadLocalRandom.current().nextInt(bound));
        }
        return numbers;
    }
}
