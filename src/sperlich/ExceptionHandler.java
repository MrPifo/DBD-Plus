package sperlich;

public class ExceptionHandler extends RuntimeException {
    private static final long serialVersionUID = 1L;
    public ExceptionHandler(String error) {
        super(error);
    }
}
