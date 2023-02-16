package release.tracker.api.exceptions;

public class InvalidReleaseStatusException extends Exception {
    public InvalidReleaseStatusException(String message) {
        super(message);
    }
}
