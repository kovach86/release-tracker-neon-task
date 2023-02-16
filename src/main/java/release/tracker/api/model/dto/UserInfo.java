package release.tracker.api.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserInfo {

    private String email;
    private String password;
    private boolean returnSecureToken;
}
