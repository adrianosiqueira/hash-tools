package hashtools.service;

import hashtools.domain.algorithm.ChecksumGenerator;
import hashtools.domain.checksum.CheckerChecksum;
import hashtools.domain.checksum.Checksum;
import hashtools.domain.container.ChecksumCheckingContainer;
import hashtools.domain.context.ChecksumCheckingContext;
import hashtools.domain.result.ChecksumCheckingResult;

import java.io.IOException;
import java.security.MessageDigest;
import java.util.Collection;

public class CheckerService {

    public ChecksumCheckingContainer performChecksumChecking(ChecksumCheckingContext context) {
        // Initial setup
        context.updateProgress(0.0);


        // Problem detection
        String problem = context
            .detectProblem()
            .orElse(null);

        if (problem != null) {
            return ChecksumCheckingContainer.problem(problem);
        }



        try {
            // Processing data
            Collection<OfficialChecksum> officialChecksums = context
                .extractOfficialChecksums()
                .stream()
                .map(OfficialChecksum::checksum)
                .toList();



            // Processing
            Collection<MessageDigest> messageDigests = officialChecksums
                .stream()
                .map(OfficialChecksum::getMessageDigest)
                .toList();

            context.updateMessageDigests(messageDigests);



            // Result collecting
            ChecksumCheckingResult result = new ChecksumCheckingResult();

            officialChecksums
                .stream()
                .map(OfficialChecksum::toCheckerChecksum)
                .forEach(result::addChecksum);

            return ChecksumCheckingContainer.result(result);
        } catch (IOException e) {
            return ChecksumCheckingContainer.exception(e);
        }
    }



    private record OfficialChecksum(
        Checksum checksum,
        ChecksumGenerator messageDigestProxy
    ) {

        public static OfficialChecksum checksum(Checksum checksum) {
            return new OfficialChecksum(
                checksum,
                checksum.createMessageDigestProxy()
            );
        }



        public MessageDigest getMessageDigest() {
            return messageDigestProxy.messageDigest();
        }

        public CheckerChecksum toCheckerChecksum() {
            CheckerChecksum checkerChecksum = new CheckerChecksum();
            checkerChecksum.setOfficial(checksum);
            checkerChecksum.setGenerated(messageDigestProxy.decodeIntoChecksum());

            return checkerChecksum;
        }
    }
}
