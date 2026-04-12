package hashtools.core.strategy.formatter;

public interface Formatter<T, R> {

    R format(T t);
}
