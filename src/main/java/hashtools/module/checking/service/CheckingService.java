package hashtools.module.checking.service;

import hashtools.core.model.Checksum;
import hashtools.core.strategy.checksumsource.ChecksumSource;
import hashtools.core.strategy.inputsource.InputSource;
import hashtools.core.threadpool.ThreadPoolFactory;
import hashtools.module.checking.model.CheckingChecksum;
import hashtools.module.checking.model.CheckingResult;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class CheckingService {

    public CheckingResult performChecksumChecking(InputSource inputSource, ChecksumSource checksumSource) throws IOException {
        Objects.requireNonNull(inputSource, "The input source cannot be null");
        Objects.requireNonNull(checksumSource, "The checksum source cannot be null");



        List<Future<CheckingChecksum>> futureChecksums = new ArrayList<>();

        try (ExecutorService threadPool = ThreadPoolFactory.createDaemonPool()) {
            for (Checksum official : checksumSource.getValidChecksums()) {
                Future<CheckingChecksum> futureChecksum = threadPool.submit(() -> {
                    Checksum generated = official.generateChecksum(inputSource);

                    CheckingChecksum checksum = new CheckingChecksum();
                    checksum.setOfficialChecksum(official);
                    checksum.setGeneratedChecksum(generated);

                    return checksum;
                });

                futureChecksums.add(futureChecksum);
            }
        }



        CheckingResult result = new CheckingResult();
        result.setIdentification(inputSource.identify());

        try {
            for (Future<CheckingChecksum> checksum : futureChecksums) {
                result.addChecksum(checksum.get());
            }
        } catch (ExecutionException | InterruptedException e) {
            switch (e.getCause()) {
                case IOException cause -> throw cause;
                case Throwable cause -> throw new RuntimeException(cause);
            }
        }



        return result;
    }
}
