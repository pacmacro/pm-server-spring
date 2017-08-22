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
		Boolean success = tagRegistry.tagPlayer(tagger, taggee);

		// Then
		assertFalse(success);

	}

	@Test
	public void unitTest_tagPlayer_PacmanGhost() throws PmServerException {

		// Given
		Player.Name tagger = Player.Name.Pacman;
		Player.Name taggee = Player.Name.Inky;

		// When
		Boolean success = tagRegistry.tagPlayer(tagger, taggee);

		// Then
		assertFalse(success);

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
		Boolean success = tagRegistry.receiveTagFromPlayer(taggee, tagger);

		// Then
		assertFalse(success);

	}

	@Test
	public void unitTest_receiveTagFromPlayer_PacmanGhost()
			throws PmServerException {

		// Given
		Player.Name taggee = Player.Name.Inky;
		Player.Name tagger = Player.Name.Pacman;

		// When
		Boolean success = tagRegistry.receiveTagFromPlayer(taggee, tagger);

		// Then
		assertFalse(success);

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
		Boolean success = tagRegistry.tagPlayer(tagger, taggee);

		// Then
		assertTrue(success);

	}

	@Test
	public void unitTest_receiveTagFromPlayer_winGame() throws PmServerException {

		// Given
		Player.Name tagger = Player.Name.Inky;
		Player.Name taggee = Player.Name.Pacman;
		tagRegistry.tagPlayer(tagger, taggee);

		// When
		Boolean success = tagRegistry.receiveTagFromPlayer(taggee, tagger);

		// Then
		assertTrue(success);

	}

	@Test
	public void unitTest_clearTags_start() throws PmServerException {

		// When
		tagRegistry.clearTags();

		// Then
		// No exception

	}

	@Test
	public void unitTest_clearTags_tagged() throws PmServerException {

		// Given
		Player.Name tagger = Player.Name.Inky;
		Player.Name taggee = Player.Name.Pacman;
		tagRegistry.receiveTagFromPlayer(taggee, tagger);

		// When
		tagRegistry.clearTags();
		Boolean success = tagRegistry.tagPlayer(tagger, taggee);

		// Then
		assertFalse(success);

	}

}
