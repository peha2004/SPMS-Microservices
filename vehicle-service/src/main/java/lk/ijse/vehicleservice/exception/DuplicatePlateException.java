package lk.ijse.vehicleservice.exception;

public class DuplicatePlateException extends RuntimeException {
    public DuplicatePlateException(String message) {
        super(message);
    }
}