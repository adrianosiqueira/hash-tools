package hashtools.domain.commom;

public sealed interface Option<VALUE_TYPE> {

    VALUE_TYPE getValue() throws IllegalStateException;

    boolean isSome();

    boolean isNone();



    record Some<VALUE_TYPE>(VALUE_TYPE value) implements Option<VALUE_TYPE> {
        @Override
        public VALUE_TYPE getValue() {
            return value;
        }

        @Override
        public boolean isSome() {
            return true;
        }

        @Override
        public boolean isNone() {
            return false;
        }
    }



    record None<VALUE_TYPE>() implements Option<VALUE_TYPE> {
        @Override
        public VALUE_TYPE getValue() throws IllegalStateException {
            throw new IllegalStateException("Cannot get value from None option");
        }

        @Override
        public boolean isSome() {
            return false;
        }

        @Override
        public boolean isNone() {
            return true;
        }
    }
}
