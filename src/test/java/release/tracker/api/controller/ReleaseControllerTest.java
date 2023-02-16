package release.tracker.api.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import release.tracker.api.globals.ErrorMessage;
import release.tracker.api.model.api_response.ReleaseResponseModel;
import release.tracker.api.model.dto.ReleaseDto;
import release.tracker.api.model.dto.UserInfo;
import release.tracker.api.utilities.DateTimeUtil;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ReleaseControllerTest {

    @Autowired
    private TestRestTemplate testRestTemplate;
    private ReleaseDto malformedBodyDto;
    private HttpHeaders headers;
    ObjectMapper mapper;
    private final String formattedDate = DateTimeUtil.parseDate(new Date(), "yyyy-MM-dd");
    @Value("${firebase.user}")
    private String userEmail;
    @Value("${firebase.pass}")
    private String password;
    @Value("${firebase.authenticate.url}")
    private String firebaseUrl;
    @Value("${firebase.authenticate.key}")
    private String firebaseApikey;

    int releaseIdForUpdate = 0;

    @BeforeAll
    public void setUp() throws JsonProcessingException {
        mapper = new ObjectMapper();

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime faultyDate = LocalDateTime.now().minusDays(3);
        String dateString = dtf.format(faultyDate);

        malformedBodyDto = new ReleaseDto();
        malformedBodyDto.setReleaseName("mailformed name");
        malformedBodyDto.setReleaseDescription("mailformed description");
        malformedBodyDto.setReleaseDate(dateString);
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(createBearerForTestUser());
    }

    @Test
    @Order(1)
    public void getReleases() {
        var entity = new HttpEntity<>("", headers);
        ResponseEntity<ReleaseResponseModel> response = testRestTemplate.exchange("/releases", HttpMethod.GET, entity, ReleaseResponseModel.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @Order(2)
    public void insertInvalidRelease_withReleaseDateBeforeTodayDate() {

        var entity = new HttpEntity<>(malformedBodyDto, headers);

        ResponseEntity<ReleaseResponseModel> response = testRestTemplate.exchange("/releases", HttpMethod.POST, entity, ReleaseResponseModel.class);
        String expectedMsg = String.format(ErrorMessage.RELEASE_DATE_BEFORE_CREATED, formattedDate);
        assertEquals(expectedMsg, response.getBody().getDescription());
    }

    @Test
    @Order(3)
    public void updateRelease_withNoExistingReleaseStatus() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime faultyDate = LocalDateTime.now().minusDays(3);
        String dateString = dtf.format(faultyDate);

        malformedBodyDto.setReleaseDate(dateString);
        malformedBodyDto.setReleaseStatus("NO_EXISTING_STATUS");
        var entity = new HttpEntity<>(malformedBodyDto, headers);

        ResponseEntity<ReleaseResponseModel> response = testRestTemplate.exchange("/releases/" + releaseIdForUpdate, HttpMethod.PUT, entity, ReleaseResponseModel.class);
        if(releaseIdForUpdate == 0)
            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        else{
            String expectedMsg = ErrorMessage.INVALID_RELEASE_STATUS;
            assertEquals(expectedMsg, response.getBody().getDescription());
        }
    }

    @Test
    @Order(4)
    public void testUser_withEmptyBearer() {
        headers.setBearerAuth("empty token");
        var entity = new HttpEntity<>(headers);

        ResponseEntity<ReleaseResponseModel> response = testRestTemplate.exchange("/releases", HttpMethod.GET, entity, ReleaseResponseModel.class);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());

    }

    private String createBearerForTestUser() throws JsonProcessingException {
        var userInfo = new UserInfo();
        userInfo.setEmail(userEmail);
        userInfo.setPassword(password);
        userInfo.setReturnSecureToken(true);

        var jsonBody = mapper.writeValueAsString(userInfo);
        String url = firebaseUrl + "?key=" + firebaseApikey;
        var response = testRestTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(jsonBody), String.class);
        String responseJson = response.getBody();
        return mapper.readTree(responseJson).findValue("idToken").textValue();
    }
}
