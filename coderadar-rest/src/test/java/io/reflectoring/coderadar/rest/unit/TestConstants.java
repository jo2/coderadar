package io.reflectoring.coderadar.rest.unit;

import io.reflectoring.coderadar.domain.User;
import io.reflectoring.coderadar.rest.domain.GetUserResponse;

import java.util.Collections;

public interface TestConstants {

    static GetUserResponse getGetUserResponse(Long id, String username, boolean platformAdmin) {
        return new GetUserResponse(
                id,
                username,
                platformAdmin,
                1,
                1,
                1,
                1,
                "test",
                "#000000",
                "#000000",
                Collections.emptyList(),
                Collections.emptyList(),
                Collections.emptyList()
        );
    }

    static User getUser(Long id, String username, boolean platformAdmin) {
        return new User()
                .setId(id)
                .setUsername(username)
                .setPassword("")
                .setPlatformAdmin(platformAdmin)
                .setContributors(Collections.emptyList())
                .setLevel(1)
                .setCoins(1)
                .setPointsInLevel(1)
                .setPointsOverAll(1)
                .setTitle("test")
                .setPrimaryColor("#000000")
                .setSecondaryColor("#000000")
                .setBadges(Collections.emptyList())
                .setPublicBadges(Collections.emptyList())
                .setSolvedQuestIds(Collections.emptyList()
        );
    }
}
