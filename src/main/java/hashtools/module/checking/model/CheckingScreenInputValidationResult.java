package hashtools.module.checking.model;

public class CheckingScreenInputValidationResult {

    private boolean isValid;
    private String message;



    private CheckingScreenInputValidationResult(boolean isValid, String message) {
        this.isValid = isValid;
        this.message = message;
    }



    public static CheckingScreenInputValidationResult issue(String message) {
        return new CheckingScreenInputValidationResult(false, message);
    }

    public static CheckingScreenInputValidationResult valid() {
        return new CheckingScreenInputValidationResult(true, "");
    }



    public boolean isValid() {
        return isValid;
    }

    public String getMessage() {
        return message;
    }
}
