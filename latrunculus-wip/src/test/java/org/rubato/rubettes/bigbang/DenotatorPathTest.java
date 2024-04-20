package org.rubato.rubettes.bigbang;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.vetronauta.latrunculus.core.exception.LatrunculusCheckedException;
import org.vetronauta.latrunculus.core.logeo.DenoFactory;
import org.vetronauta.latrunculus.core.logeo.FormFactory;
import org.rubato.rubettes.util.DenotatorPath;
import org.vetronauta.latrunculus.core.math.element.generic.Vector;
import org.vetronauta.latrunculus.core.math.element.impl.Rational;
import org.vetronauta.latrunculus.core.math.element.impl.ZInteger;
import org.vetronauta.latrunculus.core.math.element.generic.FreeElement;
import org.vetronauta.latrunculus.core.math.module.generic.Module;
import org.vetronauta.latrunculus.core.math.module.generic.StringVectorModule;
import org.vetronauta.latrunculus.core.math.module.generic.VectorModule;
import org.vetronauta.latrunculus.core.math.module.impl.RRing;
import org.vetronauta.latrunculus.core.math.module.impl.ZRing;
import org.vetronauta.latrunculus.core.math.module.factory.StringRingRepository;
import org.vetronauta.latrunculus.core.math.yoneda.denotator.Denotator;
import org.vetronauta.latrunculus.core.math.yoneda.denotator.SimpleDenotator;
import org.vetronauta.latrunculus.core.math.yoneda.form.ColimitForm;
import org.vetronauta.latrunculus.core.math.yoneda.form.LimitForm;
import org.vetronauta.latrunculus.core.math.yoneda.form.PowerForm;
import org.vetronauta.latrunculus.core.math.yoneda.form.SimpleForm;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DenotatorPathTest {
	
	private TestObjects objects;
	private DenotatorPath satellitePath, modulatorPath, rationalTriplePath, realTriplesPath;
	
	@BeforeEach
	void setUp() {
		this.objects = new TestObjects();
		this.satellitePath = new DenotatorPath(this.objects.SOUND_SCORE_FORM, new int[]{2,1,3,1,5,0});
		this.modulatorPath = new DenotatorPath(this.objects.SOUND_SCORE_FORM, new int[]{2,1,5,0,5,3,5,2});
		this.rationalTriplePath = new DenotatorPath(this.objects.RATIONAL_TRIPLE_FORM, new int[]{2});
		this.realTriplesPath = new DenotatorPath(this.objects.REAL_TRIPLES_FORM, new int[]{5,1});
	}
	
	@Test
	void testGeneralMethods() {
		assertFalse(this.satellitePath.equals(this.modulatorPath));
		assertEquals(-2, this.satellitePath.compareTo(this.modulatorPath));
		assertEquals(0, this.satellitePath.compareTo(this.satellitePath));
		assertFalse(this.satellitePath.isElementPath());
		assertEquals(this.satellitePath, this.satellitePath.getDenotatorSubpath());
		assertNull(this.satellitePath.getElementSubpath());
		assertEquals(this.objects.SOUND_NOTE_FORM, this.satellitePath.getEndForm());
		assertNull(this.satellitePath.getModule());
		
		assertTrue(this.rationalTriplePath.isElementPath());
		assertEquals(new DenotatorPath(this.objects.RATIONAL_TRIPLE_FORM), this.rationalTriplePath.getDenotatorSubpath());
		assertEquals(this.rationalTriplePath, this.rationalTriplePath.getElementSubpath());
		assertEquals(this.objects.RATIONAL_TRIPLE_FORM, this.rationalTriplePath.getEndForm());
		assertEquals(this.objects.RATIONAL_TRIPLE_MODULE.getComponentModule(2), this.rationalTriplePath.getModule());
		
		assertTrue(this.realTriplesPath.isElementPath());
		assertEquals(new DenotatorPath(this.objects.REAL_TRIPLES_FORM, new int[]{5}), this.realTriplesPath.getDenotatorSubpath());
		assertEquals(new DenotatorPath(this.objects.REAL_TRIPLES_FORM, new int[]{1}), this.realTriplesPath.getElementSubpath());
		assertEquals(this.objects.REAL_TRIPLE_FORM, this.realTriplesPath.getEndForm());
		assertEquals(RRing.ring, this.realTriplesPath.getModule());
		
		DenotatorPath modulatorPath2 = new DenotatorPath(this.objects.SOUND_SCORE_FORM, new int[]{4,1,3,0,5,1,5,5});
		assertTrue(modulatorPath2.equalsExceptForPowersetIndices(this.modulatorPath));
		assertTrue(this.modulatorPath.equalsExceptForPowersetIndices(modulatorPath2));
		DenotatorPath modulatorPath3 = new DenotatorPath(this.objects.SOUND_SCORE_FORM, new int[]{2,1,0,1,5,1,5,0});
		assertFalse(modulatorPath3.equalsExceptForPowersetIndices(this.modulatorPath));
		assertFalse(this.modulatorPath.equalsExceptForPowersetIndices(modulatorPath3));
	}

	@Test
	void testWithIntegerPath() {
		DenotatorPath integerPath = new DenotatorPath(this.objects.INTEGER_FORM, new int[]{});
		assertNull(integerPath.getParentPath());
		assertNull(integerPath.getPowersetPath(0));
		assertNull(integerPath.getPowersetPath(0, this.objects.INTEGER_FORM));
	}

	@Test
	void testWithSatellitePath() {
		//assertFalse(this.satellitePath.isModulatorPath());
		assertEquals(new DenotatorPath(this.objects.SOUND_SCORE_FORM, new int[]{2,1,3,1,5}), this.satellitePath.getParentPath());
		assertEquals(new DenotatorPath(this.objects.SOUND_SCORE_FORM, new int[]{2,1,3,1}), this.satellitePath.getAnchorPowersetPath());
		//TODO: test ALL possible methods!!!!!
		assertEquals(5, this.satellitePath.getObjectIndex());
	}

	@Test
	void testSatelliteFamilyPaths() {
		ArrayList<DenotatorPath> parentPaths = new ArrayList<DenotatorPath>();
		parentPaths.add(new DenotatorPath(this.objects.SOUND_SCORE_FORM, new int[]{2,1,3}));
		parentPaths.add(new DenotatorPath(this.objects.SOUND_SCORE_FORM, new int[]{2}));
		parentPaths.add(new DenotatorPath(this.objects.SOUND_SCORE_FORM));
		//System.out.println(this.satellitePath.getAnchorPaths());
		assertEquals(parentPaths, this.satellitePath.getAnchorPaths());
		assertEquals(parentPaths.get(0), this.satellitePath.getAnchorPath());
		assertEquals(new DenotatorPath(this.objects.SOUND_SCORE_FORM, new int[]{2,1,3,1,5,1}), this.satellitePath.getFirstPowersetPath());
		assertEquals(new DenotatorPath(this.objects.SOUND_SCORE_FORM, new int[]{2,1,3,1,5,0,5}), this.satellitePath.getPowersetPath(1));
		assertTrue(this.satellitePath.isDirectSatelliteOf(parentPaths.get(0)));
		assertEquals(new DenotatorPath(this.objects.SOUND_SCORE_FORM, new int[]{2,1,3,1}), this.satellitePath.getPowersetPath(0, this.objects.SOUND_NODE_FORM));
		assertEquals(new DenotatorPath(this.objects.SOUND_SCORE_FORM, new int[]{2,1,3,1,5,1}), this.satellitePath.getPowersetPath(1, this.objects.SOUND_NODE_FORM));
		assertEquals(new DenotatorPath(this.objects.SOUND_SCORE_FORM, new int[]{2,1,3,1,5,1,0,1}), this.satellitePath.getPowersetPath(2, this.objects.SOUND_NODE_FORM));
		assertEquals(new DenotatorPath(this.objects.SOUND_SCORE_FORM, new int[]{2,1,3,0,5}), this.satellitePath.getPowersetPath(0, this.objects.SOUND_NOTE_FORM));
		assertEquals(new DenotatorPath(this.objects.SOUND_SCORE_FORM, new int[]{2,1,3,1,5,0,5}), this.satellitePath.getPowersetPath(1, this.objects.SOUND_NOTE_FORM));
		assertEquals(new DenotatorPath(this.objects.SOUND_SCORE_FORM, new int[]{2,1,3,1,5,0,5,0,5}), this.satellitePath.getPowersetPath(2, this.objects.SOUND_NOTE_FORM));
		//TODO TEST WITH GENERIC SOUND FORM!!!!
		DenotatorPath genericSoundPath = new DenotatorPath(this.objects.GENERIC_SOUND_FORM, new int[]{0});
		assertEquals(null, genericSoundPath.getPowersetPath(0, this.objects.GENERIC_SOUND_FORM));
		assertEquals(new DenotatorPath(this.objects.GENERIC_SOUND_FORM, new int[]{1}), genericSoundPath.getPowersetPath(1, this.objects.GENERIC_SOUND_FORM));
		assertEquals(new DenotatorPath(this.objects.GENERIC_SOUND_FORM, new int[]{1,0,1}), genericSoundPath.getPowersetPath(2, this.objects.GENERIC_SOUND_FORM));
	}

	@Test
	void testModulatorPath() {
		//assertTrue(this.modulatorPath.isModulatorPath());
		//assertEquals(this.modulatorPath, this.modulatorPath.getParentPath());
		assertEquals(new DenotatorPath(this.objects.SOUND_SCORE_FORM, new int[]{2,1,5,0,5,3,5}), this.modulatorPath.getAnchorPowersetPath());
		assertEquals(2, this.modulatorPath.getObjectIndex());
		assertEquals(new DenotatorPath(this.objects.SOUND_SCORE_FORM, new int[]{2,1,5,0,5,3}), this.modulatorPath.getAnchorPath());
	}

	@Test
	void testModulatorFamilyPaths() {
		ArrayList<DenotatorPath> parentPaths = new ArrayList<DenotatorPath>();
		parentPaths.add(new DenotatorPath(this.objects.SOUND_SCORE_FORM, new int[]{2,1,5,0,5,3}));
		parentPaths.add(new DenotatorPath(this.objects.SOUND_SCORE_FORM, new int[]{2,1,5}));
		parentPaths.add(new DenotatorPath(this.objects.SOUND_SCORE_FORM, new int[]{2}));
		parentPaths.add(new DenotatorPath(this.objects.SOUND_SCORE_FORM));
		assertEquals(parentPaths, this.modulatorPath.getAnchorPaths());
		assertEquals(parentPaths.get(0), this.modulatorPath.getAnchorPath());
		assertEquals(new DenotatorPath(this.objects.SOUND_SCORE_FORM, new int[]{2,1,5,0,5,3,5,2,5}), this.modulatorPath.getFirstPowersetPath());
		assertTrue(this.modulatorPath.isDirectSatelliteOf(parentPaths.get(0)));
	}

	@Test
	void testGetChild() {
		assertEquals(new DenotatorPath(this.objects.SOUND_SCORE_FORM, new int[]{2,1,3,1,5,1,4}), this.satellitePath.getSatellitePath(4,0));
		assertEquals(new DenotatorPath(this.objects.SOUND_SCORE_FORM, new int[]{2,1,3,1,5,0,5,4}), this.satellitePath.getSatellitePath(4,1));
		//of course, within a modulator, the only powerset is the one of its modulators!!
		assertEquals(new DenotatorPath(this.objects.SOUND_SCORE_FORM, new int[]{2,1,5,0,5,3,5,2,5,4}), this.modulatorPath.getSatellitePath(4,0));
	}

	@Test
	void testGetPowersetPath() {
		DenotatorPath node = new DenotatorPath(this.objects.SOUND_SCORE_FORM, new int[]{2});
		assertEquals(new DenotatorPath(this.objects.SOUND_SCORE_FORM, new int[]{2,1}), node.getPowersetPath(0));
		assertEquals(new DenotatorPath(this.objects.SOUND_SCORE_FORM, new int[]{2,0,5}), node.getPowersetPath(1));
	}

	@Test
	void testInConflictingColimitPositions() {
		DenotatorPath intOrRealPath0 = new DenotatorPath(this.objects.INTEGER_OR_REALS_FORM, new int[]{0,0});
		DenotatorPath intOrRealPath1 = new DenotatorPath(this.objects.INTEGER_OR_REALS_FORM, new int[]{0,1});
		assertTrue(intOrRealPath0.inConflictingColimitPositions(intOrRealPath1));
		assertTrue(intOrRealPath1.inConflictingColimitPositions(intOrRealPath0));
		assertFalse(intOrRealPath0.inConflictingColimitPositions(intOrRealPath0));
		
		DenotatorPath intOrRealPath2 = new DenotatorPath(this.objects.INTEGER_OR_REALS_FORM, new int[]{0});
		assertFalse(intOrRealPath0.inConflictingColimitPositions(intOrRealPath2));
		assertFalse(intOrRealPath1.inConflictingColimitPositions(intOrRealPath2));
		assertFalse(intOrRealPath2.inConflictingColimitPositions(intOrRealPath0));
		assertFalse(intOrRealPath2.inConflictingColimitPositions(intOrRealPath1));
		
		DenotatorPath intOrRealPath3 = new DenotatorPath(this.objects.INTEGER_OR_REALS_FORM, new int[]{});
		assertFalse(intOrRealPath0.inConflictingColimitPositions(intOrRealPath3));
		assertFalse(intOrRealPath1.inConflictingColimitPositions(intOrRealPath3));
		assertFalse(intOrRealPath3.inConflictingColimitPositions(intOrRealPath0));
		assertFalse(intOrRealPath3.inConflictingColimitPositions(intOrRealPath1));
	}

	@Test
	void testOtherStuffForThesisTest() throws LatrunculusCheckedException {
		SimpleForm onset = FormFactory.makeQModuleForm("Onset");
		Module eulerPitchSpace = new VectorModule<>(ZRing.ring, 3);
		SimpleForm eulerPitch = FormFactory.makeModuleForm("EulerPitch", eulerPitchSpace);
		Module loudnessSpace = new StringVectorModule<>(StringRingRepository.getRing(ZRing.ring), 1);
		SimpleForm loudness = FormFactory.makeModuleForm("Loudness", loudnessSpace);
		SimpleForm duration = FormFactory.makeQModuleForm("Duration");
		LimitForm eulerNote = FormFactory.makeLimitForm("EulerNote", onset, eulerPitch, loudness, duration);
		LimitForm rest = FormFactory.makeLimitForm("Rest", onset, duration);
		ColimitForm eulerNoteOrRest = FormFactory.makeColimitForm("EulerNoteOrRest", eulerNote, rest);
		PowerForm eulerScore = FormFactory.makePowerForm("EulerScore", eulerNoteOrRest);
		
		SimpleDenotator onset1 = DenoFactory.makeDenotator(onset, new Rational(0));
		List<ZInteger> pitchList = new ArrayList<>();
		pitchList.add(new ZInteger(1));
		pitchList.add(new ZInteger(0));
		pitchList.add(new ZInteger(-1));
		FreeElement<?, ZInteger> pitch1Element = new Vector<>(ZRing.ring, pitchList);
		SimpleDenotator pitch1 = DenoFactory.makeDenotator(eulerPitch, pitch1Element);
		SimpleDenotator loudness1 = DenoFactory.makeDenotator(loudness, "sfz");
		SimpleDenotator duration1 = DenoFactory.makeDenotator(duration, new Rational(1, 4));
		Denotator note1 = DenoFactory.makeDenotator(eulerNote, onset1, pitch1, loudness1, duration1);
		Denotator noteOne = DenoFactory.makeDenotator(eulerNoteOrRest, 0, note1);
		
		SimpleDenotator onsetAtBeat2 = DenoFactory.makeDenotator(onset, new Rational(1, 4));
		Denotator rest1 = DenoFactory.makeDenotator(rest, onsetAtBeat2, duration1);
		Denotator shortRest = DenoFactory.makeDenotator(eulerNoteOrRest, 1, rest1);
		
		SimpleDenotator onset2 = DenoFactory.makeDenotator(onset, new Rational(1, 2));
		List<ZInteger> pitch2List = new ArrayList<>();
		pitch2List.add(new ZInteger(-1));
		pitch2List.add(new ZInteger(1));
		pitch2List.add(new ZInteger(11));
		FreeElement<?, ZInteger> pitch2Element = new Vector<>(ZRing.ring, pitch2List);
		SimpleDenotator pitch2 = DenoFactory.makeDenotator(eulerPitch, pitch2Element);
		SimpleDenotator loudness2 = DenoFactory.makeDenotator(loudness, "ppp");
		SimpleDenotator duration2 = DenoFactory.makeDenotator(duration, new Rational(3, 2));
		Denotator note2 = DenoFactory.makeDenotator(eulerNote, onset2, pitch2, loudness2, duration2);
		Denotator noteTwo = DenoFactory.makeDenotator(eulerNoteOrRest, 0, note2);
		
		Denotator twoNoteScore = DenoFactory.makeDenotator(eulerScore, noteOne, shortRest, noteTwo);
		
		Denotator pitchOfNoteTwo = twoNoteScore.get(new int[]{1,0,1});
		//TODO DID THIS WORK AT SOME POINT???
		//pitchOfNoteTwo.display();
		//System.out.println(pitchOfNoteTwo.getElement(new int[]{0}));
		//int thirdValue = ((ZElement)twoNoteScore.getElement(new int[]{1,0,1,0})).getValue();
		//System.out.println(thirdValue);
		
		//twoNoteScore.display();
	}

}
