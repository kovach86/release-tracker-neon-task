package release.tracker.api.services.interfaces;


import release.tracker.api.exceptions.DateNotValidException;
import release.tracker.api.exceptions.InvalidReleaseStatusException;
import release.tracker.api.exceptions.RecordNotFoundException;
import release.tracker.api.model.api_response.PaginationResponse;
import release.tracker.api.model.dto.ReleaseDto;

import java.util.List;

public interface IReleaseService {
    List<ReleaseDto> getAllReleases();

    ReleaseDto getReleaseById(int releaseId) throws RecordNotFoundException;

    void insertRelease(ReleaseDto releaseDto) throws Exception;

    int updateRelease(int releaseId, ReleaseDto releaseDto) throws DateNotValidException, RecordNotFoundException, InvalidReleaseStatusException;

    void deleteRelease(int releaseId) throws RecordNotFoundException;

    PaginationResponse filterRelease(String releaseDate, String lastUpdateDate, String createdDate, String releaseStatus, String description, String name, int page, int recordsToTake);
}
