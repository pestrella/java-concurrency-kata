package estrella.kata;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Function;

import static java.lang.String.format;
import static java.util.stream.Collectors.toList;

public class Core {
    private final Service service = new Service();

    public List<Integer> multiply(List<Integer> numbers) {
        return numbers.stream()
                .map(doMultiply())
                .collect(toList());
    }

    public List<Integer> multiplyConcurrent(List<Integer> numbers) {
        List<CompletableFuture<Integer>> futures = numbers.stream()
                .map(supplyAsync())
                .map(thenApply(doMultiply()))
                .collect(toList());

        return futures.stream()
                .map(CompletableFuture::join)
                .collect(toList());
    }

    private final ExecutorService exec = Executors.newFixedThreadPool(20);

    public List<Integer> multiplyWithExecutor(List<Integer> numbers) {
        List<CompletableFuture<Integer>> futures = numbers.stream()
                .map(supplyAsync())
                .map(thenApply(doMultiply(), exec))
                .collect(toList());

        return futures.stream()
                .map(CompletableFuture::join)
                .collect(toList());
    }

    private Function<Integer, Integer> doMultiply() {
        return n -> {
            printThreadName();
            return n * service.loadNumber();
        };
    }

    private static void printThreadName() {
        System.out.println(format("%s [%s] computing...", now(), Thread.currentThread().getName()));
    }

    private static String now() {
        return DateTimeFormatter.ISO_TIME.format(ZonedDateTime.now());
    }

    private static <T> Function<T, CompletableFuture<T>> supplyAsync() {
        return t -> CompletableFuture.supplyAsync(() -> t);
    }

    private static <T, R> Function<CompletableFuture<T>, CompletableFuture<R>> thenApply(Function<T, R> f) {
        return completableFuture -> completableFuture.thenApply(f);
    }

    private static <T, R> Function<CompletableFuture<T>, CompletableFuture<R>> thenApply(
            Function<T, R> f,
            ExecutorService exec) {
        return completableFuture -> completableFuture.thenApplyAsync(f, exec);
    }
}
