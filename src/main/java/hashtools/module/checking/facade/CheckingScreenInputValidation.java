package hashtools.module.checking.facade;

import hashtools.module.checking.model.CheckingScreenInput;
import hashtools.module.checking.model.CheckingScreenInputValidationResult;

import java.nio.file.Files;
import java.nio.file.Path;

public class CheckingScreenInputValidation {

    public CheckingScreenInputValidationResult validate(CheckingScreenInput input) {
        if (input == null) {
            return CheckingScreenInputValidationResult.issue("The screen input is null.");
        }



        if (input.isUsingInputFile()) {
            Path file = input.getInputFile();

            if (!Files.isRegularFile(file)) {
                return CheckingScreenInputValidationResult.issue("The input is not a file.");
            }
        }



        if (input.isUsingChecksumFile()) {
            boolean isChecksumFile = input.checksumFileHasValidExtension();

            if (!isChecksumFile) {
                return CheckingScreenInputValidationResult.issue("The file is not a checksum file.");
            }



            Path file = input.getChecksumFile();

            if (Files.notExists(file)) {
                return CheckingScreenInputValidationResult.issue("The checksum file does not exist.");
            }
        }



        return CheckingScreenInputValidationResult.valid();
    }
}
