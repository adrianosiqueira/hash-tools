package hashtools.core.service;

import hashtools.core.model.HashAlgorithm;
import hashtools.core.model.Sample;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class SampleService {

    public Optional<Sample> createSampleFromAlgorithm(HashAlgorithm algorithm) {
        return Optional.ofNullable(algorithm)
                       .map(a -> {
                           Sample sample = new Sample();
                           sample.setAlgorithm(a);
                           return sample;
                       });
    }

    public List<Sample> createSampleList(String officialData) {
        return new FileService().stringIsFilePath(officialData)
               ? createList(Path.of(officialData))
               : createList(officialData);
    }

    private Sample createFromAlgorithmAndOfficialHash(HashAlgorithm algorithm, String hash) {
        Sample sample = new Sample();
        sample.setAlgorithm(algorithm);
        sample.setOfficialHash(hash);

        return sample;
    }

    private Optional<Sample> createFromHash(String hash) {
        return Optional.ofNullable(hash)
                       .map(String::length)
                       .flatMap(new HashAlgorithmService()::searchByLength)
                       .map(algorithm -> createFromAlgorithmAndOfficialHash(algorithm, hash));
    }

    private List<Sample> createList(Path path) {
        List<String> lines = new ArrayList<>();

        try {
            lines.addAll(Files.readAllLines(path));
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
        }

        return lines.stream()
                    .map(line -> line.split(" "))
                    .flatMap(Stream::of)
                    .map(this::createFromHash)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .toList();
    }

    private List<Sample> createList(String hash) {
        return Optional.ofNullable(hash)
                       .map(this::createFromHash)
                       .filter(Optional::isPresent)
                       .map(Optional::get)
                       .map(List::of)
                       .orElse(new ArrayList<>());
    }
}