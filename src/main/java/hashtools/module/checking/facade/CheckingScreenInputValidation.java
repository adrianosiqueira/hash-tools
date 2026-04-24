package hashtools.module.checking.facade;

import hashtools.core.model.InputValidationException;
import hashtools.module.checking.model.CheckingScreenInput;

import java.nio.file.Files;
import java.nio.file.Path;

public class CheckingScreenInputValidation {

    public void validate(CheckingScreenInput input) throws InputValidationException {
        if (input == null) {
            throw new InputValidationException("The screen input is null.");
        }



        if (input.isUsingInputFile()) {
            Path file = input.getInputFile();

            if (!Files.isRegularFile(file)) {
                throw new InputValidationException("The input is not a file.");
            }
        }



        if (input.isUsingChecksumFile()) {
            boolean isChecksumFile = input.checksumFileHasValidExtension();

            if (!isChecksumFile) {
                throw new InputValidationException("The file is not a checksum file.");
            }



            Path file = input.getChecksumFile();

            if (Files.notExists(file)) {
                throw new InputValidationException("The checksum file does not exist.");
            }
        }
    }
}
