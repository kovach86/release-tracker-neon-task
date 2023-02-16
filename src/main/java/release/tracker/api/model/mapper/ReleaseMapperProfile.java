package release.tracker.api.model.mapper;

import release.tracker.api.exceptions.DateNotValidException;
import release.tracker.api.model.domain.Release;
import release.tracker.api.model.dto.ReleaseDto;
import release.tracker.api.model.enums.ReleaseStatus;
import release.tracker.api.utilities.DateTimeUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class ReleaseMapperProfile {

    public ReleaseDto mapToReleaseDto(Release release) {
        var dto = new ReleaseDto();
        dto.setId(release.getId());
        dto.setReleaseName(release.getName());
        dto.setReleaseDescription(release.getDescription());
        dto.setReleaseStatus(ReleaseStatus.getReleaseStatusByName(release.getStatus()).getName());
        dto.setLastUpdateAt(DateTimeUtil.parseDate(release.getCreatedAt()));
        dto.setCreatedAt(DateTimeUtil.parseDate(release.getCreatedAt()));
        dto.setReleaseDate(DateTimeUtil.parseDate(release.getReleaseDate(), "yyyy-MM-dd"));
        return dto;
    }

    public Release mapToRelease(ReleaseDto releaseDto) throws DateNotValidException {
        var releaseEntity = new Release();
        releaseEntity.setName(releaseDto.getReleaseName());
        releaseEntity.setDescription(releaseDto.getReleaseDescription());
        releaseEntity.setReleaseDate(DateTimeUtil.parseDateFromString(releaseDto.getReleaseDate()));
        releaseEntity.setLastUpdateAt(new Date());
        return releaseEntity;
    }


    public List<ReleaseDto> mapToReleaseDtoList(List<Release> releases) {
        List<ReleaseDto> list = new ArrayList<ReleaseDto>();
        for (Release release : releases) {
            list.add(mapToReleaseDto(release));
        }
        return list;
    }

    private List<ReleaseDto> mapToReleaseEntity(List<ReleaseDto> releaseDtos) {
        return null;
    }
}
