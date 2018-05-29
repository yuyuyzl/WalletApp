package buaa.jj.accountservice.exceptions;

public class UserFrozenException extends AccountServiceException {
    public UserFrozenException() {
        super();
    }

    public UserFrozenException(String message) {
        super(message);
    }
}
