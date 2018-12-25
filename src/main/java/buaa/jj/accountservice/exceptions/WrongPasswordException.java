package buaa.jj.accountservice.exceptions;

public class WrongPasswordException extends AccountServiceException {
    public WrongPasswordException() {
        super();
    }

    public WrongPasswordException(String message) {
        super(message);
    }
}
