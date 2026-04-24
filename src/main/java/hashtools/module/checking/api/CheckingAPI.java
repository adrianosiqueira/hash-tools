package hashtools.module.checking.api;

import hashtools.core.model.InputValidationException;
import hashtools.module.checking.model.CheckingContext;
import hashtools.module.checking.model.CheckingResult;
import hashtools.module.checking.model.CheckingScreenInput;
import hashtools.module.checking.service.CheckingService;

public class CheckingAPI {

    private CheckingService checkingService;



    public CheckingAPI() {
        this.checkingService = new CheckingService();
    }



    public void requestInputValidation(CheckingScreenInput input) throws InputValidationException {
        checkingService.performInputValidation(input);
    }

    public CheckingResult requestChecksumChecking(CheckingContext context) throws Exception {
        return checkingService.performChecksumChecking(context);
    }

    public String requestResultFormatting(CheckingResult result) throws Exception {
        return checkingService.performResultFormatting(result);
    }
}
