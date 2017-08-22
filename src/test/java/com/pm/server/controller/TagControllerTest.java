package com.pm.server.controller;

import com.pm.server.ControllerTestTemplate;
import com.pm.server.PmServerException;
import com.pm.server.TestTemplate;
import com.pm.server.datatype.Player;
import com.pm.server.manager.AdminManager;
import com.pm.server.manager.TagManager;
import com.pm.server.request.TagRequest;
import com.pm.server.utils.JsonUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static com.pm.server.datatype.Player.Name.Inky;
import static com.pm.server.datatype.Player.Name.Pacman;
import static org.hamcrest.beans.HasPropertyWithValue.hasProperty;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TagControllerTest extends TestTemplate {

	@Mock
	private TagManager tagManager;

	private TagController tagController;

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		tagController = new TagController(tagManager);
	}

	@Test
	public void unitTest_registerTag() throws Exception {

		// Given
		Player.Name reporter = Pacman;
		Player.Name other = Inky;

		TagRequest tagRequest = new TagRequest();
		tagRequest.setSource(other.name());

		// When
		tagController.registerTag(reporter.name(), tagRequest);

		// Then
		verify(tagManager).registerTag(reporter, other, null);

	}

	@Test
	public void unitTest_registerTag_noRequestBody() throws Exception {

		// Given
		Player.Name reporter = Pacman;
		Player.Name other = Inky;

		// Then
		thrown.expect(PmServerException.class);
		thrown.expect(hasProperty(
				"status", is(HttpStatus.BAD_REQUEST)
		));

		// When
		tagController.registerTag(reporter.name(), null);

	}

	@Test
	public void unitTest_registerTag_wrongReporter() throws Exception {

		// Given
		Player.Name reporter = Pacman;
		Player.Name other = Inky;

		TagRequest tagRequest = new TagRequest();
		tagRequest.setSource(other.name());

		// Then
		thrown.expect(PmServerException.class);
		thrown.expect(hasProperty(
				"status", is(HttpStatus.NOT_FOUND)
		));

		// When
		tagController.registerTag(reporter.name() + "X", tagRequest);

	}

	@Test
	public void unitTest_registerTag_wrongSource() throws Exception {

		// Given
		Player.Name reporter = Pacman;
		Player.Name other = Inky;

		TagRequest tagRequest = new TagRequest();
		tagRequest.setSource(other.name() + "X");

		// Then
		thrown.expect(PmServerException.class);
		thrown.expect(hasProperty(
				"status", is(HttpStatus.NOT_FOUND)
		));

		// When
		tagController.registerTag(reporter.name(), tagRequest);

	}

	@Test
	public void unitTest_registerTag_wrongDestination() throws Exception {

		// Given
		Player.Name reporter = Pacman;
		Player.Name other = Inky;

		TagRequest tagRequest = new TagRequest();
		tagRequest.setDestination(other.name() + "X");

		// Then
		thrown.expect(PmServerException.class);
		thrown.expect(hasProperty(
				"status", is(HttpStatus.NOT_FOUND)
		));

		// When
		tagController.registerTag(reporter.name(), tagRequest);

	}

}
