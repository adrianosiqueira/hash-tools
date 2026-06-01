package hashtools.module.checking.service;

import hashtools.core.model.Checksum;
import hashtools.core.model.Problem;
import hashtools.core.strategy.checksumsource.ChecksumSource;
import hashtools.core.strategy.inputsource.InputSource;
import hashtools.core.threadpool.ThreadPoolFactory;
import hashtools.module.checking.model.CheckingChecksum;
import hashtools.module.checking.model.CheckingResult;
import hashtools.module.checking.model.CheckingScreenInput;

import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class CheckingService {

    public CheckingService() {
    }



    public Optional<Problem> performInputValidation(CheckingScreenInput input) {
        if (input == null) {
            return Optional.of(new Problem()
                .withDescription("The input data is null.")
                .withCause("The variable was not initialized.")
                .withFix("It is an internal error. Report it to the developer."));
        }



        if (input.isUsingInputFile() && !Files.isRegularFile(input.getInputFile())) {
            return Optional.of(new Problem()
                .withDescription("The input is not a file.")
                .withCause("You may incorrectly entered the file path.")
                .withFix("Use the 'open' button to properly select a file."));
        }



        if (input.isUsingChecksumFile()) {
            if (!input.checksumFileHasValidExtension()) {
                return Optional.of(new Problem()
                    .withDescription("The checksum file is not valid.")
                    .withCause("You are attempting to use a file with an invalid extension.")
                    .withFix("Use the 'open' button to properly select the checksum file."));
            } else if (Files.notExists(input.getChecksumFile())) {
                return Optional.of(new Problem()
                    .withDescription("The checksum file does not exist.")
                    .withCause("The file may be deleted after selection or you entered a incorrect file path.")
                    .withFix("Use the 'open' button to properly select the checksum file."));
            } else if (!Files.isRegularFile(input.getChecksumFile())) {
                return Optional.of(new Problem()
                    .withDescription("The checksum file is not a file.")
                    .withCause("You may incorrectly entered the file path or the path ends in a directory.")
                    .withFix("Use the 'open' button to properly select the checksum file."));
            }
        }



        return Optional.empty();
    }

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
