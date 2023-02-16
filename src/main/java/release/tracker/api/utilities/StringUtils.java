package release.tracker.api.utilities;

public class StringUtils {
    public static boolean isStringNullOrEmpty(String s) {
        return s == null || s.trim().equals("");
    }
}
