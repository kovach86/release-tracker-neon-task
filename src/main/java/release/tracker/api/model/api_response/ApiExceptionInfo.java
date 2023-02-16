package release.tracker.api.model.api_response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import release.tracker.api.utilities.DateTimeUtil;

import java.util.Date;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiExceptionInfo extends BaseResponseModel {

    public String endpointMethod;
    private String dateTime;

    public ApiExceptionInfo(String description) {
        super(description);
        dateTime = DateTimeUtil.parseDate(new Date());
    }
}
