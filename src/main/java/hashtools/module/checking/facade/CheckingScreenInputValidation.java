package hashtools.module.checking.facade;

import hashtools.module.checking.model.CheckingScreenInput;
import hashtools.module.checking.model.InputValidationResult;

import java.nio.file.Files;
import java.nio.file.Path;

public class CheckingScreenInputValidation {

    public InputValidationResult perform(CheckingScreenInput input) throws IllegalArgumentException {
        if (input == null) {
            throw new IllegalArgumentException("The screen input is null.");
        }



        InputValidationResult validationResult = new InputValidationResult();



        if (input.isUsingInputFile()) {
            Path file = input.getInputFile();

            if (!Files.isRegularFile(file)) {
                validationResult.addProblem("The input is not a file.");
            }
        }



        if (input.isUsingChecksumFile()) {
            boolean isChecksumFile = input.checksumFileHasValidExtension();

            if (!isChecksumFile) {
                validationResult.addProblem("The file is not a checksum file.");
            }



            Path file = input.getChecksumFile();

            if (Files.notExists(file)) {
                validationResult.addProblem("The checksum file does not exist.");
            }
        }



        return validationResult;
    }
}
