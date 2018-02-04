package estrella.kata;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.lang.String.format;
import static java.util.concurrent.CompletableFuture.supplyAsync;
import static java.util.stream.Collectors.toList;

public class Core {
    private final Service service = new Service();

    public List<Integer> multiply(List<Integer> numbers) {
        return numbers.stream()
                .map(n -> {
                    printThreadName();
                    return n * service.loadNumber();
                })
                .collect(toList());
    }

    private final ExecutorService exec = Executors.newFixedThreadPool(10);

    public List<Integer> multiplyConcurrent(List<Integer> numbers) {
        List<CompletableFuture<Integer>> futures = numbers.stream()
                .map(n -> supplyAsync(() -> n))
                .map(cf -> cf.thenApplyAsync(n -> {
                    printThreadName();
                    return n * service.loadNumber();
                }, exec))
                .collect(toList());

        return futures.stream()
                .map(CompletableFuture::join)
                .collect(toList());
    }

    private static void printThreadName() {
        System.out.println(format("%s [%s] computing...", now(), Thread.currentThread().getName()));
    }

    private static String now() {
        return DateTimeFormatter.ISO_TIME.format(ZonedDateTime.now());
    }
}
