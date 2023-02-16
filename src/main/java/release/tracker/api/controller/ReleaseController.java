package release.tracker.api.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import release.tracker.api.exceptions.DateNotValidException;
import release.tracker.api.exceptions.InvalidReleaseStatusException;
import release.tracker.api.exceptions.RecordNotFoundException;
import release.tracker.api.globals.ErrorMessage;
import release.tracker.api.globals.SuccessMessage;
import release.tracker.api.model.api_response.PaginationResponse;
import release.tracker.api.model.api_response.ReleaseResponseModel;
import release.tracker.api.model.dto.ReleaseDto;
import release.tracker.api.services.interfaces.IReleaseService;
import release.tracker.api.utilities.DateTimeUtil;

@RestController
@RequestMapping("/releases")
public class ReleaseController {

    IReleaseService _releaseService;

    public ReleaseController(IReleaseService _releaseService) {
        this._releaseService = _releaseService;
    }

    @GetMapping
    public ResponseEntity<ReleaseResponseModel> getAllReleases() {

        var list = _releaseService.getAllReleases();
        if (list.isEmpty()) {
            return new ResponseEntity<>(new ReleaseResponseModel("No current releases"), HttpStatus.OK);
        }
        var responseModel = new ReleaseResponseModel("Retrieved list of releases", list);
        return new ResponseEntity<>(responseModel, HttpStatus.OK);
    }

    @GetMapping("/{releaseId}")
    public ResponseEntity<ReleaseResponseModel> getSingleRelease(@PathVariable int releaseId) {
        try {
            var responseModel = new ReleaseResponseModel("Retrieving information about release", _releaseService.getReleaseById(releaseId));
            return new ResponseEntity<>(responseModel, HttpStatus.OK);

        } catch (RecordNotFoundException exc) {
            return new ResponseEntity<>(new ReleaseResponseModel(exc.getMessage()), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/filter")
    public ResponseEntity<PaginationResponse> getReleaseByParams(@RequestParam(required = false, defaultValue = "0001-01-01") String releaseDate,
                                                                 @RequestParam(required = false, value = "") String lastUpdateDate,
                                                                 @RequestParam(required = false, value = "") String createdAt,
                                                                 @RequestParam(required = false, value = "") String releaseStatus, @RequestParam(required = false) String description,
                                                                 @RequestParam(required = false) String name,
                                                                 @RequestParam(required = false, defaultValue = "1") Integer page,
                                                                 @RequestParam(required = false, defaultValue = "10") Integer recordsToTake) {

        try {
            if (!createdAt.isEmpty())
                DateTimeUtil.parseDateFromString(createdAt);
            if (!lastUpdateDate.isEmpty())
                DateTimeUtil.parseDateFromString(releaseDate);
            if (!lastUpdateDate.isEmpty())
                DateTimeUtil.parseDateFromString(lastUpdateDate);
        } catch (DateNotValidException e) {
            return new ResponseEntity<>(new PaginationResponse(ErrorMessage.DATE_WRONG_FORMAT), HttpStatus.BAD_REQUEST);
        }

        var paginationResponse = _releaseService.filterRelease(releaseDate, lastUpdateDate, createdAt, releaseStatus, description, name, page, recordsToTake);
        if (paginationResponse.getRecordsPerPage() > 0)
            return new ResponseEntity<>(paginationResponse, HttpStatus.OK);

        return new ResponseEntity<>(paginationResponse, HttpStatus.NO_CONTENT);
    }

    @PostMapping(produces = {"application/json"}, consumes = {"application/json"})
    public ResponseEntity<ReleaseResponseModel> insertRelease(@RequestBody @Valid ReleaseDto releaseDto) throws Exception {
        try {
            _releaseService.insertRelease(releaseDto);
            return new ResponseEntity<>(new ReleaseResponseModel(String.format(SuccessMessage.NEW_ENTRY_INSERTED, "release")), HttpStatus.CREATED);

        } catch (DateNotValidException exc) {
            return new ResponseEntity<>(new ReleaseResponseModel(exc.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping(value = "/{releaseId}", produces = {"application/json"}, consumes = {"application/json"})
    public ResponseEntity<ReleaseResponseModel> updateRelease(@PathVariable int releaseId, @RequestBody @Valid ReleaseDto releaseDto) {
        try {
            _releaseService.updateRelease(releaseId, releaseDto);
            return new ResponseEntity<>(new ReleaseResponseModel(String.format(SuccessMessage.ENTRY_UPDATED, "release")), HttpStatus.OK);

        } catch (DateNotValidException | InvalidReleaseStatusException exc) {
            return new ResponseEntity<>(new ReleaseResponseModel(exc.getMessage()), HttpStatus.BAD_REQUEST);
        } catch (RecordNotFoundException exc) {
            return new ResponseEntity<>(new ReleaseResponseModel(exc.getMessage()), HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping(value = "/{releaseId}", produces = {"application/json"}, consumes = {"application/json"})
    public ResponseEntity<ReleaseResponseModel> delete(@PathVariable int releaseId) {
        try {
            _releaseService.deleteRelease(releaseId);
            return new ResponseEntity<>(new ReleaseResponseModel("release deleted"), HttpStatus.OK);
        } catch (RecordNotFoundException exc) {
            return new ResponseEntity<>(new ReleaseResponseModel(exc.getMessage()), HttpStatus.NOT_FOUND);
        }
    }
}
