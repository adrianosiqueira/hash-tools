package hashtools.domain.checker.service;

import hashtools.core.checksum.Checksum;
import hashtools.core.source.checksum.ChecksumSource;
import hashtools.core.source.input.InputSource;
import hashtools.core.threadpool.ThreadPoolFactory;
import hashtools.domain.checker.model.CheckerChecksum;
import hashtools.domain.checker.model.CheckerResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class CheckerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CheckerService.class);



    public CheckerResult performChecksumChecking(InputSource inputSource, ChecksumSource checksumSource) throws IOException {
        LOGGER.info("Performing the checksum checking.");
        Objects.requireNonNull(inputSource, "The input source cannot be null");
        Objects.requireNonNull(checksumSource, "The checksum source cannot be null");



        List<Future<CheckerChecksum>> futureChecksums = new ArrayList<>();

        try (ExecutorService threadPool = ThreadPoolFactory.createDaemonPool()) {
            for (Checksum official : checksumSource.getValidChecksums()) {
                Future<CheckerChecksum> futureChecksum = threadPool.submit(() -> {
                    Checksum generated = official.generateChecksum(inputSource);

                    CheckerChecksum checksum = new CheckerChecksum();
                    checksum.setOfficialChecksum(official);
                    checksum.setGeneratedChecksum(generated);

                    return checksum;
                });

                futureChecksums.add(futureChecksum);
            }
        }



        CheckerResult result = new CheckerResult();
        result.setIdentification(inputSource.identify());

        try {
            for (Future<CheckerChecksum> checksum : futureChecksums) {
                result.addChecksum(checksum.get());
            }
        } catch (ExecutionException | InterruptedException e) {
            LOGGER.error("Failed to perform the checksum checking.", e);
            switch (e.getCause()) {
                case IOException cause -> throw cause;
                case Throwable cause -> throw new RuntimeException(cause);
            }
        }



        LOGGER.info("The checksum checking is finished.");
        return result;
    }
}
