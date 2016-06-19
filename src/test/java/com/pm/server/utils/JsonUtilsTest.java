package com.pm.server.utils;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

public class JsonUtilsTest {

	private DummyObject dummyObject;

	@Before
	public void setUp() {

		dummyObject = new DummyObject();

	}

	@Test
	public void unitTest_objectToJson() {

		// Given
		Integer arbitraryInteger = 12345;
		dummyObject.integer = arbitraryInteger;

		String arbitraryString = "text here";
		dummyObject.stringArrayList.add(arbitraryString);

		// When
		String objectString = JsonUtils.objectToJson(dummyObject);

		// Then
		assertTrue(objectString.contains("{"));
		assertTrue(objectString.contains("}"));
		assertTrue(objectString.contains(Integer.toString(arbitraryInteger)));
		assertTrue(objectString.contains(arbitraryString));

	}

	@Test
	public void unitTest_objectToJson_null() {

		// Given
		dummyObject = null;

		// When
		String objectString = JsonUtils.objectToJson(dummyObject);

		// Then
		assertTrue(objectString.equals("null"));

	}

	private final class DummyObject {

		@SuppressWarnings("unused")
		public Integer integer = 0;

		public ArrayList<String> stringArrayList = new ArrayList<String>();

	}

}