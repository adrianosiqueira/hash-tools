package hashtools.comparator;

import hashtools.shared.Algorithm;
import hashtools.shared.ChecksumGenerator;
import hashtools.shared.Formatter;
import hashtools.shared.RequestProcessor;
import hashtools.shared.ResponseFormatter;
import hashtools.shared.threadpool.ThreadPool;

import java.util.concurrent.ExecutorService;

public class ComparatorService implements RequestProcessor<ComparatorRequest, ComparatorResponse>, ResponseFormatter<ComparatorResponse> {

    @Override
    public String formatResponse(ComparatorResponse response, Formatter<ComparatorResponse> formatter) {
        return formatter.format(response);
    }

    @Override
    public ComparatorResponse processRequest(ComparatorRequest request) {
        ComparatorChecksum checksum = new ComparatorChecksum();
        checksum.setAlgorithm(Algorithm.MD5);

        try (ExecutorService executor = ThreadPool.newFixedDaemon("ComparatorThreadPool")) {
            ChecksumGenerator generator = new ChecksumGenerator();

            executor.execute(() -> {
                String hash = generator.generate(
                    checksum.getAlgorithm(),
                    request.createNewMessageDigestUpdater1()
                );

                checksum.setHash1(hash);
            });

            executor.execute(() -> {
                String hash = generator.generate(
                    checksum.getAlgorithm(),
                    request.createNewMessageDigestUpdater2()
                );

                checksum.setHash2(hash);
            });
        }

        ComparatorResponse response = new ComparatorResponse();
        response.setIdentification1(request.createNewIdentification1());
        response.setIdentification2(request.createNewIdentification2());
        response.setChecksum(checksum);
        return response;
    }
}
