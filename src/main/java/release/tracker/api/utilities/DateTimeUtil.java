package release.tracker.api.utilities;

import release.tracker.api.exceptions.DateNotValidException;
import release.tracker.api.globals.ErrorMessage;
import org.apache.commons.validator.GenericValidator;
import org.apache.http.client.utils.DateUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTimeUtil {

    public static String parseDate(Date date) {
        try {
            SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return dateTimeFormat.format(date);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static String parseDate(Date date, String pattern) {
        try {
            SimpleDateFormat dateTimeFormat = new SimpleDateFormat(pattern);
            return dateTimeFormat.format(date);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static Date parseDateFromString(String date) throws DateNotValidException {
        if (!GenericValidator.isDate(date, "yyyy-MM-dd", true))
            throw new DateNotValidException(ErrorMessage.DATE_WRONG_FORMAT);

        return DateUtils.parseDate(date, new String[]{"yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd"});
    }
}
