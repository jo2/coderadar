package io.reflectoring.coderadar.useradministration.service.game;

import io.reflectoring.coderadar.useradministration.port.driven.GetUserPort;
import io.reflectoring.coderadar.useradministration.port.driven.UpdateUserPort;
import io.reflectoring.coderadar.useradministration.service.get.GetUserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UpdateCoinsForUserServiceTest {

    @Mock
    private GetUserPort getUserPort;
    @Mock
    private UpdateUserPort updateUserPort;
    @InjectMocks
    private UpdateCoinsForUserService updateCoinsForUserService;

    @Test
    void updateCoinsForUser() {

    }
}
