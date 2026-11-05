package hashtools.comparator.service;

import hashtools.comparator.domain.ChecksumComparisonParameters;
import hashtools.comparator.domain.ComparisonChecksum;
import hashtools.comparator.domain.ComparisonChecksumGenerators;
import hashtools.core.domain.ProblemException;

import java.io.IOException;

public class ChecksumComparisonService {

    public ComparisonChecksum perform(ChecksumComparisonParameters parameters)
    throws IOException, InterruptedException, ProblemException {
        var inputSource1 = parameters.getInputSource1();
        var inputSource2 = parameters.getInputSource2();

        inputSource1.detectProblem();
        inputSource2.detectProblem();



        var comparisonGenerators = new ComparisonChecksumGenerators();
        comparisonGenerators.setGenerator1(parameters.getAlgorithm().createChecksumGenerator());
        comparisonGenerators.setGenerator2(parameters.getAlgorithm().createChecksumGenerator());
        comparisonGenerators.update(
            inputSource1::updateGenerators,
            inputSource2::updateGenerators
        );



        var comparisonChecksum = new ComparisonChecksum();
        comparisonGenerators.decodeAndCollect(
            comparisonChecksum::setChecksum1,
            comparisonChecksum::setChecksum2
        );

        return comparisonChecksum;
    }
}
