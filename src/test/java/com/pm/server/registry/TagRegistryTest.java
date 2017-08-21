package com.pm.server.registry;

import com.pm.server.PmServerException;
import com.pm.server.TestTemplate;
import com.pm.server.datatype.Player;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static org.hamcrest.beans.HasPropertyWithValue.hasProperty;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

@RunWith(MockitoJUnitRunner.class)
public class TagRegistryTest extends TestTemplate {

	private TagRegistry tagRegistry;

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Before
	public void setUp() {
		tagRegistry = new TagRegistryImpl();
	}

	@Test
	public void unitTest_tagPlayer_GhostPacman() throws PmServerException {

		// Given
		Player.Name tagger = Player.Name.Inky;
		Player.Name taggee = Player.Name.Pacman;

		// When
		Boolean gameEnds = tagRegistry.tagPlayer(tagger, taggee);

		// Then
		assertFalse(gameEnds);

	}

	@Test
	public void unitTest_tagPlayer_PacmanGhost() throws PmServerException {

		// Given
		Player.Name tagger = Player.Name.Pacman;
		Player.Name taggee = Player.Name.Inky;

		// When
		Boolean gameEnds = tagRegistry.tagPlayer(tagger, taggee);

		// Then
		assertFalse(gameEnds);

	}

	@Test(expected=NullPointerException.class)
	public void unitTest_tagPlayer_nullTagger() throws Exception {

		// Given
		Player.Name taggee = Player.Name.Inky;

		// When
		tagRegistry.tagPlayer(null, taggee);

		// Then
		// Exception thrown above

	}

	@Test(expected=NullPointerException.class)
	public void unitTest_tagPlayer_nullTaggee() throws Exception {

		// Given
		Player.Name tagger = Player.Name.Inky;

		// When
		tagRegistry.tagPlayer(tagger, null);

		// Then
		// Exception thrown above

	}

	@Test
	public void unitTest_tagPlayer_samePlayer() throws Exception {

		// Given
		Player.Name tagger = Player.Name.Inky;

		// Then
		thrown.expect(PmServerException.class);
		thrown.expect(hasProperty(
				"status", is(HttpStatus.INTERNAL_SERVER_ERROR)
		));

		// When
		tagRegistry.tagPlayer(tagger, tagger);

	}

	@Test
	public void unitTest_receiveTagFromPlayer_GhostPacman()
			throws PmServerException {

		// Given
		Player.Name taggee = Player.Name.Pacman;
		Player.Name tagger = Player.Name.Inky;

		// When
		Boolean gameEnds = tagRegistry.receiveTagFromPlayer(taggee, tagger);

		// Then
		assertFalse(gameEnds);

	}

	@Test
	public void unitTest_receiveTagFromPlayer_PacmanGhost()
			throws PmServerException {

		// Given
		Player.Name taggee = Player.Name.Inky;
		Player.Name tagger = Player.Name.Pacman;

		// When
		Boolean gameEnds = tagRegistry.receiveTagFromPlayer(taggee, tagger);

		// Then
		assertFalse(gameEnds);

	}

	@Test(expected=NullPointerException.class)
	public void unitTest_receiveTagFromPlayer_nullTaggee() throws Exception {

		// Given
		Player.Name tagger = Player.Name.Inky;

		// When
		tagRegistry.receiveTagFromPlayer(null, tagger);

		// Then
		// Exception thrown above

	}

	@Test(expected=NullPointerException.class)
	public void unitTest_receiveTagFromPlayer_nullTagger() throws Exception {

		// Given
		Player.Name taggee = Player.Name.Inky;

		// When
		tagRegistry.receiveTagFromPlayer(taggee, null);

		// Then
		// Exception thrown above

	}

	@Test
	public void unitTest_receiveTagFromPlayer_samePlayer() throws Exception {

		// Given
		Player.Name taggee = Player.Name.Inky;

		// Then
		thrown.expect(PmServerException.class);
		thrown.expect(hasProperty(
				"status", is(HttpStatus.INTERNAL_SERVER_ERROR)
		));

		// When
		tagRegistry.receiveTagFromPlayer(taggee, taggee);

	}

	@Test
	public void unitTest_tagPlayer_winGame() throws PmServerException {

		// Given
		Player.Name tagger = Player.Name.Inky;
		Player.Name taggee = Player.Name.Pacman;
		tagRegistry.receiveTagFromPlayer(taggee, tagger);

		// When
		Boolean gameEnds = tagRegistry.tagPlayer(tagger, taggee);

		// Then
		assertTrue(gameEnds);

	}

	@Test
	public void unitTest_receiveTagFromPlayer_winGame() throws PmServerException {

		// Given
		Player.Name tagger = Player.Name.Inky;
		Player.Name taggee = Player.Name.Pacman;
		tagRegistry.tagPlayer(tagger, taggee);

		// When
		Boolean gameEnds = tagRegistry.receiveTagFromPlayer(taggee, tagger);

		// Then
		assertTrue(gameEnds);

	}

	@Test
	public void unitTest_getWinner_none() throws PmServerException {

		// When
		Player.Name winner = tagRegistry.getWinner();

		// Then
		assertNull(winner);

	}

	@Test
	public void unitTest_getWinner_Inky() throws PmServerException {

		// Given
		Player.Name tagger = Player.Name.Inky;
		Player.Name taggee = Player.Name.Pacman;
		tagRegistry.receiveTagFromPlayer(taggee, tagger);
		tagRegistry.tagPlayer(tagger, taggee);

		// When
		Player.Name winner = tagRegistry.getWinner();

		// Then
		assertEquals(winner, tagger);

	}

	@Test
	public void unitTest_getWinner_Pacman() throws PmServerException {

		// Given
		Player.Name tagger = Player.Name.Pacman;
		Player.Name taggee = Player.Name.Inky;
		tagRegistry.receiveTagFromPlayer(taggee, tagger);
		tagRegistry.tagPlayer(tagger, taggee);

		// When
		Player.Name winner = tagRegistry.getWinner();

		// Then
		assertEquals(winner, tagger);

	}

	@Test
	public void unitTest_clearTagsAndWinner_start() throws PmServerException {

		// When
		tagRegistry.clearTagsAndWinner();

		// Then
		assertNull(tagRegistry.getWinner());

	}

	@Test
	public void unitTest_clearTagsAndWinner_tagged() throws PmServerException {

		// Given
		Player.Name tagger = Player.Name.Pacman;
		Player.Name taggee = Player.Name.Inky;
		tagRegistry.tagPlayer(tagger, taggee);

		// When
		tagRegistry.clearTagsAndWinner();
		tagRegistry.receiveTagFromPlayer(taggee, tagger);

		// Then
		assertNull(tagRegistry.getWinner());

	}

	@Test
	public void unitTest_clearTagsAndWinner_winner() throws PmServerException {

		// Given
		Player.Name tagger = Player.Name.Pacman;
		Player.Name taggee = Player.Name.Inky;
		tagRegistry.tagPlayer(tagger, taggee);
		tagRegistry.receiveTagFromPlayer(taggee, tagger);
		assertNotNull(tagRegistry.getWinner());

		// When
		tagRegistry.clearTagsAndWinner();

		// Then
		assertNull(tagRegistry.getWinner());

	}

}
