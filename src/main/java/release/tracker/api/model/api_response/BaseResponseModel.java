package release.tracker.api.model.api_response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BaseResponseModel {
    protected String description;

    public BaseResponseModel(String description) {
        this.description = description;
    }
}
