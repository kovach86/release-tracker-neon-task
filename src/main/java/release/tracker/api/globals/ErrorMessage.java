package release.tracker.api.globals;

import release.tracker.api.model.enums.ReleaseStatus;

public class ErrorMessage {
    public static final String RELEASE_DATE_BEFORE_CREATED = "Release date should be after %s";
    public static final String DATE_WRONG_FORMAT = "release date is not in correct format, use yyyy-MM-dd";
    public static final String NO_RELEASE_FOUND = "Release with id %s does not exist";
    public static final String INVALID_RELEASE_STATUS = "Release status name is invalid, it should be one from the following: " + ReleaseStatus.getNames();
    public static final String STATUS_ALREADY_CREATE = "Release status is already Created";

}
