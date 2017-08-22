package com.pm.server.manager;

import com.pm.server.PmServerException;
import com.pm.server.TestTemplate;
import com.pm.server.datatype.GameState;
import com.pm.server.registry.GameStateRegistry;
import com.pm.server.registry.PacdotRegistry;
import com.pm.server.registry.PlayerRegistry;
import com.pm.server.registry.TagRegistry;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasProperty;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class AdminGameStateManagerTest extends TestTemplate {

    @Mock
    private PlayerRegistry mockPlayerRegistry;

    @Mock
    private PacdotRegistry mockPacdotRegistry;

    @Mock
    private GameStateRegistry mockGameStateRegistry;

    @Mock
    private TagRegistry mockTagRegistry;

    private AdminGameStateManager adminGameStateManager;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        adminGameStateManager = new AdminGameStateManager(
                mockPlayerRegistry,
                mockPacdotRegistry,
                mockGameStateRegistry,
                mockTagRegistry
        );
    }

    @Test
    public void unitTest_changeGameState_initializing()
            throws PmServerException {

        // Given
        GameState newState = GameState.INITIALIZING;

        // When
        adminGameStateManager.changeGameState(newState);

        // Then
        verify(mockGameStateRegistry).resetGame();

    }

    @Test
    public void unitTest_changeGameState_inProgress()
            throws PmServerException {

        // Given
        GameState newState = GameState.IN_PROGRESS;

        // When
        adminGameStateManager.changeGameState(newState);

        // Then
        verify(mockGameStateRegistry).startGame();

    }

    @Test
    public void unitTest_changeGameState_exception() throws PmServerException {

        // Given
        GameState newState = GameState.INITIALIZING;
        doThrow(new IllegalStateException())
                .when(mockGameStateRegistry).resetGame();

        // Then
        thrown.expect(PmServerException.class);
        thrown.expect(hasProperty("status"));
        thrown.expect(hasProperty(
                "status", is(HttpStatus.CONFLICT)
        ));

        // When
        adminGameStateManager.changeGameState(newState);

    }

}
