package release.tracker.api.services.implementation;

import release.tracker.api.exceptions.RecordNotFoundException;
import release.tracker.api.model.dto.UserInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.auth.FirebaseAuthException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class FirebaseUserService {

    RestTemplate restTemplate;
    @Value("${firebase.authenticate.url}")
    private String firebaseUrl;
    @Value("${firebase.authenticate.key}")
    private String firebaseApikey;

    public FirebaseUserService() {
        this.restTemplate = new RestTemplate();
    }

    public String generateBearerTokenForUser(UserInfo userInfo) throws FirebaseAuthException, RecordNotFoundException {
        try {
            var headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            String url = firebaseUrl + "?key=" + firebaseApikey;
            var response = restTemplate.postForEntity(url, new HttpEntity<>(userInfo, headers), String.class);

            return new ObjectMapper().readTree(response.getBody()).findValue("idToken").textValue();
        } catch (Exception e) {
            throw new RecordNotFoundException("no user with this email");
        }
    }
}
