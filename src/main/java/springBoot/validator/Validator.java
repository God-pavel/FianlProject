package springBoot.validator;

public interface Validator<T> {

    Result validate(T value);
}
