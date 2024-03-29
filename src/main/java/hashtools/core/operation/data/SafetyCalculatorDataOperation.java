package hashtools.core.operation.data;

import hashtools.core.model.Data;
import hashtools.core.model.Hash;

import java.util.List;

/**
 * <p>
 * Calculates the safety percentage of the input data, comparing
 * the official hash checksums with the generated ones.
 * </p>
 */
public class SafetyCalculatorDataOperation implements DataOperation {

    @Override
    public void perform(Data data) {
        List<Hash> hashes = data.getHashes();

        long matches = hashes.stream()
                             .map(Hash::matches)
                             .filter(b -> b)
                             .count();

        data.setSafetyPercentage(matches * 100.0 / hashes.size());
    }
}
