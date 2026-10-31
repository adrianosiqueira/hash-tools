package hashtools.generator.controller;

import hashtools.core.domain.Algorithm;
import hashtools.core.domain.InputSource;
import hashtools.core.domain.ProblemException;
import hashtools.generator.domain.ChecksumGenerationParameters;
import hashtools.generator.service.ChecksumGenerationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Collection;

public class GeneratorController {

    private static final Logger log = LoggerFactory.getLogger(GeneratorController.class);



    private void performChecksumGeneration() {
        var parameters = new ChecksumGenerationParameters();
        parameters.setInputSource(this.createInputSource());
        parameters.setAlgorithms(this.createAlgorithmList());

        try {
            var service = new ChecksumGenerationService();
            var generationChecksums = service.perform(parameters);

            // TODO Present result into ui
            for (var checksum : generationChecksums.getChecksums()) {
                IO.println(checksum.getHash());
            }
        } catch (ProblemException e) {
            // TODO Show message dialog
            log.warn("Problem: ", e);
        } catch (IOException e) {
            // TODO Log to file
            // TODO Show message dialog
            log.error("Error: ", e);
        } catch (InterruptedException e) {
            // TODO Clean up ui
            log.debug("Canceled");
        }
    }

    private InputSource createInputSource() {
        // TODO Get date from ui
        return null;
    }

    private Collection<Algorithm> createAlgorithmList() {
        // TODO Create from ui
        return null;
    }
}
