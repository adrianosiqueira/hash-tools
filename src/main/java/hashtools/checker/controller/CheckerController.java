package hashtools.checker.controller;

import hashtools.checker.domain.ChecksumCheckingParameters;
import hashtools.checker.domain.ChecksumSource;
import hashtools.checker.service.ChecksumCheckingService;
import hashtools.core.domain.InputSource;
import hashtools.core.domain.ProblemException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class CheckerController {

    private static final Logger log = LoggerFactory.getLogger(CheckerController.class);



    private void performChecksumChecking() {
        var parameters = new ChecksumCheckingParameters();
        parameters.setInputSource(this.createInputSource());
        parameters.setChecksumSource(this.createChecksumSource());

        try {
            var service = new ChecksumCheckingService();
            var checkingChecksums = service.perform(parameters);

            // TODO Present result into ui
            for (var checksum : checkingChecksums.getChecksums()) {
                var official = checksum.getOfficialChecksum();
                var generated = checksum.getGeneratedChecksum();
                IO.println(official.getHash() + "  " + generated.getHash());
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
        // TODO Get data from ui
        return null;
    }

    private ChecksumSource createChecksumSource() {
        // TODO Get data from ui
        return null;
    }
}
