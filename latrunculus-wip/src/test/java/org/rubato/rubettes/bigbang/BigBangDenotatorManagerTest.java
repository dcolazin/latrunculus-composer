package org.rubato.rubettes.bigbang;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.vetronauta.latrunculus.core.exception.LatrunculusCheckedException;
import org.vetronauta.latrunculus.core.math.yoneda.denotator.Denotator;
import org.vetronauta.latrunculus.core.math.yoneda.denotator.LimitDenotator;
import org.vetronauta.latrunculus.core.math.yoneda.denotator.PowerDenotator;
import org.rubato.rubettes.bigbang.model.OperationPathResults;
import org.rubato.rubettes.bigbang.model.denotators.BigBangDenotatorManager;
import org.rubato.rubettes.bigbang.model.denotators.BigBangTransformation;
import org.rubato.rubettes.bigbang.model.denotators.TransformationPaths;
import org.rubato.rubettes.util.DenotatorPath;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

class BigBangDenotatorManagerTest {
	
	private BigBangDenotatorManager denotatorManager;
	private TestObjects objects;
	private TransformationPaths nodePaths, notePaths, rationalTriplesPaths, realTriplesPaths;
	
	@BeforeEach
	void setUp() {
		this.objects = new TestObjects();
		this.denotatorManager = new BigBangDenotatorManager();
		this.nodePaths = this.objects.createStandardTransformationPaths(
				this.objects.SOUND_NODE_FORM, new int[][]{{0,0},{0,1}});
		this.notePaths = this.objects.createStandardTransformationPaths(
				this.objects.SOUND_NOTE_FORM, new int[][]{{0},{1}});
		this.rationalTriplesPaths = this.objects.createStandardTransformationPaths(
				this.objects.RATIONAL_TRIPLE_FORM, new int[][]{{1},{2}});
		this.realTriplesPaths = this.objects.createStandardTransformationPaths(
				this.objects.REAL_TRIPLE_FORM, new int[][]{{0},{2}});
	}
	
	@Test
	void testAddComposition() {
		OperationPathResults pathResults = this.denotatorManager.setOrAddComposition(this.objects.multiLevelSoundScore);
		assertEquals(1, ((PowerDenotator)this.denotatorManager.getComposition()).getFactorCount());
		assertEquals(3, pathResults.getNewPaths().size());
		
		pathResults = this.denotatorManager.setOrAddComposition(this.objects.generator.createFlatSoundScore(
				new double[][]{{0,59,120,1,0,0},{1,63,116,1,0,0},{2,60,121,1,1,0}}));
		assertEquals(4, ((PowerDenotator)this.denotatorManager.getComposition()).getFactorCount());
		assertEquals(3, pathResults.getNewPaths().size());
		assertEquals(1, pathResults.getChangedPaths().size());
	}

	@Test
	@Disabled("todo")
	void testSetComposition() {
		//TODO test if newPaths work
		fail();
	}

	@Test
	void testAddNote() throws LatrunculusCheckedException {
		this.denotatorManager.setForm(this.objects.SOUND_SCORE_FORM);
		DenotatorPath topLevelPath = new DenotatorPath(this.objects.SOUND_SCORE_FORM);
		OperationPathResults pathResults = this.denotatorManager.addObjects(topLevelPath, this.generateNodeValueMapList(this.objects.NOTE0_VALUES));
		assertEquals(new DenotatorPath(this.objects.SOUND_SCORE_FORM, new int[]{0}), pathResults.getNewPaths().iterator().next());
		this.assertEqualNodes(this.objects.node0, this.denotatorManager.getComposition().get(new DenotatorPath(this.objects.SOUND_SCORE_FORM, new int[]{0}).toIntArray()));
		pathResults = this.denotatorManager.addObjects(topLevelPath, this.generateNodeValueMapList(this.objects.NOTE2_ABSOLUTE_VALUES));
		assertEquals(new DenotatorPath(this.objects.SOUND_SCORE_FORM, new int[]{1}), pathResults.getNewPaths().iterator().next());
		this.assertEqualNodes(this.objects.node2Absolute, this.denotatorManager.getComposition().get(new DenotatorPath(this.objects.SOUND_SCORE_FORM, new int[]{1}).toIntArray()));
		pathResults = this.denotatorManager.addObjects(topLevelPath, this.generateNodeValueMapList(this.objects.NOTE1_ABSOLUTE_VALUES));
		assertEquals(new DenotatorPath(this.objects.SOUND_SCORE_FORM, new int[]{1}), pathResults.getNewPaths().iterator().next());
		this.assertEqualNodes(this.objects.node1Absolute, this.denotatorManager.getComposition().get(new DenotatorPath(this.objects.SOUND_SCORE_FORM, new int[]{1}).toIntArray()));
		PowerDenotator composition = (PowerDenotator)this.denotatorManager.getComposition();
		assertEquals(3, composition.getFactorCount());
	}

	@Test
	void testAddNotes() {
		this.denotatorManager.setOrAddComposition(this.objects.multiLevelSoundScore);
		List<DenotatorPath> anchorPaths = new ArrayList<>();
		anchorPaths.add(new DenotatorPath(this.objects.SOUND_SCORE_FORM, new int[]{}));
		anchorPaths.add(new DenotatorPath(this.objects.SOUND_SCORE_FORM, new int[]{0,0}));
		anchorPaths.add(new DenotatorPath(this.objects.SOUND_SCORE_FORM, new int[]{0,1,0,0}));
		List<Denotator> notes = new ArrayList<>();
		notes.add(this.objects.node1Absolute);
		notes.add(this.objects.node2Absolute);
		notes.add(this.objects.node0);
		//add more objects to the multilevel macroscore and verify their new paths
		this.denotatorManager.addObjects(notes, anchorPaths, new int[]{0,0,0});
		OperationPathResults pathResults = this.denotatorManager.getPathResults();
		Set<DenotatorPath> expectedNewPaths = new TreeSet<>();
		expectedNewPaths.add(new DenotatorPath(this.objects.SOUND_SCORE_FORM, new int[]{1}));
		expectedNewPaths.add(new DenotatorPath(this.objects.SOUND_SCORE_FORM, new int[]{0,1,1}));
		expectedNewPaths.add(new DenotatorPath(this.objects.SOUND_SCORE_FORM, new int[]{0,1,0,1,0}));
		assertEquals(expectedNewPaths, pathResults.getNewPaths());
	}

	@Test
	void testGetAbsoluteNote() throws LatrunculusCheckedException {
		this.denotatorManager.setOrAddComposition(this.objects.multiLevelSoundScore);
		DenotatorPath nodePath = new DenotatorPath(this.objects.SOUND_SCORE_FORM, new int[]{0,1,0,1,0});
		this.assertEqualNodes(this.objects.node2Relative, this.denotatorManager.getComposition().get(nodePath.toIntArray()));
		this.assertEqualNodes(this.objects.node2Absolute, this.denotatorManager.getAbsoluteObject(nodePath));
	}

	@Test
	void testRemoveNotes() throws LatrunculusCheckedException {
		this.denotatorManager.setOrAddComposition(this.objects.flatSoundScore);
		assertEquals(3, ((PowerDenotator) this.denotatorManager.getComposition()).getFactorCount());
		List<DenotatorPath> paths = new ArrayList<>();
		paths.add(new DenotatorPath(this.objects.SOUND_SCORE_FORM, new int[]{1}));
		//remove and check composition and values
		Denotator removedNote = this.denotatorManager.removeObjects2(paths).get(0);
		OperationPathResults pathResults = this.denotatorManager.getPathResults();
		assertEquals(2, ((PowerDenotator) this.denotatorManager.getComposition()).getFactorCount());
		this.assertEqualNodes(removedNote, this.objects.node1Absolute);
		//check paths results
		Set<DenotatorPath> expectedRemovedPaths = new TreeSet<>();
		expectedRemovedPaths.add(new DenotatorPath(this.objects.SOUND_SCORE_FORM, new int[]{1}));
		assertEquals(expectedRemovedPaths, pathResults.getRemovedPaths());
		Map<DenotatorPath,DenotatorPath> expectedChangedPaths = new TreeMap<>();
		expectedChangedPaths.put(new DenotatorPath(this.objects.SOUND_SCORE_FORM, new int[]{2}), new DenotatorPath(this.objects.SOUND_SCORE_FORM, new int[]{1}));
		assertEquals(expectedChangedPaths, pathResults.getChangedPaths());
	}

	@Test
	void testRemoveSatelliteNote() throws LatrunculusCheckedException {
		this.denotatorManager.setOrAddComposition(this.objects.multiLevelSoundScore);
		assertEquals(1, ((PowerDenotator) this.denotatorManager.getComposition().get(new int[]{0, 1, 0, 1})).getFactorCount());
		List<DenotatorPath> paths = new ArrayList<>();
		paths.add(new DenotatorPath(this.objects.SOUND_SCORE_FORM, new int[]{0,1,0,1,0}));
		//remove and check composition and values
		Denotator removedNode = this.denotatorManager.removeObjects2(paths).get(0);
		OperationPathResults pathResults = this.denotatorManager.getPathResults();
		assertEquals(0, ((PowerDenotator) this.denotatorManager.getComposition().get(new int[]{0, 1, 0, 1})).getFactorCount());
		this.assertEqualNodes(this.objects.node2Absolute, removedNode);
		//check paths results
		Set<DenotatorPath> expectedRemovedPaths = new TreeSet<>();
		expectedRemovedPaths.add(new DenotatorPath(this.objects.SOUND_SCORE_FORM, new int[]{0,1,0,1,0}));
		assertEquals(expectedRemovedPaths, pathResults.getRemovedPaths());
		Map<DenotatorPath,DenotatorPath> expectedChangedPaths = new TreeMap<>();
		assertEquals(expectedChangedPaths, pathResults.getChangedPaths());
	}

	@Test
	void testMapNodesFlat() {
		this.denotatorManager.setOrAddComposition(this.objects.flatSoundScore);
		BigBangTransformation translation = this.objects.makeTranslation(-1,-2, this.nodePaths);
		Set<DenotatorPath> notePaths = this.objects.makeNotePaths(new int[][]{{0},{2}});
		OperationPathResults pathResults = this.denotatorManager.addTransformation(notePaths, null, translation);
		Denotator expectedResult = this.objects.generator.createFlatSoundScore(new double[][]{{-1,58,120,1,0,0},{1,63,116,1,0,0},{1,58,121,1,1,0}});
		assertEquals(expectedResult, this.denotatorManager.getComposition());
		Map<DenotatorPath,DenotatorPath> expectedChangedPaths = new TreeMap<>();
		expectedChangedPaths.put(new DenotatorPath(this.objects.SOUND_SCORE_FORM, new int[]{1}), new DenotatorPath(this.objects.SOUND_SCORE_FORM, new int[]{2}));
		expectedChangedPaths.put(new DenotatorPath(this.objects.SOUND_SCORE_FORM, new int[]{2}), new DenotatorPath(this.objects.SOUND_SCORE_FORM, new int[]{1}));
		assertEquals(expectedChangedPaths, pathResults.getChangedPaths());
	}

	@Test
	void testProject() {
		//scale so that notes projected and made equal
		this.denotatorManager.setOrAddComposition(this.objects.generator.createFlatSoundScore(
				new double[][]{{0,60,120,1,0,0},{1,60,120,1,0,0},{2,60,121,1,0,0}}));
		BigBangTransformation scaling = this.objects.makeScaling(0, 1, this.nodePaths);
		Set<DenotatorPath> notePaths = this.objects.makeNotePaths(new int[][]{{0},{1},{2}});
		OperationPathResults pathResults = this.denotatorManager.addTransformation(notePaths, null, scaling);
		Denotator expectedResult = this.objects.generator.createFlatSoundScore(new double[][]{{0,60,120,1,0,0},{0,60,121,1,0,0}});
		assertEquals(expectedResult, this.denotatorManager.getComposition());
		
		Map<DenotatorPath,DenotatorPath> expectedChangedPaths = new TreeMap<>();
		expectedChangedPaths.put(new DenotatorPath(this.objects.SOUND_SCORE_FORM, new int[]{1}), new DenotatorPath(this.objects.SOUND_SCORE_FORM, new int[]{0}));
		expectedChangedPaths.put(new DenotatorPath(this.objects.SOUND_SCORE_FORM, new int[]{2}), new DenotatorPath(this.objects.SOUND_SCORE_FORM, new int[]{1}));
		assertEquals(expectedChangedPaths, pathResults.getChangedPaths());
		
		Set<DenotatorPath> expectedRemovedPaths = new TreeSet<>();
		expectedRemovedPaths.add(new DenotatorPath(this.objects.SOUND_SCORE_FORM, new int[]{0}));
		assertEquals(expectedRemovedPaths, pathResults.getRemovedPaths());
	}

	@Test
	void testCopyAndMapNodesFlat() {
		this.denotatorManager.setOrAddComposition(this.objects.flatSoundScore);
		BigBangTransformation translation = this.objects.makeTranslation(-1,-2, this.nodePaths);
		translation.setCopyAndMap(true);
		Set<DenotatorPath> notePaths = this.objects.makeNotePaths(new int[][]{{0},{2}});
		OperationPathResults pathResults = this.denotatorManager.addTransformation(notePaths, null, translation);
		Map<DenotatorPath,DenotatorPath> expectedChangedPaths = new TreeMap<>();
		expectedChangedPaths.put(new DenotatorPath(this.objects.SOUND_SCORE_FORM, new int[]{0}), new DenotatorPath(this.objects.SOUND_SCORE_FORM, new int[]{1}));
		expectedChangedPaths.put(new DenotatorPath(this.objects.SOUND_SCORE_FORM, new int[]{1}), new DenotatorPath(this.objects.SOUND_SCORE_FORM, new int[]{3}));
		expectedChangedPaths.put(new DenotatorPath(this.objects.SOUND_SCORE_FORM, new int[]{2}), new DenotatorPath(this.objects.SOUND_SCORE_FORM, new int[]{4}));
		assertEquals(expectedChangedPaths, pathResults.getChangedPaths());
		Set<DenotatorPath> expectedNewPaths = new TreeSet<>();
		expectedNewPaths.add(new DenotatorPath(this.objects.SOUND_SCORE_FORM, new int[]{0}));
		expectedNewPaths.add(new DenotatorPath(this.objects.SOUND_SCORE_FORM, new int[]{2}));
		assertEquals(expectedNewPaths, pathResults.getNewPaths());
		Denotator expectedResult = this.objects.generator.createFlatSoundScore(new double[][]{{-1,58,120,1,0,0},{0,60,120,1,0,0},{1,58,121,1,1,0},{1,63,116,1,0,0},{2,60,121,1,1,0}});
		assertEquals(expectedResult, this.denotatorManager.getComposition());
	}

	@Test
	void testMapRationalTriples() throws LatrunculusCheckedException {
		this.denotatorManager.setOrAddComposition(this.objects.rationalTriples);
		BigBangTransformation translation = this.objects.makeTranslation(-1,-2, this.rationalTriplesPaths);
		Set<DenotatorPath> triplesPath = new TreeSet<>();
		triplesPath.add(new DenotatorPath(this.objects.RATIONAL_TRIPLES_FORM, new int[]{0}));
		triplesPath.add(new DenotatorPath(this.objects.RATIONAL_TRIPLES_FORM, new int[]{1}));
		triplesPath.add(new DenotatorPath(this.objects.RATIONAL_TRIPLES_FORM, new int[]{3}));
		OperationPathResults pathResults = this.denotatorManager.addTransformation(triplesPath, null, translation);
		Map<DenotatorPath,DenotatorPath> expectedChangedPaths = new TreeMap<>();
		assertEquals(expectedChangedPaths,pathResults.getChangedPaths());
		Denotator expectedTriples = this.objects.createRationalTriples(new double[][]{{1,1,1},{4,2,-1},{2,0,3},{3,4,2}});
		assertEquals(expectedTriples, this.denotatorManager.getComposition());
	}

	@Test
	void testMapRealTriples() throws LatrunculusCheckedException {
		this.denotatorManager.setOrAddComposition(this.objects.realTriples);
		BigBangTransformation translation = this.objects.makeTranslation(-2,-3, this.realTriplesPaths);
		Set<DenotatorPath> triplesPath = new TreeSet<>();
		triplesPath.add(new DenotatorPath(this.objects.REAL_TRIPLES_FORM, new int[]{1}));
		triplesPath.add(new DenotatorPath(this.objects.REAL_TRIPLES_FORM, new int[]{2}));
		OperationPathResults pathResults = this.denotatorManager.addTransformation(triplesPath, null, translation);
		Map<DenotatorPath,DenotatorPath> expectedChangedPaths = new TreeMap<>();
		expectedChangedPaths.put(new DenotatorPath(this.objects.REAL_TRIPLES_FORM, new int[]{1}), new DenotatorPath(this.objects.REAL_TRIPLES_FORM, new int[]{0}));
		expectedChangedPaths.put(new DenotatorPath(this.objects.REAL_TRIPLES_FORM, new int[]{0}), new DenotatorPath(this.objects.REAL_TRIPLES_FORM, new int[]{1}));
		assertEquals(expectedChangedPaths, pathResults.getChangedPaths());
		Denotator expectedTriples = this.objects.createRealTriples(new double[][]{{0,1,2},{1,2,3},{2,3,-2}});
		assertEquals(expectedTriples, this.denotatorManager.getComposition());
	}

	@Test
	void testMapRealTriple() {
		this.denotatorManager.setOrAddComposition(this.objects.realTriples.getFactor(1));
		BigBangTransformation translation = this.objects.makeTranslation(-2,-3, this.realTriplesPaths);
		Set<DenotatorPath> emptyPath = new TreeSet<>();
		emptyPath.add(new DenotatorPath(this.objects.REAL_TRIPLE_FORM, new int[]{}));
		//System.out.println("\n\n");
		OperationPathResults pathResults = this.denotatorManager.addTransformation(emptyPath, null, translation);
		assertTrue(pathResults.getChangedPaths().isEmpty());
		assertEquals(emptyPath, pathResults.getNewPaths());
		assertTrue(pathResults.getRemovedPaths().isEmpty());
		Denotator expectedTriple = this.objects.createRealTriple(new double[]{0,1,2});
		this.objects.assertEqualNonPowerDenotators(expectedTriple, this.denotatorManager.getComposition());
	}

	@Test
	void testMapNodesFlatSequential() {
		this.denotatorManager.setOrAddComposition(this.objects.flatSoundScore);
		BigBangTransformation translation = this.objects.makeTranslation(3,5, this.nodePaths);
		Set<DenotatorPath> notePaths = this.objects.makeNotePaths(new int[][]{{0},{2}});
		OperationPathResults pathResults = this.denotatorManager.addTransformation(notePaths, null, translation);
		Map<DenotatorPath,DenotatorPath> expectedChangedPaths = new TreeMap<>();
		expectedChangedPaths.put(new DenotatorPath(this.objects.SOUND_SCORE_FORM, new int[]{0}), new DenotatorPath(this.objects.SOUND_SCORE_FORM, new int[]{1}));
		expectedChangedPaths.put(new DenotatorPath(this.objects.SOUND_SCORE_FORM, new int[]{1}), new DenotatorPath(this.objects.SOUND_SCORE_FORM, new int[]{0}));
		assertEquals(expectedChangedPaths, pathResults.getChangedPaths());
	}

	@Test
	void testMapNodesMultiLevel() throws LatrunculusCheckedException {
		this.objects.multiLevelSoundScore.appendFactor(this.objects.generator.createNodeDenotator(this.objects.note2Absolute));
		this.denotatorManager.setOrAddComposition(this.objects.multiLevelSoundScore);
		BigBangTransformation translation = this.objects.makeTranslation(-2, -1, this.nodePaths);
		Set<DenotatorPath> nodePaths = this.objects.makeNotePaths(new int[][]{{1},{0,1,0}});
		OperationPathResults pathResults = this.denotatorManager.addTransformation(nodePaths, null, translation);
		Map<DenotatorPath,DenotatorPath> expectedChangedPaths = new TreeMap<>();
		expectedChangedPaths.put(new DenotatorPath(this.objects.SOUND_SCORE_FORM, new int[]{1}), new DenotatorPath(this.objects.SOUND_SCORE_FORM, new int[]{0}));
		expectedChangedPaths.put(new DenotatorPath(this.objects.SOUND_SCORE_FORM, new int[]{0}), new DenotatorPath(this.objects.SOUND_SCORE_FORM, new int[]{1}));
		assertEquals(expectedChangedPaths, pathResults.getChangedPaths());
		
		LimitDenotator expectedNode = this.objects.createMultilevelNode(new double[][]{{-1,2,-4,0,0,0},{1,-3,5,0,1,0}});
		this.objects.assertEqualNonPowerDenotators(expectedNode, this.denotatorManager.getComposition().get(new int[]{1,1,0}));
		
		nodePaths = this.objects.makeNotePaths(new int[][]{{0},{1,1,0}});
		pathResults = this.denotatorManager.addTransformation(nodePaths, null, translation);
		expectedChangedPaths = new TreeMap<>();
		assertEquals(expectedChangedPaths, pathResults.getChangedPaths());
		expectedNode = this.objects.createMultilevelNode(new double[][]{{-3,1,-4,0,0,0},{1,-3,5,0,1,0}});
		this.objects.assertEqualNonPowerDenotators(expectedNode, this.denotatorManager.getComposition().get(new int[]{1,1,0}));
	}

	@Test
	void testMapSoundScoreModulators() throws LatrunculusCheckedException {
		this.denotatorManager.setOrAddComposition(this.objects.flatSoundScore);
		Set<DenotatorPath> paths = new TreeSet<>();
		paths.add(new DenotatorPath(this.objects.SOUND_SCORE_FORM, new int[]{1}));
		//build modulator structure
		this.denotatorManager.buildSatelliteObjects(paths, new DenotatorPath(this.objects.SOUND_SCORE_FORM, new int[]{0}), 1);
		OperationPathResults pathResults = this.denotatorManager.buildSatelliteObjects(paths, new DenotatorPath(this.objects.SOUND_SCORE_FORM, new int[]{0,0,5,0}), 0);
		Map<DenotatorPath,DenotatorPath> expectedChangedPaths = new TreeMap<>();
		expectedChangedPaths.put(new DenotatorPath(this.objects.SOUND_SCORE_FORM, new int[]{1}), new DenotatorPath(this.objects.SOUND_SCORE_FORM, new int[]{0,0,5,0,5,0}));
		assertEquals(expectedChangedPaths, pathResults.getChangedPaths());
		Set<DenotatorPath> expectedEmptyPaths = new TreeSet<>();
		assertEquals(expectedEmptyPaths, pathResults.getNewPaths());
		assertEquals(expectedEmptyPaths, pathResults.getRemovedPaths());
		
		BigBangTransformation translation = this.objects.makeTranslation(-2, -1, this.notePaths);
		Set<DenotatorPath> nodePaths = this.objects.makeNotePaths(new int[][]{{0,0,5,0,5,0}});
		pathResults = this.denotatorManager.addTransformation(nodePaths, null, translation);
		expectedChangedPaths = new TreeMap<>();
		assertEquals(expectedChangedPaths, pathResults.getChangedPaths());
		LimitDenotator expectedNote = this.objects.generator.createNoteDenotator(new double[]{-1,-4,5,0,1,0});
		this.objects.assertEqualNonPowerDenotators(expectedNote, this.denotatorManager.getComposition().get(new int[]{0,0,5,0,5,0}));
	}

	@Test
	void testBuildSatellites() throws LatrunculusCheckedException {
		this.denotatorManager.setOrAddComposition(this.objects.flatSoundScore);
		Set<DenotatorPath> paths = new TreeSet<>();
		paths.add(new DenotatorPath(this.objects.SOUND_SCORE_FORM, new int[]{1}));
		//build first satellite and check if it's there
		this.denotatorManager.buildSatelliteObjects(paths, new DenotatorPath(this.objects.SOUND_SCORE_FORM, new int[]{0}), 0);
		this.objects.assertEqualNonPowerDenotators(this.objects.node1Relative, this.denotatorManager.getComposition().get(new int[]{0,1,0}));
		//build second satellite and check if it's there
		assertEquals(0, ((PowerDenotator) this.denotatorManager.getComposition().get(new int[]{0, 1, 0, 1})).getFactorCount());
		OperationPathResults pathResults = this.denotatorManager.buildSatelliteObjects(paths, new DenotatorPath(this.objects.SOUND_SCORE_FORM, new int[]{0,1,0}), 0);
		Map<DenotatorPath,DenotatorPath> expectedChangedPaths = new TreeMap<>();
		expectedChangedPaths.put(new DenotatorPath(this.objects.SOUND_SCORE_FORM, new int[]{1}), new DenotatorPath(this.objects.SOUND_SCORE_FORM, new int[]{0,1,0,1,0}));
		assertEquals(expectedChangedPaths, pathResults.getChangedPaths());
		assertEquals(1, ((PowerDenotator) this.denotatorManager.getComposition().get(new int[]{0, 1, 0, 1})).getFactorCount());
		this.objects.assertEqualNonPowerDenotators(this.objects.node2Relative, this.denotatorManager.getComposition().get(new int[]{0,1,0,1,0}));
	}

	@Test
	void testBuildSoundScoreModulators() throws LatrunculusCheckedException {
		this.denotatorManager.setOrAddComposition(this.objects.flatSoundScore);
		Set<DenotatorPath> paths = new TreeSet<>();
		paths.add(new DenotatorPath(this.objects.SOUND_SCORE_FORM, new int[]{1}));
		//build first modulator and check if it's there
		this.denotatorManager.buildSatelliteObjects(paths, new DenotatorPath(this.objects.SOUND_SCORE_FORM, new int[]{0}), 1);
		this.objects.assertEqualNonPowerDenotators(this.objects.note1Relative, this.denotatorManager.getComposition().get(new int[]{0,0,5,0}));
		//build second modulator and check if it's there
		assertEquals(0, ((PowerDenotator) this.denotatorManager.getComposition().get(new int[]{0, 0, 5, 0, 5})).getFactorCount());
		OperationPathResults pathResults = this.denotatorManager.buildSatelliteObjects(paths, new DenotatorPath(this.objects.SOUND_SCORE_FORM, new int[]{0,0,5,0}), 0);
		Map<DenotatorPath,DenotatorPath> expectedChangedPaths = new TreeMap<>();
		expectedChangedPaths.put(new DenotatorPath(this.objects.SOUND_SCORE_FORM, new int[]{1}), new DenotatorPath(this.objects.SOUND_SCORE_FORM, new int[]{0,0,5,0,5,0}));
		assertEquals(expectedChangedPaths, pathResults.getChangedPaths());
		Set<DenotatorPath> expectedEmptyPaths = new TreeSet<>();
		assertEquals(expectedEmptyPaths, pathResults.getNewPaths());
		assertEquals(expectedEmptyPaths, pathResults.getRemovedPaths());

		assertEquals(1, ((PowerDenotator) this.denotatorManager.getComposition().get(new int[]{0, 0, 5, 0, 5})).getFactorCount());
		Denotator addedModulator = this.denotatorManager.getComposition().get(new int[]{0,0,5,0,5,0});
		assertEquals(this.objects.generator.getSoundNoteForm(), addedModulator.getForm());
		this.objects.assertEqualNonPowerDenotators(this.objects.note2Relative, this.denotatorManager.getComposition().get(new int[]{0,0,5,0,5,0}));
	}

	@Test
	void testFlatten() throws LatrunculusCheckedException {
		this.denotatorManager.setOrAddComposition(this.objects.multiLevelSoundScore);
		assertEquals(1, ((PowerDenotator) this.denotatorManager.getComposition().get(new int[]{0, 1})).getFactorCount());
		assertEquals(1, ((PowerDenotator) this.denotatorManager.getComposition().get(new int[]{0, 1, 0, 1})).getFactorCount());
		TreeSet<DenotatorPath> paths = new TreeSet<>();
		paths.add(new DenotatorPath(this.objects.SOUND_SCORE_FORM, new int[]{0,1,0,1,0}));
		OperationPathResults pathResults = this.denotatorManager.flattenObjects(paths);
		assertEquals(2, ((PowerDenotator) this.denotatorManager.getComposition().get(new int[]{0, 1})).getFactorCount());
		Map<DenotatorPath,DenotatorPath> expectedChangedPaths = new TreeMap<>();
		expectedChangedPaths.put(new DenotatorPath(this.objects.SOUND_SCORE_FORM, new int[]{0,1,0,1,0}), new DenotatorPath(this.objects.SOUND_SCORE_FORM, new int[]{0,1,1}));
		assertEquals(expectedChangedPaths, pathResults.getChangedPaths());
		Set<DenotatorPath> expectedEmptyPaths = new TreeSet<>();
		assertEquals(expectedEmptyPaths, pathResults.getNewPaths());
		assertEquals(expectedEmptyPaths, pathResults.getRemovedPaths());
	}

	@Test
	void testFlatten2() throws LatrunculusCheckedException {
		this.denotatorManager.setOrAddComposition(this.objects.multiLevelSoundScore);
		TreeSet<DenotatorPath> paths = new TreeSet<>();
		paths.add(new DenotatorPath(this.objects.SOUND_SCORE_FORM, new int[]{0,1,0}));
		//flatten the first path and check denotators
		OperationPathResults pathResults = this.denotatorManager.flattenObjects(paths);
		this.objects.assertEqualNonPowerDenotators(this.objects.node0, this.denotatorManager.getComposition().get(new int[]{0}));
		LimitDenotator expectedNode = this.objects.createMultilevelNode(new double[][]{{1,63,116,1,0,0},{1,-3,5,0,1,0}});
		this.objects.assertEqualNonPowerDenotators(expectedNode, this.denotatorManager.getComposition().get(new int[]{1}));
		//check pathResults
		Map<DenotatorPath,DenotatorPath> expectedChangedPaths = new TreeMap<>();
		expectedChangedPaths.put(new DenotatorPath(this.objects.SOUND_SCORE_FORM, new int[]{0,1,0}), new DenotatorPath(this.objects.SOUND_SCORE_FORM, new int[]{1}));
		assertEquals(expectedChangedPaths, pathResults.getChangedPaths());
		Set<DenotatorPath> expectedEmptyPaths = new TreeSet<>();
		assertEquals(expectedEmptyPaths, pathResults.getNewPaths());
		assertEquals(expectedEmptyPaths, pathResults.getRemovedPaths());
	}

	@Test
	void testShapeNotes() throws LatrunculusCheckedException {
		this.denotatorManager.setOrAddComposition(this.objects.flatSoundScore);
		Set<DenotatorPath> paths = new TreeSet<>();
		paths.add(new DenotatorPath(this.objects.SOUND_SCORE_FORM, new int[]{0}));
		paths.add(new DenotatorPath(this.objects.SOUND_SCORE_FORM, new int[]{1}));
		paths.add(new DenotatorPath(this.objects.SOUND_SCORE_FORM, new int[]{2}));
		
		TreeMap<Double,Double> shapingLocations = new TreeMap<>();
		shapingLocations.put(-0.3, 69.0);
		shapingLocations.put(0.5, 71.0);
		shapingLocations.put(4.0, 99.0);
		this.denotatorManager.shapeObjects(paths, shapingLocations, Collections.singletonList(this.nodePaths), false);
		
		LimitDenotator expectedNote = this.objects.generator.createNodeDenotator(new double[]{0,69,120,1,0,0});
		this.objects.assertEqualNonPowerDenotators(expectedNote, this.denotatorManager.getComposition().get(new int[]{0}));
		expectedNote = this.objects.generator.createNodeDenotator(new double[]{1,71,116,1,0,0});
		this.objects.assertEqualNonPowerDenotators(expectedNote, this.denotatorManager.getComposition().get(new int[]{1}));
		expectedNote = this.objects.generator.createNodeDenotator(new double[]{2,60,121,1,1,0});
		this.objects.assertEqualNonPowerDenotators(expectedNote, this.denotatorManager.getComposition().get(new int[]{2}));
	}

	@Test
	void testShapeRationalTriples() throws LatrunculusCheckedException {
		this.denotatorManager.setOrAddComposition(this.objects.rationalTriples);
		Set<DenotatorPath> paths = new TreeSet<>();
		paths.add(new DenotatorPath(this.objects.RATIONAL_TRIPLES_FORM, new int[]{0}));
		paths.add(new DenotatorPath(this.objects.RATIONAL_TRIPLES_FORM, new int[]{1}));
		paths.add(new DenotatorPath(this.objects.RATIONAL_TRIPLES_FORM, new int[]{2}));
		paths.add(new DenotatorPath(this.objects.RATIONAL_TRIPLES_FORM, new int[]{3}));
		
		TreeMap<Double,Double> shapingLocations = new TreeMap<>();
		shapingLocations.put(0.7, 1.0);
		shapingLocations.put(2.3, 11.0);
		shapingLocations.put(4.1, 99.0);
		shapingLocations.put(5.0, 0.0);
		this.denotatorManager.shapeObjects(paths, shapingLocations, Collections.singletonList(this.rationalTriplesPaths), false);
		
		Denotator expectedTriple = this.objects.createRationalTriple(new double[]{1,2,11});
		this.objects.assertEqualNonPowerDenotators(expectedTriple, this.denotatorManager.getComposition().get(new int[]{0}));
		expectedTriple = this.objects.createRationalTriple(new double[]{2,1,1});
		this.objects.assertEqualNonPowerDenotators(expectedTriple, this.denotatorManager.getComposition().get(new int[]{1}));
		expectedTriple = this.objects.createRationalTriple(new double[]{3,4,99});
		this.objects.assertEqualNonPowerDenotators(expectedTriple, this.denotatorManager.getComposition().get(new int[]{2}));
		expectedTriple = this.objects.createRationalTriple(new double[]{4,3,1});
		this.objects.assertEqualNonPowerDenotators(expectedTriple, this.denotatorManager.getComposition().get(new int[]{3}));
	}

	@Test
	void testShapeRealTriples() throws LatrunculusCheckedException {
		this.denotatorManager.setOrAddComposition(this.objects.realTriples);
		Set<DenotatorPath> paths = new TreeSet<>();
		paths.add(new DenotatorPath(this.objects.REAL_TRIPLES_FORM, new int[]{0}));
		paths.add(new DenotatorPath(this.objects.REAL_TRIPLES_FORM, new int[]{1}));
		paths.add(new DenotatorPath(this.objects.REAL_TRIPLES_FORM, new int[]{2}));
		
		TreeMap<Double,Double> shapingLocations = new TreeMap<>();
		shapingLocations.put(1.2, 1.0);
		shapingLocations.put(3.0, 11.0);
		shapingLocations.put(2.1, 99.0);
		this.denotatorManager.shapeObjects(paths, shapingLocations, Collections.singletonList(this.realTriplesPaths), false);
		
		Denotator expectedTriple = this.objects.createRealTriple(new double[]{1,2,1});
		this.objects.assertEqualNonPowerDenotators(expectedTriple, this.denotatorManager.getComposition().get(new int[]{0}));
		expectedTriple = this.objects.createRealTriple(new double[]{2,1,99});
		this.objects.assertEqualNonPowerDenotators(expectedTriple, this.denotatorManager.getComposition().get(new int[]{1}));
		expectedTriple = this.objects.createRealTriple(new double[]{4,3,1});
		this.objects.assertEqualNonPowerDenotators(expectedTriple, this.denotatorManager.getComposition().get(new int[]{2}));
	}

	@Test
	@Disabled("old bug in domain ConstantMorphism made this work well...")
	void testWallpaper2() {
		//test adding transformation that displaces motif
		this.denotatorManager.setOrAddComposition(this.objects.flatSoundScore);
		Set<DenotatorPath> notePaths = this.objects.makeNotePaths(new int[][]{{0},{1}});
		this.denotatorManager.addWallpaperDimension(notePaths, 0, 1);
		BigBangTransformation translation = this.objects.makeTranslation(-1,-1, this.nodePaths);
		OperationPathResults pathResults = this.denotatorManager.addTransformation(notePaths, null, translation);
		Set<DenotatorPath> expectedNewPaths = new TreeSet<>();
		expectedNewPaths.add(new DenotatorPath(this.objects.SOUND_SCORE_FORM, new int[]{0}));
		expectedNewPaths.add(new DenotatorPath(this.objects.SOUND_SCORE_FORM, new int[]{2}));
		assertEquals(expectedNewPaths, pathResults.getNewPaths());
		Map<DenotatorPath,DenotatorPath> expectedChangedPaths = new TreeMap<>();
		expectedChangedPaths.put(new DenotatorPath(this.objects.SOUND_SCORE_FORM, new int[]{0}), new DenotatorPath(this.objects.SOUND_SCORE_FORM, new int[]{1}));
		expectedChangedPaths.put(new DenotatorPath(this.objects.SOUND_SCORE_FORM, new int[]{1}), new DenotatorPath(this.objects.SOUND_SCORE_FORM, new int[]{3}));
		expectedChangedPaths.put(new DenotatorPath(this.objects.SOUND_SCORE_FORM, new int[]{2}), new DenotatorPath(this.objects.SOUND_SCORE_FORM, new int[]{4}));
		assertEquals(expectedChangedPaths, pathResults.getChangedPaths());
	}

	@Test
	@Disabled("old bug in domain ConstantMorphism made this work well...")
	void testWallpaperWithSatellites() {
		//test adding transformation that displaces motif
		this.denotatorManager.setOrAddComposition(this.objects.multiLevelSoundScore);
		Set<DenotatorPath> notePaths = this.objects.makeNotePaths(new int[][]{{0}});
		this.denotatorManager.addWallpaperDimension(notePaths, 0, 2);
		BigBangTransformation translation = this.objects.makeTranslation(-1,-1, this.nodePaths);
		OperationPathResults pathResults = this.denotatorManager.addTransformation(notePaths, null, translation);
		//should include new satellites as new paths too!
		Set<DenotatorPath> expectedNewPaths = new TreeSet<>();
		expectedNewPaths.add(new DenotatorPath(this.objects.SOUND_SCORE_FORM, new int[]{0}));
		expectedNewPaths.add(new DenotatorPath(this.objects.SOUND_SCORE_FORM, new int[]{1}));
		expectedNewPaths.add(new DenotatorPath(this.objects.SOUND_SCORE_FORM, new int[]{0,1,0}));
		expectedNewPaths.add(new DenotatorPath(this.objects.SOUND_SCORE_FORM, new int[]{0,1,0,1,0}));
		expectedNewPaths.add(new DenotatorPath(this.objects.SOUND_SCORE_FORM, new int[]{1,1,0}));
		expectedNewPaths.add(new DenotatorPath(this.objects.SOUND_SCORE_FORM, new int[]{1,1,0,1,0}));
		assertEquals(expectedNewPaths, pathResults.getNewPaths());
		//should include only anchor as changed path
		Map<DenotatorPath,DenotatorPath> expectedChangedPaths = new TreeMap<>();
		expectedChangedPaths.put(new DenotatorPath(this.objects.SOUND_SCORE_FORM, new int[]{0}), new DenotatorPath(this.objects.SOUND_SCORE_FORM, new int[]{2}));
		assertEquals(expectedChangedPaths, pathResults.getChangedPaths());
		//should now include three anchors on top level
		assertEquals(3, ((PowerDenotator)this.denotatorManager.getComposition()).getFactorCount());
	}

	@Test
	void testAlteration() throws LatrunculusCheckedException {
		this.denotatorManager.setOrAddComposition(this.objects.flatSoundScore);
		Set<DenotatorPath> comp0 = this.objects.makeNotePaths(new int[][]{{0},{1}});
		Set<DenotatorPath> comp1 = this.objects.makeNotePaths(new int[][]{{2}});
		List<DenotatorPath> alterationCoordinates = new ArrayList<>();
		alterationCoordinates.add(new DenotatorPath(this.objects.SOUND_NODE_FORM, new int[]{0,0}));
		alterationCoordinates.add(new DenotatorPath(this.objects.SOUND_NODE_FORM, new int[]{0,1}));
		alterationCoordinates.add(new DenotatorPath(this.objects.SOUND_NODE_FORM, new int[]{0,4}));
		DenotatorPath degreeDimension = new DenotatorPath(this.objects.SOUND_NODE_FORM, new int[]{0,0});
		OperationPathResults pathResults = this.denotatorManager.addAlteration(comp0, comp1, alterationCoordinates, .75, .25, degreeDimension);
		
		LimitDenotator expectedNode = this.objects.generator.createNodeDenotator(new double[]{1.25,62.25,116,1,0.25,0});
		this.objects.assertEqualNonPowerDenotators(expectedNode, this.denotatorManager.getComposition().get(new int[]{0}));
		expectedNode = this.objects.generator.createNodeDenotator(new double[]{1.5,60,120,1,0.75,0});
		this.objects.assertEqualNonPowerDenotators(expectedNode, this.denotatorManager.getComposition().get(new int[]{1}));
		expectedNode = this.objects.generator.createNodeDenotator(new double[]{2,60,121,1,1,0});
		this.objects.assertEqualNonPowerDenotators(expectedNode, this.denotatorManager.getComposition().get(new int[]{2}));
		
		Map<DenotatorPath,DenotatorPath> expectedChangedPaths = new TreeMap<>();
		expectedChangedPaths.put(new DenotatorPath(this.objects.SOUND_SCORE_FORM, new int[]{0}), new DenotatorPath(this.objects.SOUND_SCORE_FORM, new int[]{1}));
		expectedChangedPaths.put(new DenotatorPath(this.objects.SOUND_SCORE_FORM, new int[]{1}), new DenotatorPath(this.objects.SOUND_SCORE_FORM, new int[]{0}));
		assertEquals(expectedChangedPaths, pathResults.getChangedPaths());
	}

	@Test
	void testTotalAlteration() throws LatrunculusCheckedException {
		this.denotatorManager.setOrAddComposition(this.objects.flatSoundScore);
		Set<DenotatorPath> comp0 = this.objects.makeNotePaths(new int[][]{{0},{1}});
		Set<DenotatorPath> comp1 = this.objects.makeNotePaths(new int[][]{{2}});
		List<DenotatorPath> alterationCoordinates = new ArrayList<>();
		alterationCoordinates.add(new DenotatorPath(this.objects.SOUND_NODE_FORM, new int[]{0,0}));
		alterationCoordinates.add(new DenotatorPath(this.objects.SOUND_NODE_FORM, new int[]{0,1}));
		alterationCoordinates.add(new DenotatorPath(this.objects.SOUND_NODE_FORM, new int[]{0,2}));
		alterationCoordinates.add(new DenotatorPath(this.objects.SOUND_NODE_FORM, new int[]{0,4}));
		DenotatorPath degreeDimension = new DenotatorPath(this.objects.SOUND_NODE_FORM, new int[]{0,0});
		OperationPathResults pathResults = this.denotatorManager.addAlteration(comp0, comp1, alterationCoordinates, 1, 1, degreeDimension);
		
		assertEquals(1, ((PowerDenotator)this.denotatorManager.getComposition()).getFactorCount());
		LimitDenotator expectedNode = this.objects.generator.createNodeDenotator(new double[]{2,60,121,1,1,0});
		this.objects.assertEqualNonPowerDenotators(expectedNode, this.denotatorManager.getComposition().get(new int[]{0}));
		
		Set<DenotatorPath> expectedRemovedPaths = new TreeSet<>();
		expectedRemovedPaths.add(new DenotatorPath(this.objects.SOUND_SCORE_FORM, new int[]{0}));
		expectedRemovedPaths.add(new DenotatorPath(this.objects.SOUND_SCORE_FORM, new int[]{1}));
		assertEquals(expectedRemovedPaths, pathResults.getRemovedPaths());
		
		Map<DenotatorPath,DenotatorPath> expectedChangedPaths = new TreeMap<>();
		expectedChangedPaths.put(new DenotatorPath(this.objects.SOUND_SCORE_FORM, new int[]{2}), new DenotatorPath(this.objects.SOUND_SCORE_FORM, new int[]{0}));
		assertEquals(expectedChangedPaths, pathResults.getChangedPaths());
	}

	@Test
	@Disabled("old bug in domain ConstantMorphism made this work well...")
	void testWallpaper() {
		this.denotatorManager.setOrAddComposition(this.objects.flatSoundScore);
		Set<DenotatorPath> notePaths = this.objects.makeNotePaths(new int[][]{{0},{1}});
		this.denotatorManager.addWallpaperDimension(notePaths, 0, 1);
		BigBangTransformation translation = this.objects.makeTranslation(1,1, this.nodePaths);
		OperationPathResults pathResults = this.denotatorManager.addTransformation(notePaths, null, translation);
		Set<DenotatorPath> expectedNewPaths = new TreeSet<>();
		expectedNewPaths.add(new DenotatorPath(this.objects.SOUND_SCORE_FORM, new int[]{1}));
		expectedNewPaths.add(new DenotatorPath(this.objects.SOUND_SCORE_FORM, new int[]{4}));
		assertEquals(expectedNewPaths, pathResults.getNewPaths());
		Map<DenotatorPath,DenotatorPath> expectedChangedPaths = new TreeMap<>();
		expectedChangedPaths.put(new DenotatorPath(this.objects.SOUND_SCORE_FORM, new int[]{1}), new DenotatorPath(this.objects.SOUND_SCORE_FORM, new int[]{2}));
		expectedChangedPaths.put(new DenotatorPath(this.objects.SOUND_SCORE_FORM, new int[]{2}), new DenotatorPath(this.objects.SOUND_SCORE_FORM, new int[]{3}));
		assertEquals(expectedChangedPaths, pathResults.getChangedPaths());
		
		//add second transformation to dimension
		translation = this.objects.makeTranslation(-2,-2, this.nodePaths);
		pathResults = this.denotatorManager.addTransformation(notePaths, null, translation);
		Set<DenotatorPath> expectedEmptyPaths = new TreeSet<>();
		assertEquals(expectedEmptyPaths, pathResults.getNewPaths());
		expectedChangedPaths = new TreeMap<>();
		expectedChangedPaths.put(new DenotatorPath(this.objects.SOUND_SCORE_FORM, new int[]{0}), new DenotatorPath(this.objects.SOUND_SCORE_FORM, new int[]{1}));
		expectedChangedPaths.put(new DenotatorPath(this.objects.SOUND_SCORE_FORM, new int[]{1}), new DenotatorPath(this.objects.SOUND_SCORE_FORM, new int[]{0}));
		expectedChangedPaths.put(new DenotatorPath(this.objects.SOUND_SCORE_FORM, new int[]{2}), new DenotatorPath(this.objects.SOUND_SCORE_FORM, new int[]{3}));
		expectedChangedPaths.put(new DenotatorPath(this.objects.SOUND_SCORE_FORM, new int[]{3}), new DenotatorPath(this.objects.SOUND_SCORE_FORM, new int[]{4}));
		expectedChangedPaths.put(new DenotatorPath(this.objects.SOUND_SCORE_FORM, new int[]{4}), new DenotatorPath(this.objects.SOUND_SCORE_FORM, new int[]{2}));
		assertEquals(expectedChangedPaths, pathResults.getChangedPaths());
		
		//add second dimension with different range
		//{0,60,120,1,0,0},{1,63,116,1,0,0},{2,60,121,1,1,0}}
		//-1 59 0 60 0 62 1 63 generated by first dimension .. 2 60 stays
		//1 61 2 62 2 64 3 65 transformed by second dimension .. 2 60 stays
		//3 63 4 66 added by second iteration of second dimension
		//4 64 5 67 added by third iteration of second dimension SINCE 2 62 3 65 ALREADY EXISTED NOT FURTHER TRANSFORMED!!! 
		//NEW PATHS: 4 6 7 8
		this.denotatorManager.addWallpaperDimension(notePaths, 2, 4);
		translation = this.objects.makeTranslation(1,1, this.nodePaths);
		pathResults = this.denotatorManager.addTransformation(notePaths, null, translation);
		expectedNewPaths = new TreeSet<>();
		expectedNewPaths.add(new DenotatorPath(this.objects.SOUND_SCORE_FORM, new int[]{4}));
		for (int i = 6; i <= 8; i++) {
			expectedNewPaths.add(new DenotatorPath(this.objects.SOUND_SCORE_FORM, new int[]{i}));
		}
		assertEquals(expectedNewPaths, pathResults.getNewPaths());
		expectedChangedPaths = new TreeMap<>();
		expectedChangedPaths.put(new DenotatorPath(this.objects.SOUND_SCORE_FORM, new int[]{4}), new DenotatorPath(this.objects.SOUND_SCORE_FORM, new int[]{5}));
		expectedChangedPaths.put(new DenotatorPath(this.objects.SOUND_SCORE_FORM, new int[]{6}), new DenotatorPath(this.objects.SOUND_SCORE_FORM, new int[]{7}));
		assertEquals(expectedChangedPaths, pathResults.getChangedPaths());
		
		//TODO end wallpaper
	}
	
	private List<Map<DenotatorPath,Double>> generateNodeValueMapList(double[] noteValues) {
		Map<DenotatorPath,Double> valueMap = new TreeMap<>();
		for (int i = 0; i < noteValues.length; i++) {
			valueMap.put(new DenotatorPath(this.objects.SOUND_NODE_FORM, new int[]{0,i}), noteValues[i]);
		}
		return Collections.singletonList(valueMap);
	}
	
	private void assertEqualNodes(Denotator note1, Denotator note2) throws LatrunculusCheckedException {
		for (int i = 0; i < 6; i++) {
			assertEquals(note1.get(new int[]{0,i}).getCoordinate(), note2.get(new int[]{0,i}).getCoordinate());
		}
	}

}
