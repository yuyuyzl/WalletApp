package buaa.jj.accountservice.exceptions;

public class AccountServiceException extends RuntimeException {
    public AccountServiceException() {
        super();
    }

    public AccountServiceException(String message) {
        super(message);
    }
}
