package hashtools.domain.commom;

public sealed interface Result<RESULT_TYPE, ERROR_TYPE> {

    RESULT_TYPE getValue() throws IllegalStateException;

    ERROR_TYPE getError() throws IllegalStateException;

    boolean isOk();

    boolean isError();



    record Ok<RESULT_TYPE, ERROR_TYPE>(RESULT_TYPE value) implements Result<RESULT_TYPE, ERROR_TYPE> {
        @Override
        public RESULT_TYPE getValue() throws IllegalStateException {
            return value;
        }

        @Override
        public ERROR_TYPE getError() throws IllegalStateException {
            throw new IllegalStateException("Cannot get error from Ok result");
        }

        @Override
        public boolean isOk() {
            return true;
        }

        @Override
        public boolean isError() {
            return false;
        }
    }



    record Error<RESULT_TYPE, ERROR_TYPE>(ERROR_TYPE error) implements Result<RESULT_TYPE, ERROR_TYPE> {
        @Override
        public RESULT_TYPE getValue() throws IllegalStateException {
            throw new IllegalStateException("Cannot get value from Error result");
        }

        @Override
        public ERROR_TYPE getError() throws IllegalStateException {
            return error;
        }

        @Override
        public boolean isOk() {
            return false;
        }

        @Override
        public boolean isError() {
            return true;
        }
    }
}
