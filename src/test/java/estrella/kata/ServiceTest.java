package estrella.kata;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;

public class ServiceTest {
    private Core core = new Core();

    @Test
    public void testMultiply() {
        println("Synchronous test:");
        runMultiply(core::multiply, 5);
    }

    @Test
    public void testMultiplyConcurrent() {
        println("Concurrency test:");
        runMultiply(core::multiplyConcurrent, 50);

        println("Concurrency test with thread pool:");
        runMultiply(core::multiplyWithExecutor, 50);
    }

    private static void println(Object o) {
        System.out.println(o);
    }

    private static void runMultiply(Function<List<Integer>, List<Integer>> multiply, int n) {
        List<Integer> numbers = randomNumbers(n, 20);
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
