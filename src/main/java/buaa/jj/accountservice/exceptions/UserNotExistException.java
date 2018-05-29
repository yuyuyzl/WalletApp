package buaa.jj.accountservice.exceptions;

public class UserNotExistException extends AccountServiceException {
    public UserNotExistException() {
        super();
    }

    public UserNotExistException(String message) {
        super(message);
    }
}
