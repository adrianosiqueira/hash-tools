package hashtools.module.generation;

import hashtools.core.model.Algorithm;
import hashtools.core.model.Checksum;
import hashtools.core.threadpool.ThreadPoolFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class ChecksumGeneration {

    public ChecksumGenerationResult perform(ChecksumGenerationContext context) throws Exception {
        List<Future<ChecksumGenerationDTO>> futureChecksums = new ArrayList<>();

        try (ExecutorService executor = ThreadPoolFactory.createDaemonPool()) {
            for (Algorithm algorithm : context.getAlgorithms()) {
                futureChecksums.add(executor.submit(
                    () -> this.generateChecksumMappingToDTO(algorithm, context)
                ));
            }
        }



        ChecksumGenerationResult result = new ChecksumGenerationResult();

        for (Future<ChecksumGenerationDTO> future : futureChecksums) {
            ChecksumGenerationDTO checksum = future.get();
            result.addChecksum(checksum);
        }

        return result;
    }



    private ChecksumGenerationDTO generateChecksumMappingToDTO(Algorithm algorithm, ChecksumGenerationContext context) throws Exception {
        Checksum generated = context.generateChecksum(algorithm);

        ChecksumGenerationDTO checksum = new ChecksumGenerationDTO();
        checksum.setChecksum(generated);
        checksum.setIdentifier(context::getIdentification);

        return checksum;
    }
}
