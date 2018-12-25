package buaa.jj.accountservice.exceptions;

public class NameDuplicateException extends RegistryException {
    public NameDuplicateException() {
        super();
    }

    public NameDuplicateException(String message) {
        super(message);
    }
}
