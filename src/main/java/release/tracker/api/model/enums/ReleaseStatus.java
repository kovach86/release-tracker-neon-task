package release.tracker.api.model.enums;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.stream.Collectors;

@Nullable
public enum ReleaseStatus {
    CREATED("Created"),
    IN_DEVELOPMENT("In Development"),
    ON_DEV("On Dev"),
    QA_DONE_ON_DEV("QA Done on DEV"),
    ON_STAGING("On staging"),
    QA_DONE_ON_STAGING("QA done on STAGING"),
    ON_PROD("On PROD"),
    DONE("Done");
    private String name;

    ReleaseStatus(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static String getNames() {
        String result = Arrays.stream(values())
                .filter(releaseStatus -> releaseStatus != ReleaseStatus.CREATED)
                .map(Enum::toString)
                .collect(Collectors.joining(", "));

        return result;
    }

    public static ReleaseStatus getReleaseStatusByName(String status) {
        ReleaseStatus result = null;
        if (status != null || !status.equals("")) {
            for (ReleaseStatus releaseStatus : values()) {
                if (releaseStatus.getName().equals(status.trim())) {
                    result = releaseStatus;
                    break;
                }
            }
        }
        return result;
    }


}
