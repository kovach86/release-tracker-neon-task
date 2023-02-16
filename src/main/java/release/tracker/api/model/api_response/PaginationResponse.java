package release.tracker.api.model.api_response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PaginationResponse extends BaseResponseModel {
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private int currentPage;
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private int totalPages;
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private int recordsPerPage;
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private int allRecords;
    @JsonProperty("result")
    private Object filteredRecords;

    public PaginationResponse(String message, int currentPage, int totalPages, int recordsPerPage, int allRecords, Object resultList) {
        super(message);
        this.currentPage = currentPage;
        this.totalPages = totalPages;
        this.recordsPerPage = recordsPerPage;
        this.allRecords = allRecords;
        this.filteredRecords = resultList;
    }

    public PaginationResponse(String description) {
        super(description);
        currentPage = 0;
        totalPages = 0;
        recordsPerPage = 0;
        allRecords = 0;
    }
}
