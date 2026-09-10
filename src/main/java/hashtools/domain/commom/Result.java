package hashtools.domain.commom;

public class Result<R, E> {

    private R value;
    private E error;

    private boolean isError;



    private Result() {
    }



    public static <R, E> Result<R, E> ok(R value) {
        var result = new Result<R, E>();
        result.value = value;
        result.error = null;
        result.isError = false;

        return result;
    }

    public static <R, E> Result<R, E> error(E error) {
        var result = new Result<R, E>();
        result.value = null;
        result.error = error;
        result.isError = true;

        return result;
    }



    public R getValue() {
        return value;
    }

    public E getError() {
        return error;
    }

    public boolean isError() {
        return isError;
    }
}
