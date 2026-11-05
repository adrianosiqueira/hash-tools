package hashtools.comparator.domain;

import hashtools.core.domain.Checksum;
import hashtools.core.domain.ChecksumGenerator;
import hashtools.core.strategy.ChecksumGeneratorUpdate;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class ComparisonChecksumGenerators {

    private ChecksumGenerator generator1;
    private ChecksumGenerator generator2;



    public void update(ChecksumGeneratorUpdate update1, ChecksumGeneratorUpdate update2)
    throws IOException, InterruptedException {
        var threadPool = Executors.newFixedThreadPool(
            Runtime.getRuntime().availableProcessors(),
            Thread.ofPlatform().daemon()::unstarted
        );
        var future1 = threadPool.submit(() -> {
            update1.update(List.of(generator1));
            return null;
        });
        var future2 = threadPool.submit(() -> {
            update2.update(List.of(generator2));
            return null;
        });

        try (threadPool) {
            future1.get();
            future2.get();
        } catch (ExecutionException e) {
            throw (IOException) e.getCause();
        }
    }

    public void decodeAndCollect(Consumer<Checksum> consumer1, Consumer<Checksum> consumer2) {
        var checksum1 = generator1.decodeIntoChecksum();
        consumer1.accept(checksum1);

        var checksum2 = generator2.decodeIntoChecksum();
        consumer2.accept(checksum2);
    }

    public ChecksumGenerator getGenerator1() {
        return generator1;
    }

    public void setGenerator1(ChecksumGenerator generator1) {
        this.generator1 = Objects.requireNonNull(generator1);
    }

    public ChecksumGenerator getGenerator2() {
        return generator2;
    }

    public void setGenerator2(ChecksumGenerator generator2) {
        this.generator2 = Objects.requireNonNull(generator2);
    }
}
