package hashtools.module.checking.service;

import hashtools.module.checking.facade.CheckingResultFormatting;
import hashtools.module.checking.facade.CheckingScreenInputValidation;
import hashtools.module.checking.facade.ChecksumChecking;
import hashtools.module.checking.model.CheckingContext;
import hashtools.module.checking.model.CheckingResult;
import hashtools.module.checking.model.CheckingScreenInput;
import hashtools.module.checking.model.InputValidationResult;

public class CheckingService {

    public CheckingService() {
    }



    public InputValidationResult performInputValidation(CheckingScreenInput input) {
        CheckingScreenInputValidation inputValidation = new CheckingScreenInputValidation();
        return inputValidation.perform(input);
    }

    public CheckingResult performChecksumChecking(CheckingContext context) throws Exception {
        ChecksumChecking checksumChecking = new ChecksumChecking();
        return checksumChecking.perform(context);
    }

    public String performResultFormatting(CheckingResult result) throws Exception {
        CheckingResultFormatting resultFormatting = new CheckingResultFormatting();
        return resultFormatting.format(result);
    }
}
