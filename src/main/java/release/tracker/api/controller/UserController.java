package release.tracker.api.controller;

import release.tracker.api.exceptions.RecordNotFoundException;
import release.tracker.api.model.dto.UserInfo;
import release.tracker.api.services.implementation.FirebaseUserService;
import com.google.firebase.auth.FirebaseAuthException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user/authorize")
public class UserController {

    @Autowired
    FirebaseUserService _fireBaseUserService;

    @PostMapping
    public ResponseEntity<String> returnBearerToken(@RequestBody UserInfo userInfo) {
        try {
            String token = _fireBaseUserService.generateBearerTokenForUser(userInfo);
            return ResponseEntity.ok(token);
        } catch (RecordNotFoundException | FirebaseAuthException exc) {
            return new ResponseEntity<>(exc.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
