package emengjzs.dacon.context;

public class NoSuchDataException extends NullPointerException {

    private static final long serialVersionUID = 1L;

    public NoSuchDataException(String name) {
        super("Data [" + name + "]" + " not found in dataContext");
    }
}
