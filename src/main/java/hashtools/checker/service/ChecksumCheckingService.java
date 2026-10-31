package hashtools.checker.service;

import hashtools.checker.domain.CheckingChecksumGenerators;
import hashtools.checker.domain.CheckingChecksums;
import hashtools.checker.domain.ChecksumCheckingParameters;
import hashtools.core.domain.ProblemException;

import java.io.IOException;

public class ChecksumCheckingService {

    public CheckingChecksums perform(ChecksumCheckingParameters parameters)
    throws IOException, InterruptedException, ProblemException {
        var inputSource = parameters.getInputSource();
        var checksumSource = parameters.getChecksumSource();

        inputSource.detectProblem();
        checksumSource.detectProblem();



        var officialChecksums = checksumSource.extractOfficialChecksums();

        var checkingGenerators = new CheckingChecksumGenerators();
        checkingGenerators.addFromOfficialChecksums(officialChecksums);
        checkingGenerators.update(inputSource::updateGenerators);



        var checkingChecksums = new CheckingChecksums();
        checkingGenerators.decodeAndCollect(checkingChecksums::add);

        return checkingChecksums;
    }
}
