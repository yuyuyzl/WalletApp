package buaa.jj.accountservice.exceptions;

public class AgencyNotExistException extends RegistryException {
    public AgencyNotExistException() {
        super();
    }

    public AgencyNotExistException(String message) {
        super(message);
    }
}
