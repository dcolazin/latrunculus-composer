package org.rubato.rubettes.bigbang;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.rubato.rubettes.util.CoolFormRegistrant;
import org.rubato.rubettes.util.FormValueFinder;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FormValueFinderTest {
	
	private TestObjects testObjects;
	
	@BeforeEach
	void setUp() {
		this.testObjects = new TestObjects();
	}
	
	@Test
	void testWithMacroScore() {
		FormValueFinder finder = new FormValueFinder(this.testObjects.MACRO_SCORE_FORM, true);
		assertEquals(5, finder.getCoordinateSystemValueNames().size());
		assertEquals(1, finder.getObjectCount());
		assertEquals(5, finder.getObjectAt(0).getColimitConfigurationValueNames(this.createColimitConfiguration()).size());
		assertEquals(5, finder.getObjectAt(0).getColimitConfigurationValuePaths(this.createColimitConfiguration()).size());
		assertEquals(0, finder.getObjectAt(0).getColimits().size());
		assertEquals(0, finder.getObjectAt(0).getColimitPaths().size());
		assertEquals(true, finder.formAllowsForSatellites());
		assertEquals(false, finder.formContainsColimits());
	}

	@Test
	void testWithSoundScore() {
		FormValueFinder finder = new FormValueFinder(this.testObjects.SOUND_SCORE_FORM, true);
		assertEquals(5, finder.getCoordinateSystemValueNames().size());
		assertEquals(2, finder.getObjectCount());
		assertEquals(5, finder.getObjectAt(0).getColimitConfigurationValueNames(this.createColimitConfiguration()).size());
		assertEquals(5, finder.getObjectAt(0).getColimitConfigurationValuePaths(this.createColimitConfiguration()).size());
		assertEquals(0, finder.getObjectAt(0).getColimits().size());
		assertEquals(0, finder.getObjectAt(0).getColimitPaths().size());
		assertEquals(true, finder.formAllowsForSatellites());
		assertEquals(false, finder.formContainsColimits());
	}

	@Test
	void testWithHarmonicSpectrum() {
		FormValueFinder finder = new FormValueFinder(this.testObjects.HARMONIC_SPECTRUM_FORM, true);
		assertEquals(3, finder.getCoordinateSystemValueNames().size());
		assertEquals(2, finder.getObjectCount());
		assertEquals(1, finder.getObjectAt(0).getColimitConfigurationValueNames(this.createColimitConfiguration()).size());
		assertEquals(1, finder.getObjectAt(0).getColimitConfigurationValuePaths(this.createColimitConfiguration()).size());
		assertEquals(0, finder.getObjectAt(0).getColimits().size());
		assertEquals(0, finder.getObjectAt(0).getColimitPaths().size());
		assertEquals(2, finder.getObjectAt(1).getColimitConfigurationValueNames(this.createColimitConfiguration()).size());
		assertEquals(2, finder.getObjectAt(1).getColimitConfigurationValuePaths(this.createColimitConfiguration()).size());
		assertEquals(0, finder.getObjectAt(1).getColimits().size());
		assertEquals(0, finder.getObjectAt(1).getColimitPaths().size());
		assertEquals(true, finder.formAllowsForSatellites());
		assertEquals(false, finder.formContainsColimits());
	}

	@Test
	void testWithIntegerOrReals() {
		FormValueFinder finder = new FormValueFinder(this.testObjects.INTEGER_OR_REALS_FORM, true);
		assertEquals(2, finder.getCoordinateSystemValueNames().size());
		assertEquals(1, finder.getObjectCount());
		assertEquals(1, finder.getObjectAt(0).getColimits().size());
		assertEquals(1, finder.getObjectAt(0).getColimitPaths().size());
		assertEquals(1, finder.getObjectAt(0).getColimitConfigurationValueNames(this.createColimitConfiguration(0)).size());
		assertEquals(1, finder.getObjectAt(0).getColimitConfigurationValuePaths(this.createColimitConfiguration(0)).size());
		assertEquals(1, finder.getObjectAt(0).getColimitConfigurationValueNames(this.createColimitConfiguration(1)).size());
		assertEquals(1, finder.getObjectAt(0).getColimitConfigurationValuePaths(this.createColimitConfiguration(1)).size());
		assertEquals(false, finder.formAllowsForSatellites());
		assertEquals(true, finder.formContainsColimits());
	}

	@Test
	void testWithGeneralScore() {
		FormValueFinder finder = new FormValueFinder(this.testObjects.GENERAL_SCORE_FORM, true);
		assertEquals(5, finder.getCoordinateSystemValueNames().size());
		assertEquals(1, finder.getObjectCount());
		assertEquals(1, finder.getObjectAt(0).getColimits().size());
		assertEquals(1, finder.getObjectAt(0).getColimitPaths().size());
		assertEquals(5, finder.getObjectAt(0).getColimitConfigurationValueNames(this.createColimitConfiguration(0)).size());
		assertEquals(5, finder.getObjectAt(0).getColimitConfigurationValuePaths(this.createColimitConfiguration(0)).size());
		assertEquals(3, finder.getObjectAt(0).getColimitConfigurationValueNames(this.createColimitConfiguration(1)).size());
		assertEquals(3, finder.getObjectAt(0).getColimitConfigurationValuePaths(this.createColimitConfiguration(1)).size());
		assertEquals(false, finder.formAllowsForSatellites());
		assertEquals(true, finder.formContainsColimits());
	}

	@Test
	void testWithDyad() {
		FormValueFinder finder = new FormValueFinder(CoolFormRegistrant.DYADS_FORM, true);
		assertEquals(2, finder.getCoordinateSystemValueNames().size());
		assertEquals(1, finder.getObjectCount());
		assertEquals(2, finder.getObjectAt(0).getColimitConfigurationValueNames(this.createColimitConfiguration()).size());
		assertEquals(2, finder.getObjectAt(0).getColimitConfigurationValuePaths(this.createColimitConfiguration()).size());
		assertEquals(0, finder.getObjectAt(0).getColimits().size());
		assertEquals(0, finder.getObjectAt(0).getColimitPaths().size());
		assertEquals(false, finder.formAllowsForSatellites());
		assertEquals(false, finder.formContainsColimits());
	}
	
	private List<Integer> createColimitConfiguration(Integer... integers) {
		return new ArrayList<Integer>(Arrays.asList(integers));
	}

}
