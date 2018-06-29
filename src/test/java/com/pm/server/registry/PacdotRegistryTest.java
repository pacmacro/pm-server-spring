package com.pm.server.registry;

import com.pm.server.TestTemplate;
import com.pm.server.datatype.Coordinate;
import com.pm.server.datatype.EatenDots;
import com.pm.server.datatype.Pacdot;
import com.pm.server.repository.PacdotRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PacdotRegistryTest extends TestTemplate {

	@Mock
	private PacdotRepository pacdotRepositoryMock;

	private PacdotRegistryImpl pacdotRegistry;

	private List<Pacdot> pacdotList = new ArrayList<>();

	@Before
	public void setUp() throws Exception {

		MockitoAnnotations.initMocks(this);
		when(pacdotRepositoryMock.getAllPacdots()).thenReturn(pacdotList);

		pacdotRegistry = new PacdotRegistryImpl(
				pacdotRepositoryMock, "pacdots_test.json", "powerdots_test.json", 0.0005
		);
		pacdotRegistry.postConstruct();

		Pacdot pacdot1 = new Pacdot();
		Coordinate location1 = new Coordinate(3919.12391013, 9488.49119489);
		pacdot1.setLocation(location1);
		pacdot1.setUneaten();
		pacdot1.setAsPowerdot();
		pacdotList.add(pacdot1);

		Pacdot pacdot2 = new Pacdot();
		Coordinate location2 = new Coordinate(3919.12391012, 9488.49119488);
		pacdot2.setLocation(location2);
		pacdot2.setUneaten();
		pacdot2.setAsPowerdot();
		pacdotList.add(pacdot2);

	}

	@Test
	public void unitTest_eatPacdotsNearLocation() {

		// Given
		Pacdot pacdot = pacdotList.get(0);
		assertFalse(pacdot.isEaten());

		// When
		pacdotRegistry.eatPacdotsNearLocation(pacdot.getLocation());

		// Then
		assertTrue(pacdot.isEaten());

	}

	@Test
	public void unitTest_eatPacdotsNearLocation_approximateLocation() {

		// Given
		Pacdot pacdot = pacdotList.get(0);
		assertFalse(pacdot.isEaten());

		Coordinate location = new Coordinate();
		location.setLatitude(pacdot.getLocation().getLatitude() + 0.0000001);
		location.setLongitude(pacdot.getLocation().getLongitude() + 0.0000001);

		// When
		pacdotRegistry.eatPacdotsNearLocation(location);

		// Then
		assertTrue(pacdot.isEaten());

	}

	@Test
	public void unitTest_eatPacdotsNearLocation_distant() {

		// Given
		Coordinate location = new Coordinate(-99999.0, -99999.0);

		// When
		pacdotRegistry.eatPacdotsNearLocation(location);

		// Then
		for(Pacdot pacdot : pacdotList) {
			assertFalse(pacdot.isEaten());
		}

	}

	@Test
	public void unitTest_eatPacdotsNearLocation_eatenAgain() {

		// Given
		Coordinate location = new Coordinate(pacdotList.get(0).getLocation());

		EatenDots eatenDots = pacdotRegistry.eatPacdotsNearLocation(location);
		assertTrue(eatenDots.getEatenPowerdots() != 0);

		// When
		eatenDots = pacdotRegistry.eatPacdotsNearLocation(location);

		// Then
		assertTrue(eatenDots.getEatenPowerdots() == 0);

	}

	@Test(expected = NullPointerException.class)
	public void unitTest_eatPacdotsNearLocation_nullLocation() {

		// Given

		// When
		pacdotRegistry.eatPacdotsNearLocation(null);

		// Then
		// Exception thrown above

	}

}
