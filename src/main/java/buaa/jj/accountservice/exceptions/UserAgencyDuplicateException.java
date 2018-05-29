package buaa.jj.accountservice.exceptions;

public class UserAgencyDuplicateException extends RegistryException {
    public UserAgencyDuplicateException() {
        super();
    }

    public UserAgencyDuplicateException(String message) {
        super(message);
    }
}
