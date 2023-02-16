package release.tracker.api.repositories.interfaces;

import release.tracker.api.model.domain.Release;

import java.util.List;
import java.util.Map;

public interface IReleaseRepository {
    List<Release> retrieve();

    Release getById(int releaseId);

    Release insert(Release releaseTable);

    int update(Release releaseTable);

    void delete(int releaseId);

    Map<Integer, List<Release>> filter(String releaseDate, String lastUpdateDate, String createdDate, String releaseStatus, String description, String name,
                                       int page, int recordsToTake);
}
