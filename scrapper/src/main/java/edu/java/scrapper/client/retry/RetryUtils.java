package edu.java.scrapper.client.retry;

import edu.java.scrapper.configuration.RetryType;
import edu.java.scrapper.exceptions.ServiceException;
import java.time.Duration;
import java.util.List;
import java.util.function.Predicate;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

public final class RetryUtils {
    private static final String TIMES = " times";

    private RetryUtils() {
    }

    public static Retry createRetry(RetryType type, Integer maxAttempt, Integer delay, List<Integer> codes) {
        if (type.equals(RetryType.FIXED)) {
            return RetryUtils.fixedRetry(maxAttempt, delay, codes);
        } else if (type.equals(RetryType.LINEAR)) {
            return RetryUtils.linearRetry(maxAttempt, delay, codes);
        } else {
            return RetryUtils.exponentialRetry(maxAttempt, delay, codes);
        }
    }

    public static Retry fixedRetry(Integer maxAttempt, Integer delay, List<Integer> codes) {

        return Retry.fixedDelay(maxAttempt, Duration.ofSeconds(delay))
            .onRetryExhaustedThrow((spec, signal) -> {
                throw new ServiceException(
                    "Service call failed even after fixed retrying " + signal.totalRetries() + TIMES);
            })
            .filter(isCurrentCodeInCodeList(codes));
    }

    public static Retry linearRetry(Integer maxAttempt, Integer delay, List<Integer> codes) {
        return Retry.from(
            (signalFlux) -> signalFlux
                .flatMap(signal -> {
                    Throwable throwable = signal.failure();
                    if (throwable instanceof WebClientResponseException) {
                        HttpStatusCode a = ((WebClientResponseException) throwable).getStatusCode();
                        int code = a.value();
                        if (codes == null || !codes.contains(code)) {
                            return reactor.core.publisher.Flux.error(throwable);
                        }
                    }
                    if (signal.totalRetries() == maxAttempt) {
                        return reactor.core.publisher.Flux.error(new ServiceException(
                            "Service call failed even after linear retrying " + signal.totalRetries() + TIMES));
                    }
                    long delaySeconds =
                        (signal.totalRetries() + 1) * delay;
                    return Mono.delay(Duration.ofSeconds(delaySeconds))
                        .thenReturn(signal.totalRetries()); // Отложенное выполнение задержки
                })
        );
    }

    public static Retry exponentialRetry(Integer maxAttempt, Integer delay, List<Integer> codes) {
        return Retry.backoff(maxAttempt, Duration.ofSeconds(delay))
            .onRetryExhaustedThrow((spec, signal) -> {
                throw new ServiceException(
                    "Service call failed even after exponential retrying " + signal.totalRetries() + TIMES);
            })
            .filter(isCurrentCodeInCodeList(codes));
    }

    @NotNull
    private static Predicate<Throwable> isCurrentCodeInCodeList(List<Integer> codes) {
        return throwable -> {
            if (throwable instanceof WebClientResponseException) {
                HttpStatusCode a = ((WebClientResponseException) throwable).getStatusCode();
                int code = a.value();
                return codes != null && codes.contains(code);
            }
            return false;
        };
    }
}
