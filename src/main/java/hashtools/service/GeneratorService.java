package hashtools.service;

import hashtools.domain.algorithm.MessageDigestProxy;
import hashtools.domain.container.ChecksumGenerationContainer;
import hashtools.domain.context.ChecksumGenerationContext;
import hashtools.domain.result.ChecksumGenerationResult;

import java.io.IOException;
import java.security.MessageDigest;
import java.util.Collection;
import java.util.List;

public class GeneratorService {

    public ChecksumGenerationContainer performChecksumGeneration(ChecksumGenerationContext context) {
        // Problem detection
        String problem = context
            .detectProblem()
            .orElse(null);

        if (problem != null) {
            return ChecksumGenerationContainer.problem(problem);
        }



        // Processing data
        List<MessageDigestProxy> messageDigestProxies = context
            .getAlgorithms()
            .stream()
            .map(MessageDigestProxy::fromAlgorithm)
            .toList();

        Collection<MessageDigest> messageDigests = messageDigestProxies
            .stream()
            .map(MessageDigestProxy::messageDigest)
            .toList();



        try {
            // Processing
            context.updateMessageDigests(messageDigests);



            // Result collecting
            ChecksumGenerationResult result = new ChecksumGenerationResult();
            result.setIdentification(context::getIdentification);

            messageDigestProxies
                .stream()
                .map(MessageDigestProxy::decodeIntoChecksum)
                .forEach(result::addChecksum);

            return ChecksumGenerationContainer.result(result);
        } catch (IOException e) {
            return ChecksumGenerationContainer.exception(e);
        }
    }
}
