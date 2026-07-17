package hashtools.service;

import hashtools.domain.algorithm.Algorithm;
import hashtools.domain.algorithm.MessageDigestProxy;
import hashtools.domain.checksum.Checksum;
import hashtools.domain.checksum.ComparatorChecksum;
import hashtools.domain.container.ChecksumComparisonContainer;
import hashtools.domain.context.ChecksumComparisonContext;
import hashtools.domain.result.ChecksumComparisonResult;
import hashtools.strategy.thread.VirtualThreadFactory;

import java.io.IOException;
import java.security.MessageDigest;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadFactory;

public class ComparatorService {

    private ChecksumComparisonContext context;



    public ChecksumComparisonContainer performChecksumComparison(ChecksumComparisonContext context) {
        this.context = context;



        // Problem detection
        String problem = context
            .detectProblem()
            .orElse(null);

        if (problem != null) {
            return ChecksumComparisonContainer.problem(problem);
        }



        // Processing data
        CompletableFuture<Checksum> checksum1 = new CompletableFuture<>();
        CompletableFuture<Checksum> checksum2 = new CompletableFuture<>();
        ThreadFactory threadFactory = new VirtualThreadFactory();



        // Processing
        threadFactory
            .newThread(() -> this.generateChecksum1(checksum1))
            .start();

        threadFactory
            .newThread(() -> this.generateChecksum2(checksum2))
            .start();



        try {
            // Result collecting
            ComparatorChecksum checksum = new ComparatorChecksum();
            checksum.setChecksum1(checksum1.get());
            checksum.setChecksum2(checksum2.get());

            ChecksumComparisonResult result = new ChecksumComparisonResult();
            result.setChecksum(checksum);

            return ChecksumComparisonContainer.result(result);
        } catch (Exception e) {
            return ChecksumComparisonContainer.exception(e);
        }
    }



    private void generateChecksum1(CompletableFuture<Checksum> checksum1) {
        Collection<MessageDigestProxy> messageDigestProxies = context
            .getAlgorithms()
            .stream()
            .map(Algorithm::createMessageDigestProxy)
            .toList();

        List<MessageDigest> messageDigests = messageDigestProxies
            .stream()
            .map(MessageDigestProxy::messageDigest)
            .toList();

        try {
            context.updateMessageDigests1(messageDigests);

            Checksum checksum = messageDigestProxies
                .stream()
                .map(MessageDigestProxy::decodeIntoChecksum)
                .toList()
                .getFirst();

            checksum1.complete(checksum);
        } catch (IOException e) {
            checksum1.completeExceptionally(e);
        }
    }

    private void generateChecksum2(CompletableFuture<Checksum> checksum2) {
        Collection<MessageDigestProxy> messageDigestProxies = context
            .getAlgorithms()
            .stream()
            .map(Algorithm::createMessageDigestProxy)
            .toList();

        List<MessageDigest> messageDigests = messageDigestProxies
            .stream()
            .map(MessageDigestProxy::messageDigest)
            .toList();

        try {
            context.updateMessageDigests2(messageDigests);

            Checksum checksum = messageDigestProxies
                .stream()
                .map(MessageDigestProxy::decodeIntoChecksum)
                .toList()
                .getFirst();

            checksum2.complete(checksum);
        } catch (IOException e) {
            checksum2.completeExceptionally(e);
        }
    }
}
