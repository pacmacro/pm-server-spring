package com.pm.server.registry;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import com.pm.server.datatype.EatenDots;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import com.pm.server.TestTemplate;
import com.pm.server.datatype.Coordinate;
import com.pm.server.datatype.Pacdot;
import com.pm.server.repository.PacdotRepository;

@RunWith(MockitoJUnitRunner.class)
public class PacdotRegistryTest extends TestTemplate {

	@Mock
	private PacdotRepository pacdotRepositoryMock;

	private PacdotRegistryImpl pacdotRegistry;

	private List<Pacdot> pacdotList = new ArrayList<>();

	@Before
	public void setUp() {

		MockitoAnnotations.initMocks(this);
		when(pacdotRepositoryMock.getAllPacdots()).thenReturn(pacdotList);

		pacdotRegistry = new PacdotRegistryImpl(
				pacdotRepositoryMock, "pacdots_test.json", "powerdots_test.json", 0.0005
		);

		Pacdot pacdot1 = new Pacdot();
		Coordinate location1 = new Coordinate(3919.12391013, 9488.49119489);
		pacdot1.setLocation(location1);
		pacdot1.setEaten(false);
		pacdot1.setPowerdot(false);
		pacdotList.add(pacdot1);

		Pacdot pacdot2 = new Pacdot();
		Coordinate location2 = new Coordinate(3919.12391012, 9488.49119488);
		pacdot2.setLocation(location2);
		pacdot2.setEaten(false);
		pacdot2.setPowerdot(true);
		pacdotList.add(pacdot2);

	}

	@Test
	public void unitTest_eatPacdotsNearLocation() {

		// Given
		Pacdot pacdot = pacdotList.get(0);
		assertFalse(pacdot.getEaten());

		// When
		pacdotRegistry.eatPacdotsNearLocation(pacdot.getLocation());

		// Then
		assertTrue(pacdot.getEaten());

	}

	@Test
	public void unitTest_eatPacdotsNearLocation_approximateLocation() {

		// Given
		Pacdot pacdot = pacdotList.get(0);
		assertFalse(pacdot.getEaten());

		Coordinate location = new Coordinate();
		location.setLatitude(pacdot.getLocation().getLatitude() + 0.0000001);
		location.setLongitude(pacdot.getLocation().getLongitude() + 0.0000001);

		// When
		pacdotRegistry.eatPacdotsNearLocation(location);

		// Then
		assertTrue(pacdot.getEaten());

	}

	@Test
	public void unitTest_eatPacdotsNearLocation_distant() {

		// Given
		Coordinate location = new Coordinate(-99999.0, -99999.0);

		// When
		pacdotRegistry.eatPacdotsNearLocation(location);

		// Then
		for(Pacdot pacdot : pacdotList) {
			assertFalse(pacdot.getEaten());
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
