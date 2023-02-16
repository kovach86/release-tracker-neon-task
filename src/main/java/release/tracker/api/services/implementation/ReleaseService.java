package release.tracker.api.services.implementation;

import release.tracker.api.exceptions.DateNotValidException;
import release.tracker.api.exceptions.InvalidReleaseStatusException;
import release.tracker.api.exceptions.RecordNotFoundException;
import release.tracker.api.globals.ErrorMessage;
import release.tracker.api.model.api_response.PaginationResponse;
import release.tracker.api.model.domain.Release;
import release.tracker.api.model.dto.ReleaseDto;
import release.tracker.api.model.enums.ReleaseStatus;
import release.tracker.api.model.mapper.ReleaseMapperProfile;
import release.tracker.api.repositories.interfaces.IReleaseRepository;
import release.tracker.api.services.interfaces.IReleaseService;
import release.tracker.api.utilities.DateTimeUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class ReleaseService implements IReleaseService {

    IReleaseRepository _releaseRepository;
    ReleaseMapperProfile _mapper;
    private static final Logger logger = LogManager.getLogger(ReleaseService.class);

    public ReleaseService(IReleaseRepository _releaseRepository) {
        this._releaseRepository = _releaseRepository;
        this._mapper = new ReleaseMapperProfile();
    }

    @Override
    public List<ReleaseDto> getAllReleases() {
        var listOfReleases = _releaseRepository.retrieve();
        return _mapper.mapToReleaseDtoList(listOfReleases);
    }

    @Override
    public ReleaseDto getReleaseById(int releaseId) throws RecordNotFoundException {
        var release = _releaseRepository.getById(releaseId);
        if (release == null)
            throw new RecordNotFoundException(String.format(ErrorMessage.NO_RELEASE_FOUND, releaseId));
        return _mapper.mapToReleaseDto(release);
    }

    @Override
    public void insertRelease(ReleaseDto releaseDto) throws Exception {
        var releaseEntity = _mapper.mapToRelease(releaseDto);
        releaseEntity.setCreatedAt(new Date());
        releaseEntity.setStatus(ReleaseStatus.CREATED.getName());

        if (releaseEntity.getReleaseDate().before(releaseEntity.getCreatedAt()))
            throw new DateNotValidException(String.format(ErrorMessage.RELEASE_DATE_BEFORE_CREATED, DateTimeUtil.parseDate(new Date(), "yyyy-MM-dd")));

        _releaseRepository.insert(releaseEntity);
    }

    @Override
    public int updateRelease(int releaseId, ReleaseDto releaseDto) throws DateNotValidException, RecordNotFoundException, InvalidReleaseStatusException {
        var existingReleaseFromDb = _releaseRepository.getById(releaseId);

        if (existingReleaseFromDb == null) {
            throw new RecordNotFoundException(String.format(ErrorMessage.NO_RELEASE_FOUND, releaseId));
        }

        if (releaseDto.getReleaseStatus() != null) {
            if (!ReleaseStatus.getNames().contains(releaseDto.getReleaseStatus()))
                throw new InvalidReleaseStatusException(ErrorMessage.INVALID_RELEASE_STATUS);

            var releaseDtoStatus = ReleaseStatus.valueOf(releaseDto.getReleaseStatus());
            var releaseDbStatus = ReleaseStatus.getReleaseStatusByName(existingReleaseFromDb.getStatus());
            //do not allow for release status to be CREATED during update
            if (releaseDbStatus != ReleaseStatus.CREATED && releaseDtoStatus == ReleaseStatus.CREATED)
                throw new InvalidReleaseStatusException(ErrorMessage.STATUS_ALREADY_CREATE);

            existingReleaseFromDb.setStatus(releaseDtoStatus.getName());
        }
        Date releaseDateUpdateValue = DateTimeUtil.parseDateFromString(releaseDto.getReleaseDate());
        if (releaseDateUpdateValue.before(existingReleaseFromDb.getCreatedAt()))
            throw new DateNotValidException(String.format(ErrorMessage.RELEASE_DATE_BEFORE_CREATED, existingReleaseFromDb.getCreatedAt()));

        existingReleaseFromDb.setLastUpdateAt(new Date());
        existingReleaseFromDb.setName(releaseDto.getReleaseName());
        existingReleaseFromDb.setDescription(releaseDto.getReleaseDescription());
        existingReleaseFromDb.setReleaseDate(releaseDateUpdateValue);
        return _releaseRepository.update(existingReleaseFromDb);

    }

    @Override
    public void
    deleteRelease(int releaseId) throws RecordNotFoundException {
        if (_releaseRepository.getById(releaseId) == null) {
            throw new RecordNotFoundException(String.format(ErrorMessage.NO_RELEASE_FOUND, releaseId));
        }
        _releaseRepository.delete(releaseId);

    }

    @Override
    public PaginationResponse filterRelease(String releaseDate, String lastUpdateDate, String createdDate, String releaseStatus, String description, String name, int page, int recordsToTake) {
        Map<Integer, List<Release>> result = _releaseRepository.filter(releaseDate, lastUpdateDate, createdDate, releaseStatus, description, name, page, recordsToTake);
        var firstEntry = result.keySet().stream().findFirst();
        if (firstEntry.isPresent()) {
            int filteredRecordsCount = result.keySet().stream().findFirst().get();
            int pageCount = filteredRecordsCount % recordsToTake > 0 ? filteredRecordsCount / recordsToTake + 1 : filteredRecordsCount / recordsToTake;
            var filteredReleaseList = result.values().stream().findFirst().get();
            return new PaginationResponse("filtered records retrieved", page, pageCount, recordsToTake, filteredRecordsCount, filteredReleaseList);
        }
        return new PaginationResponse("No releases with specified filter");
    }
}
