package com.pm.server.registry;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;

import com.pm.server.TestTemplate;
import com.pm.server.datatype.Coordinate;
import com.pm.server.datatype.CoordinateImpl;
import com.pm.server.datatype.Pacdot;
import com.pm.server.repository.PacdotRepository;

@RunWith(MockitoJUnitRunner.class)
public class PacdotRegistryTest extends TestTemplate {

	private List<Pacdot> pacdotList = new ArrayList<>();

	@Mock
	private PacdotRepository pacdotRepositoryMock;

	@InjectMocks
	@Autowired
	private PacdotRegistryImpl pacdotRegistry;

	@Before
	public void setUp() {

		MockitoAnnotations.initMocks(this);

		Pacdot pacdot1 = new Pacdot();
		CoordinateImpl location1 = new CoordinateImpl(3919.12391013, 9488.49119489);
		pacdot1.setLocation(location1);
		pacdot1.setEaten(false);
		pacdot1.setPowerdot(false);
		pacdotList.add(pacdot1);

		Pacdot pacdot2 = new Pacdot();
		CoordinateImpl location2 = new CoordinateImpl(3919.12391012, 9488.49119488);
		pacdot2.setLocation(location2);
		pacdot2.setEaten(false);
		pacdot2.setPowerdot(true);
		pacdotList.add(pacdot2);

		Mockito.when(pacdotRepositoryMock.getAllPacdots()).thenReturn(pacdotList);

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

		Coordinate location = new CoordinateImpl();
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
		Coordinate location = new CoordinateImpl(-99999.0, -99999.0);

		// When
		pacdotRegistry.eatPacdotsNearLocation(location);

		// Then
		for(Pacdot pacdot : pacdotList) {
			assertFalse(pacdot.getEaten());
		}

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
