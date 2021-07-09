package io.reflectoring.coderadar.useradministration.service.game;

import io.reflectoring.coderadar.IllegalLevelConfigurationException;
import io.reflectoring.coderadar.domain.Level;
import io.reflectoring.coderadar.domain.User;
import io.reflectoring.coderadar.useradministration.port.driven.GetUserPort;
import io.reflectoring.coderadar.useradministration.port.driven.UpdateUserPort;
import io.reflectoring.coderadar.useradministration.service.get.GetUserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AddPointsToUserServiceTest {

    @Mock
    private UpdateUserPort updateUserScorePort;
    @Mock
    private GetUserPort getUserPort;
    @Mock
    private LevelConfig levelConfig;

    @InjectMocks
    private AddPointsToUserService addPointsToUserService;

    @Test
    void addPointsToUserSameLevel() {
        when(getUserPort.getUser(1L)).thenReturn(new User()
                .setId(1L)
                .setLevel(1)
                .setPointsInLevel(20)
                .setPointsOverAll(20));
        when(levelConfig.getLevels()).thenReturn(getLevelConfig());
        addPointsToUserService.addPointsToUser(1L, 50);
        verify(updateUserScorePort).updateUser(new User()
                .setId(1L)
                .setLevel(1)
                .setPointsInLevel(70)
                .setPointsOverAll(70));
    }

    @Test
    void addPointsToUserLevelUp() {
        when(getUserPort.getUser(1L)).thenReturn(new User()
                .setId(1L)
                .setLevel(1)
                .setPointsInLevel(20)
                .setPointsOverAll(20));
        when(levelConfig.getLevels()).thenReturn(getLevelConfig());
        addPointsToUserService.addPointsToUser(1L, 90);
        verify(updateUserScorePort).updateUser(new User()
                .setId(1L)
                .setLevel(2)
                .setPointsInLevel(10)
                .setPointsOverAll(110));
    }

    @Test
    void addPointsToUserToManyPointsForLevel() {
        when(getUserPort.getUser(1L)).thenReturn(new User()
                .setId(1L)
                .setLevel(1)
                .setPointsInLevel(20)
                .setPointsOverAll(120));
        when(levelConfig.getLevels()).thenReturn(getLevelConfig());
        assertThrows(
                IllegalLevelConfigurationException.class,
                () -> addPointsToUserService.addPointsToUser(1L, 90),
                "User has to many points for level 1");
    }

    @Test
    void addPointsToUserMorePointsThanReachable() {
        when(getUserPort.getUser(1L)).thenReturn(new User()
                .setId(1L)
                .setLevel(1)
                .setPointsInLevel(120)
                .setPointsOverAll(20));
        when(levelConfig.getLevels()).thenReturn(getLevelConfig());
        assertThrows(
                IllegalLevelConfigurationException.class,
                () -> addPointsToUserService.addPointsToUser(1L, 90),
                "User has more points than reachable in level 1");
    }

    @Test
    void addPointsToUserMultipleLevelUp() {
        when(getUserPort.getUser(1L)).thenReturn(new User()
                .setId(1L)
                .setLevel(1)
                .setPointsInLevel(20)
                .setPointsOverAll(20));
        when(levelConfig.getLevels()).thenReturn(getLevelConfig());
        addPointsToUserService.addPointsToUser(1L, 250);
        verify(updateUserScorePort).updateUser(new User()
                .setId(1L)
                .setLevel(3)
                .setPointsInLevel(50)
                .setPointsOverAll(270));
    }

    @Test
    void addPointsToUserLevelUpToMaxLevel() {
        when(getUserPort.getUser(1L)).thenReturn(new User()
                .setId(1L)
                .setLevel(7)
                .setPointsInLevel(320)
                .setPointsOverAll(1420));
        when(levelConfig.getLevels()).thenReturn(getLevelConfig());
        addPointsToUserService.addPointsToUser(1L, 90);
        verify(updateUserScorePort).updateUser(new User()
                .setId(1L)
                .setLevel(8)
                .setPointsInLevel(40)
                .setPointsOverAll(1510));
    }

    @Test
    void addPointsToUserMaxLevel() {
        when(getUserPort.getUser(1L)).thenReturn(new User()
                .setId(1L)
                .setLevel(8)
                .setPointsInLevel(50)
                .setPointsOverAll(1520));
        when(levelConfig.getLevels()).thenReturn(getLevelConfig());
        addPointsToUserService.addPointsToUser(1L, 10000);
        verify(updateUserScorePort).updateUser(new User()
                .setId(1L)
                .setLevel(8)
                .setPointsInLevel(10050)
                .setPointsOverAll(11520));
    }

    private List<Level> getLevelConfig() {
        return List.of(
                new Level().setLevel(1).setPointsForLevelUp(100).setNeededPoints(0),
                new Level().setLevel(2).setPointsForLevelUp(120).setNeededPoints(100),
                new Level().setLevel(3).setPointsForLevelUp(150).setNeededPoints(220),
                new Level().setLevel(4).setPointsForLevelUp(190).setNeededPoints(370),
                new Level().setLevel(5).setPointsForLevelUp(240).setNeededPoints(560),
                new Level().setLevel(6).setPointsForLevelUp(300).setNeededPoints(800),
                new Level().setLevel(7).setPointsForLevelUp(370).setNeededPoints(1100),
                new Level().setLevel(8).setPointsForLevelUp(-1).setNeededPoints(1470));
    }
}
