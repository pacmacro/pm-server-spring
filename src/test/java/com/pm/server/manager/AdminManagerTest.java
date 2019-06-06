package com.pm.server.manager;

import com.pm.server.PmServerException;
import com.pm.server.TestTemplate;
import com.pm.server.datatype.GameState;
import com.pm.server.datatype.Pacdot;
import com.pm.server.datatype.Player;
import com.pm.server.registry.GameStateRegistry;
import com.pm.server.registry.PacdotRegistry;
import com.pm.server.registry.PlayerRegistry;
import com.pm.server.registry.TagRegistry;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AdminManagerTest extends TestTemplate {

    private AdminManager adminManager;

    // Sample data
    private List<Pacdot> pacdotList = new ArrayList<>();

    // Mock objects
    @Mock
    private GameStateRegistry mockGameStateRegistry;
    @Mock
    private PacdotRegistry mockPacdotRegistry;
    @Mock
    private PlayerRegistry mockPlayerRegistry;
    @Mock
    private TagRegistry mockTagRegistry;
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        when(mockPacdotRegistry.getInformationOfAllPacdots())
                .thenReturn(pacdotList);

        adminManager = new AdminManagerImpl(
                mockGameStateRegistry, mockPacdotRegistry,
                mockPlayerRegistry, mockTagRegistry
        );
    }

    @After
    public void tearDown() {
        mockPlayerRegistry.reset();
    }

    @Test
    public void unitTest_changeGameState_initializing() throws PmServerException {

        // Given
        GameState newState = GameState.INITIALIZING;

        // When
        adminManager.changeGameState(newState);

        // Then
        verify(mockGameStateRegistry).resetGame();

    }

    @Test
    public void unitTest_changeGameState_inProgress() throws PmServerException {

        // Given
        GameState newState = GameState.IN_PROGRESS;

        // When
        adminManager.changeGameState(newState);

        // Then
        verify(mockGameStateRegistry).startGame();

    }

    @Test
    public void unitTest_changeGameState_pauseGame() throws PmServerException {

        // Given
        GameState newState = GameState.PAUSED;

        // When
        adminManager.changeGameState(newState);

        // Then
        verify(mockGameStateRegistry).pauseGame();

    }

    @Test
    public void unitTest_changeGameState_setWinnerPacman()
            throws PmServerException {

        // Given
        GameState newState = GameState.FINISHED_PACMAN_WIN;

        // When
        adminManager.changeGameState(newState);

        // Then
        verify(mockGameStateRegistry).setWinnerPacman();

    }

    @Test
    public void unitTest_changeGameState_setWinnerGhosts()
            throws PmServerException {

        // Given
        GameState newState = GameState.FINISHED_GHOSTS_WIN;

        // When
        adminManager.changeGameState(newState);

        // Then
        verify(mockGameStateRegistry).setWinnerGhosts();
        verify(mockPlayerRegistry).setPlayerStateByName(
                Player.Name.Pacman, Player.State.CAPTURED
        );

    }

    @Test
    public void unitTest_resetPacdots() {

        // Given
        for(int i = 0; i < 10; i++) {
            pacdotList.add(new Pacdot());
        }
        for(Pacdot p : pacdotList) {
            p.setEaten();
        }

        // When
        adminManager.resetPacdots();

        // Then
        verify(mockPacdotRegistry).resetPacdots();

    }

    @Test
    public void unitTest_resetPacdots_noPacdots() {

        // Given
        pacdotList.clear();

        // When
        adminManager.resetPacdots();

        // Then
        // No exception

    }

}
