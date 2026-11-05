package hashtools.comparator.controller;

import hashtools.comparator.domain.ChecksumComparisonParameters;
import hashtools.comparator.service.ChecksumComparisonService;
import hashtools.core.domain.Algorithm;
import hashtools.core.domain.InputSource;
import hashtools.core.domain.ProblemException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class ComparatorController {

    private static final Logger log = LoggerFactory.getLogger(ComparatorController.class);



    private void performChecksumComparison() {
        var parameters = new ChecksumComparisonParameters();
        parameters.setInputSource1(this.createInputSource1());
        parameters.setInputSource2(this.createInputSource2());
        parameters.setAlgorithm(this.getAlgorithm());

        try {
            var service = new ChecksumComparisonService();
            var comparisonChecksum = service.perform(parameters);

            // TODO Present result into ui
            System.out.printf(
                "%s%n%s%n%s",
                comparisonChecksum.getChecksum1().getHash(),
                comparisonChecksum.getChecksum1().getHash(),
                comparisonChecksum.matches()
            );
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

    private InputSource createInputSource1() {
        // TODO Get data from ui
        return null;
    }

    private InputSource createInputSource2() {
        // TODO Get data from ui
        return null;
    }

    private Algorithm getAlgorithm() {
        // TODO Get data from ui
        return null;
    }
}
