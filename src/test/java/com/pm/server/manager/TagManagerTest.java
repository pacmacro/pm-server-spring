package com.pm.server.manager;

import com.pm.server.PmServerException;
import com.pm.server.TestTemplate;
import com.pm.server.datatype.GameState;
import com.pm.server.datatype.Player;
import com.pm.server.registry.GameStateRegistry;
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

import static com.pm.server.datatype.Player.Name.*;
import static org.hamcrest.beans.HasPropertyWithValue.hasProperty;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TagManagerTest extends TestTemplate {

    @Mock
    private GameStateRegistry mockGameStateRegistry;

    @Mock
    private PlayerRegistry mockPlayerRegistry;

    @Mock
    private TagRegistry mockTagRegistry;

    private TagManager tagManager;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        tagManager = new TagManagerImpl(
                mockGameStateRegistry,
                mockPlayerRegistry,
                mockTagRegistry
        );
    }

    @Test
    public void unitTest_registerTag_source()
            throws PmServerException {

        // Given
        Player.Name reporter = Pacman;
        Player.Name source = Inky;

        Player reporterPlayer = new Player(reporter);
        reporterPlayer.setState(Player.State.ACTIVE);
        when(mockPlayerRegistry.getPlayerState(reporter))
                .thenReturn(reporterPlayer.getState());

        Player otherPlayer = new Player(source);
        otherPlayer.setState(Player.State.ACTIVE);
        when(mockPlayerRegistry.getPlayerState(source))
                .thenReturn(otherPlayer.getState());

        when(mockGameStateRegistry.getCurrentState())
                .thenReturn(GameState.IN_PROGRESS);

        // When
        tagManager.registerTag(reporter, source, null);

        // Then
        verify(mockTagRegistry).receiveTagFromPlayer(reporter, source);

    }

    @Test
    public void unitTest_registerTag_destination()
            throws PmServerException {

        // Given
        Player.Name reporter = Pacman;
        Player.Name destination = Inky;

        Player reporterPlayer = new Player(reporter);
        reporterPlayer.setState(Player.State.ACTIVE);
        when(mockPlayerRegistry.getPlayerState(reporter))
                .thenReturn(reporterPlayer.getState());

        Player otherPlayer = new Player(destination);
        otherPlayer.setState(Player.State.ACTIVE);
        when(mockPlayerRegistry.getPlayerState(destination))
                .thenReturn(otherPlayer.getState());

        when(mockGameStateRegistry.getCurrentState())
                .thenReturn(GameState.IN_PROGRESS);

        // When
        tagManager.registerTag(reporter, null, destination);

        // Then
        verify(mockTagRegistry).tagPlayer(reporter, destination);

    }

    @Test
    public void unitTest_registerTag_noOtherPlayers()
            throws PmServerException {

        // Given
        Player.Name reporter = Pacman;

        Player reporterPlayer = new Player(reporter);
        reporterPlayer.setState(Player.State.ACTIVE);
        when(mockPlayerRegistry.getPlayerState(reporter))
                .thenReturn(reporterPlayer.getState());

        when(mockGameStateRegistry.getCurrentState())
                .thenReturn(GameState.IN_PROGRESS);

        // Then
        thrown.expect(PmServerException.class);
        thrown.expect(hasProperty(
                "status", is(HttpStatus.BAD_REQUEST)
        ));

        // When
        tagManager.registerTag(reporter, null, null);

    }

    @Test
    public void unitTest_registerTag_bothOtherPlayers()
            throws PmServerException {

        // Given
        Player.Name reporter = Pacman;
        Player.Name source = Inky;
        Player.Name destination = Blinky;

        Player reporterPlayer = new Player(reporter);
        reporterPlayer.setState(Player.State.ACTIVE);
        when(mockPlayerRegistry.getPlayerState(reporter))
                .thenReturn(reporterPlayer.getState());

        Player otherPlayer = new Player(source);
        otherPlayer.setState(Player.State.ACTIVE);
        when(mockPlayerRegistry.getPlayerState(source))
                .thenReturn(otherPlayer.getState());

        Player otherOtherPlayer = new Player(destination);
        otherOtherPlayer.setState(Player.State.ACTIVE);
        when(mockPlayerRegistry.getPlayerState(destination))
                .thenReturn(otherOtherPlayer.getState());

        when(mockGameStateRegistry.getCurrentState())
                .thenReturn(GameState.IN_PROGRESS);

        // Then
        thrown.expect(PmServerException.class);
        thrown.expect(hasProperty(
                "status", is(HttpStatus.BAD_REQUEST)
        ));

        // When
        tagManager.registerTag(reporter, source, destination);

    }

    @Test
    public void unitTest_registerTag_gameInitializing()
            throws PmServerException {

        // Given
        Player.Name reporter = Pacman;
        Player.Name destination = Inky;

        Player reporterPlayer = new Player(reporter);
        reporterPlayer.setState(Player.State.ACTIVE);
        when(mockPlayerRegistry.getPlayerState(reporter))
                .thenReturn(reporterPlayer.getState());

        Player otherPlayer = new Player(destination);
        otherPlayer.setState(Player.State.ACTIVE);
        when(mockPlayerRegistry.getPlayerState(destination))
                .thenReturn(otherPlayer.getState());

        when(mockGameStateRegistry.getCurrentState())
                .thenReturn(GameState.INITIALIZING);

        // Then
        thrown.expect(PmServerException.class);
        thrown.expect(hasProperty(
                "status", is(HttpStatus.CONFLICT)
        ));

        // When
        tagManager.registerTag(reporter, null, reporter);

    }

    @Test
    public void unitTest_registerTag_sourceIsReporter()
            throws PmServerException {

        // Given
        Player.Name reporter = Pacman;

        Player reporterPlayer = new Player(reporter);
        reporterPlayer.setState(Player.State.ACTIVE);
        when(mockPlayerRegistry.getPlayerState(reporter))
                .thenReturn(reporterPlayer.getState());

        when(mockGameStateRegistry.getCurrentState())
                .thenReturn(GameState.IN_PROGRESS);

        // Then
        thrown.expect(PmServerException.class);
        thrown.expect(hasProperty(
                "status", is(HttpStatus.CONFLICT)
        ));

        // When
        tagManager.registerTag(reporter, reporter, null);

    }

    @Test
    public void unitTest_registerTag_destinationIsReporter()
            throws PmServerException {

        // Given
        Player.Name reporter = Pacman;

        Player reporterPlayer = new Player(reporter);
        reporterPlayer.setState(Player.State.ACTIVE);
        when(mockPlayerRegistry.getPlayerState(reporter))
                .thenReturn(reporterPlayer.getState());

        when(mockGameStateRegistry.getCurrentState())
                .thenReturn(GameState.IN_PROGRESS);

        // Then
        thrown.expect(PmServerException.class);
        thrown.expect(hasProperty(
                "status", is(HttpStatus.CONFLICT)
        ));

        // When
        tagManager.registerTag(reporter, null, reporter);

    }

    @Test
    public void unitTest_registerTag_bothGhosts()
            throws PmServerException {

        // Given
        Player.Name reporter = Blinky;
        Player.Name source = Inky;

        Player reporterPlayer = new Player(reporter);
        reporterPlayer.setState(Player.State.ACTIVE);
        when(mockPlayerRegistry.getPlayerState(reporter))
                .thenReturn(reporterPlayer.getState());

        Player otherPlayer = new Player(source);
        otherPlayer.setState(Player.State.ACTIVE);
        when(mockPlayerRegistry.getPlayerState(source))
                .thenReturn(otherPlayer.getState());

        when(mockGameStateRegistry.getCurrentState())
                .thenReturn(GameState.IN_PROGRESS);

        // Then
        thrown.expect(PmServerException.class);
        thrown.expect(hasProperty(
                "status", is(HttpStatus.CONFLICT)
        ));

        // When
        tagManager.registerTag(reporter, source, null);

    }

    @Test
    public void unitTest_registerTag_completeTagPacman()
            throws PmServerException {

        // Given
        Player.Name tagger = Inky;
        Player.Name taggee = Pacman;

        Player taggerPlayer = new Player(tagger);
        taggerPlayer.setState(Player.State.ACTIVE);
        when(mockPlayerRegistry.getPlayerState(tagger))
                .thenReturn(taggerPlayer.getState());

        Player taggeePlayer = new Player(taggee);
        taggeePlayer.setState(Player.State.ACTIVE);
        when(mockPlayerRegistry.getPlayerState(taggee))
                .thenReturn(taggeePlayer.getState());

        when(mockGameStateRegistry.getCurrentState())
                .thenReturn(GameState.IN_PROGRESS);

        when(mockTagRegistry.tagPlayer(tagger, taggee))
                .thenReturn(true);

        // When
        tagManager.registerTag(tagger, null, taggee);

        // Then
        verify(mockPlayerRegistry).setPlayerStateByName(
                taggee, Player.State.CAPTURED
        );
        verify(mockGameStateRegistry).setWinnerGhosts();

    }

    @Test
    public void unitTest_registerTag_completeTagGhosts()
            throws PmServerException {

        // Given
        Player.Name tagger = Pacman;
        Player.Name taggee = Inky;

        Player taggerPlayer = new Player(tagger);
        taggerPlayer.setState(Player.State.ACTIVE);
        when(mockPlayerRegistry.getPlayerState(tagger))
                .thenReturn(taggerPlayer.getState());

        Player taggeePlayer = new Player(taggee);
        taggeePlayer.setState(Player.State.ACTIVE);
        when(mockPlayerRegistry.getPlayerState(taggee))
                .thenReturn(taggeePlayer.getState());

        when(mockGameStateRegistry.getCurrentState())
                .thenReturn(GameState.IN_PROGRESS);
        when(mockTagRegistry.receiveTagFromPlayer(taggee, tagger))
                .thenReturn(true);
        when(mockPlayerRegistry.allGhostsCaptured())
                .thenReturn(true);

        // When
        tagManager.registerTag(taggee, tagger, null);

        // Then
        verify(mockPlayerRegistry).setPlayerStateByName(
                taggee, Player.State.CAPTURED
        );
        verify(mockGameStateRegistry).setWinnerPacman();

    }

}
