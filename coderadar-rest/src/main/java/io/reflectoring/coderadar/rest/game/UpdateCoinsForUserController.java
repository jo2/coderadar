package io.reflectoring.coderadar.rest.game;

import io.reflectoring.coderadar.useradministration.port.driver.game.UpdateCoinsForUserUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UpdateCoinsForUserController {

    private final UpdateCoinsForUserUseCase updateCoinsForUserUseCase;

    @PostMapping("/users/{userId}/coins")
    public ResponseEntity<HttpStatus> updateCoinsForUser(@PathVariable long userId, @RequestBody int coins) {
        updateCoinsForUserUseCase.updateCoinsForUser(userId, coins);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
