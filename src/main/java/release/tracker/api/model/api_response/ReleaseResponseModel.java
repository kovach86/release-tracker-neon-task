package release.tracker.api.model.api_response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReleaseResponseModel extends BaseResponseModel {

    @JsonProperty("result")
    private Object entity;

    public ReleaseResponseModel(String msgDescription, Object entity) {
        super(msgDescription);
        this.entity = entity;
        description = msgDescription;
    }

    public ReleaseResponseModel(String msgDescription) {
        super(msgDescription);
    }
}
