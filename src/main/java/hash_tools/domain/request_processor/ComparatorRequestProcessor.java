package hash_tools.domain.request_processor;

import hash_tools.domain.checksum.Checksum;
import hash_tools.domain.request.ComparatorRequest;
import hash_tools.domain.result.ComparatorResult;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Function;

public class ComparatorRequestProcessor implements Function<ComparatorRequest, ComparatorResult> {

    private static final int MAXIMUM_REQUIRED_CORES = 2;

    private ComparatorRequest request;



    @Override
    public ComparatorResult apply(ComparatorRequest request) {
        this.request = request;
        List<IdentifiedChecksum> checksums = new LinkedList<>();


        try (ExecutorService executor = Executors.newFixedThreadPool(coresToUse(), this::createDaemonThread)) {
            CompletableFuture
                .supplyAsync(this::generateChecksum1, executor)
                .thenApplyAsync(this::identifyChecksum1, executor)
                .thenAcceptAsync(checksums::add, executor);

            CompletableFuture
                .supplyAsync(this::generateChecksum2, executor)
                .thenApplyAsync(this::identifyChecksum2, executor)
                .thenAcceptAsync(checksums::add, executor);
        }


        return new ComparatorResult(
            checksums.getFirst().checksum(),
            checksums.getFirst().identification(),
            checksums.getLast().checksum(),
            checksums.getLast().identification()
        );
    }



    private Checksum generateChecksum1() {
        return request
            .checksumSource1()
            .generateChecksum(request.algorithm());
    }

    private Checksum generateChecksum2() {
        return request
            .checksumSource2()
            .generateChecksum(request.algorithm());
    }

    private IdentifiedChecksum identifyChecksum1(Checksum checksum) {
        return new IdentifiedChecksum(
            checksum,
            request.checksumSource1().identify()
        );
    }

    private IdentifiedChecksum identifyChecksum2(Checksum checksum) {
        return new IdentifiedChecksum(
            checksum,
            request.checksumSource2().identify()
        );
    }



    private int coresToUse() {
        return Math.min(
            MAXIMUM_REQUIRED_CORES,
            Runtime.getRuntime().availableProcessors()
        );
    }

    private Thread createDaemonThread(Runnable runnable) {
        return Thread
            .ofPlatform()
            .daemon()
            .unstarted(runnable);
    }



    private record IdentifiedChecksum(Checksum checksum, String identification) {}
}
