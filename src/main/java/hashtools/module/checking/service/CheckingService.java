package hashtools.module.checking.service;

import hashtools.module.checking.facade.CheckingResultFormatting;
import hashtools.module.checking.facade.ChecksumChecking;
import hashtools.module.checking.model.CheckingContext;
import hashtools.module.checking.model.CheckingResult;

import java.io.Closeable;

public class CheckingService implements Closeable {

    public CheckingService() {
    }



    @Override
    @Deprecated(forRemoval = true)
    public void close() {
    }



    public CheckingResult performChecksumChecking(CheckingContext context) throws Exception {
        ChecksumChecking checksumChecking = new ChecksumChecking();
        return checksumChecking.perform(context);
    }

    public String performResultFormatting(CheckingResult result) {
        CheckingResultFormatting resultFormatting = new CheckingResultFormatting();
        return resultFormatting.format(result);
    }
}
