package org.rubato.rubettes.bigbang;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.vetronauta.latrunculus.core.exception.LatrunculusCheckedException;
import org.rubato.rubettes.bigbang.model.operations.ScalingTransformation;
import org.rubato.rubettes.bigbang.model.operations.TranslationTransformation;
import org.vetronauta.latrunculus.core.math.yoneda.denotator.Denotator;
import org.vetronauta.latrunculus.core.math.yoneda.form.Form;
import org.vetronauta.latrunculus.core.math.yoneda.denotator.PowerDenotator;
import org.vetronauta.latrunculus.core.math.yoneda.denotator.SimpleDenotator;
import org.rubato.rubettes.bigbang.model.BigBangModel;
import org.rubato.rubettes.bigbang.model.BigBangObject;
import org.rubato.rubettes.bigbang.model.denotators.TransformationPaths;
import org.rubato.rubettes.bigbang.model.denotators.TransformationProperties;
import org.rubato.rubettes.bigbang.model.graph.CompositionState;
import org.rubato.rubettes.bigbang.model.operations.AbstractOperation;
import org.rubato.rubettes.bigbang.model.operations.AddObjectsOperation;
import org.rubato.rubettes.util.DenotatorPath;

import edu.uci.ics.jung.graph.util.Pair;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BigBangOperationGraphTest {
	
	private BigBangModel model;
	private TestObjects objects;
	private TransformationPaths nodePaths;
	
	@BeforeEach
	void setUp() {
		this.objects = new TestObjects();
		this.model = new BigBangModel();
		this.nodePaths = this.objects.createStandardTransformationPaths(
				this.objects.SOUND_NODE_FORM, new int[][]{{0,0},{0,1}});
	}
	
	@Test
	void testModifyLeadingToTooManyPaths() {
		this.model.setOrAddComposition(this.objects.generator.createEmptyScore());
		int[][] paths = new int[][]{{0,0},{0,1}};
		double[][] values = new double[][]{{0,60},{2,65},{3,66},{4,67}};
		this.model.addObjects(this.createNodePathAndValuesMapList(this.objects.SOUND_SCORE_FORM, paths, values),
				this.createPathsList(new DenotatorPath(this.objects.SOUND_SCORE_FORM, new int[]{}), 4), false);
		assertTrue(this.model.getTransformationGraph().getLastAddedOperation() instanceof AddObjectsOperation);
		assertEquals(4, ((PowerDenotator)this.model.getComposition()).getFactorCount());
		
		Set<BigBangObject> objects = this.getBBObjectsFromModel(0,4);
		TransformationProperties properties = new TransformationProperties(objects, null, Arrays.asList(this.nodePaths), false, true);
		properties.setCenter(new double[]{0,0});
		properties.setEndPoint(new double[]{0,1});
		this.model.rotateObjects(properties, new double[]{1,0}, Math.PI/2);
		assertEquals(4, ((PowerDenotator)this.model.getComposition()).getFactorCount());
		this.model.modifyOperation(1, 0.5);
		assertEquals(2, ((PowerDenotator)this.model.getComposition()).getFactorCount());
	}

	@Test
	void testModifyWithSatellitesAddedDirectly() {
		this.model.setOrAddComposition(this.objects.generator.createEmptyScore());
		int[][] paths = new int[][]{{0,0},{0,1}};
		double[][] values = new double[][]{{0,60},{2,65},{3,66}};
		ArrayList<DenotatorPath> pathList = this.createPathsList(new DenotatorPath(this.objects.SOUND_SCORE_FORM, new int[]{0,1}), 3);
		pathList.add(0, new DenotatorPath(this.objects.SOUND_SCORE_FORM, new int[]{}));
		this.model.addObjects(this.createNodePathAndValuesMapList(this.objects.SOUND_SCORE_FORM, paths, values),
				pathList, false);
		assertTrue(this.model.getTransformationGraph().getLastAddedOperation() instanceof AddObjectsOperation);
		//only one first-level note
		assertEquals(1, ((PowerDenotator)this.model.getComposition()).getFactorCount());
		
		this.model.modifyOperation(0, 0.5);
		//anchor tone not there anymore so satellites should not be built. all we get are two of the four initial notes
		assertEquals(1, ((PowerDenotator)this.model.getComposition()).getFactorCount());
		
		//transform satellites
		Set<BigBangObject> satelliteObjects = this.getBBObjectsFromModel(1, 3);
		TransformationProperties properties = new TransformationProperties(satelliteObjects, null, Arrays.asList(this.nodePaths), false, true);
		properties.setCenter(new double[]{0,0});
		properties.setEndPoint(new double[]{0,1});
		this.model.scaleObjects(properties, new double[]{2,2});
		this.model.modifyOperation(1, 0.5);
		assertEquals(1, ((PowerDenotator)this.model.getComposition()).getFactorCount());
		
		//add one more
		paths = new int[][]{{0,0},{0,1}};
		values = new double[][]{{4,67}};
		this.model.addObjects(this.createNodePathAndValuesMapList(this.objects.SOUND_SCORE_FORM, paths, values),
				this.createPathsList(new DenotatorPath(this.objects.SOUND_SCORE_FORM, new int[]{0,1}), 1), false);
		assertTrue(this.model.getTransformationGraph().getLastAddedOperation() instanceof AddObjectsOperation);
		//only one first-level note
		assertEquals(1, ((PowerDenotator)this.model.getComposition()).getFactorCount());
		
		//test animation
		this.model.setOperationDurations(.1);
		//this.model.toggleGraphAnimation();
		assertEquals(1, ((PowerDenotator)this.model.getComposition()).getFactorCount());
	}

	@Test
	void testModifyWithSatellites() {
		this.model.setOrAddComposition(this.objects.generator.createEmptyScore());
		int[][] paths = new int[][]{{0,0},{0,1}};
		double[][] values = new double[][]{{0,60},{2,65},{3,66},{4,67}};
		this.model.addObjects(this.createNodePathAndValuesMapList(this.objects.SOUND_SCORE_FORM, paths, values),
				this.createPathsList(new DenotatorPath(this.objects.SOUND_SCORE_FORM, new int[]{}), 4), false);
		assertTrue(this.model.getTransformationGraph().getLastAddedOperation() instanceof AddObjectsOperation);
		assertEquals(4, ((PowerDenotator)this.model.getComposition()).getFactorCount());
		
		Set<BigBangObject> satelliteObjects = this.getBBObjectsFromModel(0, 3);
		BigBangObject anchorObject = this.getBBObjectsFromModel(3, 4).iterator().next();
		this.model.buildSatellites(new TreeSet<>(satelliteObjects), anchorObject, 0);
		assertEquals(1, ((PowerDenotator)this.model.getComposition()).getFactorCount());
		
		this.model.modifyOperation(2, 0.5);
		//less satellites built so one anchor and a non-added satellite in top level
		assertEquals(2, ((PowerDenotator)this.model.getComposition()).getFactorCount());
		
		this.model.modifyOperation(1, 0.5);
		//anchor tone not there anymore so satellites should not be built. all we get are two of the four initial notes
		assertEquals(2, ((PowerDenotator)this.model.getComposition()).getFactorCount());
		
		//transform satellites
		//satelliteObjects = this.getBBObjectsFromModel(0, 1);
		TransformationProperties properties = new TransformationProperties(satelliteObjects, null, Arrays.asList(this.nodePaths), false, false);
		properties.setCenter(new double[]{0,0});
		properties.setEndPoint(new double[]{0,1});
		this.model.scaleObjects(properties , new double[]{2,2});
		
		//back to unmodified. satellites should now be transformed
		this.model.modifyOperation(1, 1.0);
		this.model.modifyOperation(2, 1.0);
		assertEquals(1, ((PowerDenotator)this.model.getComposition()).getFactorCount());
		
		//test animation
		this.model.setOperationDurations(.1);
		//this.model.toggleGraphAnimation();
		/*//satellites should still be satellites
		for (BigBangObject currentObject : this.model.getObjects().getObjects()) {
			System.out.println(currentObject.getParent());
		}
		//assertEquals(1, ((PowerDenotator)this.model.getComposition()).getFactorCount());
		assertEquals(4, this.model.getObjects().getObjects().size());
		assertEquals(3, this.model.getObjects().getObjects().iterator().next().getChildren().size());*/
	}

	@Test
	void testFatefulModificationOfPast() {
		this.model.setOrAddComposition(this.objects.generator.createEmptyScore());
		int[][] paths = new int[][]{{0,0},{0,1}};
		double[][] values = new double[][]{{0,60},{1,60}};
		this.model.addObjects(this.createNodePathAndValuesMapList(this.objects.SOUND_SCORE_FORM, paths, values),
				this.createPathsList(new DenotatorPath(this.objects.SOUND_SCORE_FORM, new int[]{}), 2), false);
		assertTrue(this.model.getTransformationGraph().getLastAddedOperation() instanceof AddObjectsOperation);
		assertEquals(2, ((PowerDenotator)this.model.getComposition()).getFactorCount());
		
		//translation so note0 ends up later than note1
		Set<BigBangObject> object = this.getBBObjectsFromModel(0, 1);
		//SelectedObjectPaths selectedPaths = this.createSelectedObjectsPaths(this.objects.SOUND_SCORE_FORM, new int[]{0});
		TransformationProperties properties = new TransformationProperties(object, null, Arrays.asList(this.nodePaths), false, true);
		properties.setCenter(new double[]{0,0});
		properties.setEndPoint(new double[]{2,0});
		this.model.translateObjects(properties);
		Denotator expectedResult = this.objects.generator.createFlatSoundScore(new double[][]{{1,60,0,0,0,0},{2,60,0,0,0,0}});
		assertEquals(expectedResult, this.model.getComposition());
		
		//translate note0 at its later position (the same set of objects can be used since BBObject kept track!) 
		this.model.translateObjects(properties);
		expectedResult = this.objects.generator.createFlatSoundScore(new double[][]{{1,60,0,0,0,0},{4,60,0,0,0,0}});
		assertEquals(expectedResult, this.model.getComposition());
		
		//modify first translation so note0 is back at earlier position
		this.model.modifyOperation(2, 0.0);
		//note0 should still be affected by second translation since BigBangObjects should have recorded its identity
		//so (1,60),(2,60) and not (0,60),(3,60)
		expectedResult = this.objects.generator.createFlatSoundScore(new double[][]{{1,60,0,0,0,0},{2,60,0,0,0,0}});
		assertEquals(expectedResult, this.model.getComposition());
	}

	@Test
	void testFatefulModificationOfPastWithSatellites() throws LatrunculusCheckedException {
		this.model.setOrAddComposition(this.objects.generator.createEmptyScore());
		int[][] paths = new int[][]{{0,0},{0,1}};
		double[][] values = new double[][]{{0,60},{1,60},{2,65}};
		this.model.addObjects(this.createNodePathAndValuesMapList(this.objects.SOUND_SCORE_FORM, paths, values),
				this.createPathsList(new DenotatorPath(this.objects.SOUND_SCORE_FORM, new int[]{}), 3), false);
		assertTrue(this.model.getTransformationGraph().getLastAddedOperation() instanceof AddObjectsOperation);
		assertEquals(3, ((PowerDenotator)this.model.getComposition()).getFactorCount());
		
		//get objects and build satellites
		BigBangObject anchor = this.getBBObjectsFromModel(0, 1).iterator().next();
		Set<BigBangObject> satellites = this.getBBObjectsFromModel(1, 3);
		this.model.buildSatellites(new TreeSet<>(satellites), anchor, 0);
		
		//translate both satellites by 1 pitch
		TransformationProperties properties = new TransformationProperties(satellites, null, Arrays.asList(this.nodePaths), false, true);
		properties.setCenter(new double[]{0,0});
		properties.setEndPoint(new double[]{0,1});
		this.model.translateObjects(properties);
		Denotator expectedNode = this.objects.createMultilevelNode(new double[][]{{1,1,0,0,0,0}});
		Denotator actualNode = this.model.getComposition().get(new int[]{0,1,0});
		assertEquals(expectedNode, actualNode);
		expectedNode = this.objects.createMultilevelNode(new double[][]{{2,6,0,0,0,0}});
		assertEquals(expectedNode, this.model.getComposition().get(new int[]{0,1,1}));
		
		//modify so that second satellite not built
		this.model.modifyOperation(2, 0.5);
		//still both nodes should be transformed
		expectedNode = this.objects.createMultilevelNode(new double[][]{{1,1,0,0,0,0}});
		assertEquals(expectedNode, this.model.getComposition().get(new int[]{0,1,0}));
		expectedNode = this.objects.createMultilevelNode(new double[][]{{2,66,0,0,0,0}});
		assertEquals(expectedNode, this.model.getComposition().get(new int[]{1}));
	}

	@Test
	void testAddAlternativeEdge() {
		//add one note and perform two translations
		this.model.setOrAddComposition(this.objects.generator.createEmptyScore());
		int[][] paths = new int[][]{{0,0},{0,1}};
		double[][] values = new double[][]{{0,60}};
		this.model.addObjects(this.createNodePathAndValuesMapList(this.objects.SOUND_SCORE_FORM, paths, values),
				this.createPathsList(new DenotatorPath(this.objects.SOUND_SCORE_FORM, new int[]{}), 1), false);
		
		Set<BigBangObject> object = this.getBBObjectsFromModel(0, 1);
		TransformationProperties properties = new TransformationProperties(object, null, Arrays.asList(this.nodePaths), false, true);
		properties.setCenter(new double[]{0,0});
		properties.setEndPoint(new double[]{0,1});
		this.model.translateObjects(properties);
		this.model.translateObjects(properties);
		assertEquals(5, this.model.getTransformationGraph().getVertexCount());
		assertEquals(4, this.model.getTransformationGraph().getEdgeCount());
		
		//add a translation starting at state 3 and check graph structure
		this.model.selectCompositionState(this.model.getTransformationGraph().getCompositionStateAt(3));
		this.model.translateObjects(properties);
		assertEquals(6, this.model.getTransformationGraph().getVertexCount());
		assertEquals(5, this.model.getTransformationGraph().getEdgeCount());
		AbstractOperation lastEdit = this.model.getTransformationGraph().getLastAddedOperation(); 
		assertEquals(new Pair<>(this.getCompStateAt(3), this.getCompStateAt(5)), this.model.getTransformationGraph().getEndpoints(lastEdit));
	}

	@Test
	void testAddParallelEdge() {
		//add one note and perform two translations
		this.model.setOrAddComposition(this.objects.generator.createEmptyScore());
		int[][] paths = new int[][]{{0,0},{0,1}};
		double[][] values = new double[][]{{0,60}};
		this.model.addObjects(this.createNodePathAndValuesMapList(this.objects.SOUND_SCORE_FORM, paths, values),
				this.createPathsList(new DenotatorPath(this.objects.SOUND_SCORE_FORM, new int[]{}), 1), false);
		
		Set<BigBangObject> object = this.getBBObjectsFromModel(0, 1);
		TransformationProperties properties = new TransformationProperties(object, null, Arrays.asList(this.nodePaths), false, true);
		properties.setCenter(new double[]{0,0});
		properties.setEndPoint(new double[]{0,1});
		this.model.translateObjects(properties);
		this.model.translateObjects(properties);
		assertEquals(5, this.model.getTransformationGraph().getVertexCount());
		assertEquals(4, this.model.getTransformationGraph().getEdgeCount());
		
		//add a parallel translation starting at state 2 and check graph structure
		this.model.selectOperation(this.model.getTransformationGraph().getLastAddedOperation());
		this.model.translateObjects(properties);
		assertEquals(5, this.model.getTransformationGraph().getVertexCount());
		assertEquals(5, this.model.getTransformationGraph().getEdgeCount());
		AbstractOperation lastEdit = this.model.getTransformationGraph().getLastAddedOperation(); 
		//System.out.println(this.model.getTransformationGraph());
		assertEquals(new Pair<>(this.getCompStateAt(3), this.getCompStateAt(4)), this.model.getTransformationGraph().getEndpoints(lastEdit));
	}

	@Test
	@Disabled("there are problems of ClassCastException with TreeSet<AbstractOperation> as AbstractOperation is not Comparable\n")
	void testInsertEdge() {
		//add one note and perform two translations
		this.model.setOrAddComposition(this.objects.generator.createEmptyScore());
		int[][] paths = new int[][]{{0,0},{0,1}};
		double[][] values = new double[][]{{0,60}};
		this.model.addObjects(this.createNodePathAndValuesMapList(this.objects.SOUND_SCORE_FORM, paths, values),
				this.createPathsList(new DenotatorPath(this.objects.SOUND_SCORE_FORM, new int[]{}), 1), false);
		
		Set<BigBangObject> object = this.getBBObjectsFromModel(0, 1);
		TransformationProperties properties = new TransformationProperties(object, null, Arrays.asList(this.nodePaths), false, true);
		properties.setCenter(new double[]{0,0});
		properties.setEndPoint(new double[]{0,1});
		this.model.translateObjects(properties);
		this.model.translateObjects(properties);
		assertEquals(5, this.model.getTransformationGraph().getVertexCount());
		assertEquals(4, this.model.getTransformationGraph().getEdgeCount());
		
		//insert a translation before the second one and check graph structure
		this.model.setInsertionState(1);
		this.model.translateObjects(properties);
		assertEquals(6, this.model.getTransformationGraph().getVertexCount());
		assertEquals(5, this.model.getTransformationGraph().getEdgeCount());
	}

	@Test
	@Disabled("there are problems of ClassCastException with TreeSet<AbstractOperation> as AbstractOperation is not Comparable\n")
	void testSplitEdge() {
		//add one note and perform a translation and a scaling
		this.model.setOrAddComposition(this.objects.generator.createEmptyScore());
		int[][] paths = new int[][]{{0,0},{0,1}};
		double[][] values = new double[][]{{0,60}};
		this.model.addObjects(this.createNodePathAndValuesMapList(this.objects.SOUND_SCORE_FORM, paths, values),
				this.createPathsList(new DenotatorPath(this.objects.SOUND_SCORE_FORM, new int[]{}), 1), false);
		
		Set<BigBangObject> object = this.getBBObjectsFromModel(0, 1);
		TransformationProperties properties = new TransformationProperties(object, null, Arrays.asList(this.nodePaths), false, true);
		properties.setCenter(new double[]{0,0});
		properties.setEndPoint(new double[]{0,1});
		this.model.translateObjects(properties);
		this.model.scaleObjects(properties, new double[]{0,2});
		assertEquals(5, this.model.getTransformationGraph().getVertexCount());
		assertEquals(4, this.model.getTransformationGraph().getEdgeCount());
		
		//move to beginning of the translation, select it, and split
		this.model.setGraphAnimationPosition(.6); //at .4 of translation (4*.1)
		this.model.selectOperation(this.model.getTransformationGraph().getOutEdges(this.getCompStateAt(2)).iterator().next());
		this.model.splitOperation();
		
		//check number of nodes and edges and the shifts of the resulting translations
		assertEquals(6, this.model.getTransformationGraph().getVertexCount());
		assertEquals(5, this.model.getTransformationGraph().getEdgeCount());
		TranslationTransformation firstPart = (TranslationTransformation)this.model.getTransformationGraph().getOutEdges(this.getCompStateAt(2)).iterator().next();
		assertEquals(0.0, firstPart.getStartingPoint()[1]);
		//cope with rounding error
		assertEquals(0.4, ((double)Math.round(firstPart.getEndingPoint()[1]*1000))/1000);
		TranslationTransformation secondPart = (TranslationTransformation)this.model.getTransformationGraph().getOutEdges(this.getCompStateAt(3)).iterator().next();
		assertEquals(0.4, ((double)Math.round(1000*secondPart.getStartingPoint()[1]))/1000);
		assertEquals(1.0, ((double)Math.round(1000*secondPart.getEndingPoint()[1]))/1000);
		
		//move to beginning of the scaling, select it, and split
		this.model.setGraphAnimationPosition(.9); //at .5 of scaling (5*.1)
		this.model.selectOperation(this.model.getTransformationGraph().getOutEdges(this.getCompStateAt(4)).iterator().next());
		this.model.splitOperation();
		
		//check number of nodes and edges and the shifts of the resulting scalings
		assertEquals(7, this.model.getTransformationGraph().getVertexCount());
		assertEquals(6, this.model.getTransformationGraph().getEdgeCount());
		ScalingTransformation firstScalingPart = (ScalingTransformation)this.model.getTransformationGraph().getOutEdges(this.getCompStateAt(4)).iterator().next();
		assertEquals(0.0, firstScalingPart.getCenter()[1]);
		//cope with rounding error
		assertEquals(1.5, ((double)Math.round(firstScalingPart.getScaleFactors()[1]*1000))/1000);
		ScalingTransformation secondScalingPart = (ScalingTransformation)this.model.getTransformationGraph().getOutEdges(this.getCompStateAt(5)).iterator().next();
		assertEquals(0.0, secondScalingPart.getCenter()[1]);
		assertEquals(1.5, ((double)Math.round(1000*secondScalingPart.getScaleFactors()[1]))/1000);
	}

	@Test
	void testRemoveEdge() {
		//add one note and perform three translations
		this.model.setOrAddComposition(this.objects.generator.createEmptyScore());
		int[][] paths = new int[][]{{0,0},{0,1}};
		double[][] values = new double[][]{{0,60}};
		this.model.addObjects(this.createNodePathAndValuesMapList(this.objects.SOUND_SCORE_FORM, paths, values),
				this.createPathsList(new DenotatorPath(this.objects.SOUND_SCORE_FORM, new int[]{}), 1), false);
		
		Set<BigBangObject> object = this.getBBObjectsFromModel(0, 1);
		TransformationProperties properties = new TransformationProperties(object, null, Arrays.asList(this.nodePaths), false, true);
		properties.setCenter(new double[]{0,0});
		properties.setEndPoint(new double[]{0,1});
		this.model.translateObjects(properties);
		this.model.translateObjects(properties);
		this.model.translateObjects(properties);
		assertEquals(6, this.model.getTransformationGraph().getVertexCount());
		assertEquals(5, this.model.getTransformationGraph().getEdgeCount());
		
		//remove second translation and check graph structure
		this.model.removeOperation(this.model.getTransformationGraph().findEdge(this.getCompStateAt(3), this.getCompStateAt(4)));
		assertEquals(5, this.model.getTransformationGraph().getVertexCount());
		assertEquals(4, this.model.getTransformationGraph().getEdgeCount());
	}

	@Test
	@Disabled("old bug in domain ConstantMorphism made this work well...")
	void testModifyCopyAndTransform() {
		this.model.setOrAddComposition(this.objects.flatSoundScore);
		TreeSet<BigBangObject> objects = this.getBBObjectsFromModel(0, 3);
		
		TransformationProperties properties = new TransformationProperties(objects, null, Arrays.asList(this.nodePaths), true, true);
		properties.setCenter(new double[]{0,0});
		properties.setEndPoint(new double[]{2,2});
		this.model.translateObjects(properties);
		Set<BigBangObject> objectsAfterTranslation = this.model.getObjects().getObjectsAt(null);
		assertEquals(6, this.model.getObjects().getAllObjects().size());
		assertEquals(6, objectsAfterTranslation.size());
		
		this.model.modifyOperation(1, 0.0);
		assertEquals(6, this.model.getObjects().getAllObjects().size());
		assertEquals(3, this.model.getObjects().getObjectsAt(null).size());
		
		this.model.modifyOperation(1, 1.0);
		assertEquals(6, this.model.getObjects().getAllObjects().size());
		assertEquals(6, this.model.getObjects().getObjectsAt(null).size());
		assertEquals(objectsAfterTranslation, this.model.getObjects().getObjectsAt(null));
	}

	@Test
	@Disabled("old bug in domain ConstantMorphism made this work well...")
	void testProjection() {
		//scale so that notes projected and made equal
		this.model.setOrAddComposition(this.objects.generator.createFlatSoundScore(
				new double[][]{{0,60,120,1,0,0},{1,60,120,1,0,0},{2,61,120,1,0,0}}));
		TreeSet<BigBangObject> objects = this.getBBObjectsFromModel(0, 3);
		TransformationProperties properties = new TransformationProperties(objects, null, Arrays.asList(this.nodePaths), false, true);
		properties.setCenter(new double[]{0,0});
		properties.setEndPoint(new double[]{-1,1});
		Set<BigBangObject> objectsBeforeProjection = this.model.getObjects().getObjectsAt(null);
		this.model.scaleObjects(properties, new double[]{0,2});
		assertEquals(3, this.model.getObjects().getAllObjects().size());
		assertEquals(2, this.model.getObjects().getObjectsAt(null).size());
		
		//undo projection and see if objects back
		this.model.modifyOperation(1, 0.5);
		assertEquals(3, this.model.getObjects().getAllObjects().size());
		assertEquals(3, this.model.getObjects().getObjectsAt(null).size());
		assertEquals(objectsBeforeProjection, this.model.getObjects().getObjectsAt(null));
		
		//change to projection to one point
		properties = new TransformationProperties(objects, null, Arrays.asList(this.nodePaths), false, true);
		properties.setCenter(new double[]{0,0});
		properties.setEndPoint(new double[]{-1,-1});
		this.model.scaleObjects(properties, new double[]{0,0});
		assertEquals(3, this.model.getObjects().getAllObjects().size());
		assertEquals(1, this.model.getObjects().getObjectsAt(null).size());
	}

	@Test
	void testTransformAnchors() {
		this.model.setOrAddComposition(this.objects.generator.createEmptyScore());
		int[][] paths = new int[][]{{0,0},{0,1}};
		double[][] values = new double[][]{{0,60},{2,65},{3,66},{4,67}};
		this.model.addObjects(this.createNodePathAndValuesMapList(this.objects.SOUND_SCORE_FORM, paths, values),
				this.createPathsList(new DenotatorPath(this.objects.SOUND_SCORE_FORM, new int[]{}), 4), false);
		assertTrue(this.model.getTransformationGraph().getLastAddedOperation() instanceof AddObjectsOperation);
		assertEquals(4, ((PowerDenotator)this.model.getComposition()).getFactorCount());
		
		List<BigBangObject> objects = new ArrayList<>(this.getBBObjectsFromModel(0, 4));
		this.model.buildSatellites(new TreeSet<>(objects.subList(3, 4)), objects.get(0), 0);
		this.model.buildSatellites(new TreeSet<>(objects.subList(2, 3)), objects.get(1), 0);
		assertEquals(2, ((PowerDenotator)this.model.getComposition()).getFactorCount());
		assertSame(objects.get(3).getParent(), objects.get(0));
		assertSame(objects.get(2).getParent(), objects.get(1));
		
		//make transformation that switches paths of anchor objects
		TransformationProperties properties = new TransformationProperties(new TreeSet<>(objects.subList(0, 1)), null, Arrays.asList(this.nodePaths), false, true);
		properties.setCenter(new double[]{0,0});
		properties.setEndPoint(new double[]{4,1});
		this.model.translateObjects(properties);
		AbstractOperation translation = this.model.getTransformationGraph().getLastAddedOperation();
		assertEquals(5, this.model.getTransformationGraph().getEdgeCount());
		assertEquals(2, ((PowerDenotator)this.model.getComposition()).getFactorCount());
		//should still have the same parent
		assertSame(objects.get(3).getParent(), objects.get(0));
		assertSame(objects.get(2).getParent(), objects.get(1));
		//should not have the same paths anymore since parent paths changed
		assertNotEquals(objects.get(2).getTopDenotatorPathAt(translation), objects.get(2).getTopDenotatorPathAt(null));
		assertNotEquals(objects.get(3).getTopDenotatorPathAt(translation), objects.get(3).getTopDenotatorPathAt(null));
	}

	@Test
	void testCopyAndTransformAnchors() {
		this.model.setOrAddComposition(this.objects.generator.createEmptyScore());
		int[][] paths = new int[][]{{0,0},{0,1}};
		double[][] values = new double[][]{{0,60},{2,65},{3,66},{4,67}};
		this.model.addObjects(this.createNodePathAndValuesMapList(this.objects.SOUND_SCORE_FORM, paths, values),
				this.createPathsList(new DenotatorPath(this.objects.SOUND_SCORE_FORM, new int[]{}), 4), false);
		assertTrue(this.model.getTransformationGraph().getLastAddedOperation() instanceof AddObjectsOperation);
		assertEquals(4, ((PowerDenotator)this.model.getComposition()).getFactorCount());
		
		List<BigBangObject> objects = new ArrayList<>(this.getBBObjectsFromModel(0, 4));
		this.model.buildSatellites(new TreeSet<>(objects.subList(3, 4)), objects.get(0), 0);
		this.model.buildSatellites(new TreeSet<>(objects.subList(2, 3)), objects.get(1), 0);
		assertEquals(2, ((PowerDenotator)this.model.getComposition()).getFactorCount());
		assertSame(objects.get(3).getParent(), objects.get(0));
		assertSame(objects.get(2).getParent(), objects.get(1));
		
		//make transformation that copies and transforms first anchor
		TransformationProperties properties = new TransformationProperties(new TreeSet<>(objects.subList(0, 1)), null, Arrays.asList(this.nodePaths), true, true);
		properties.setCenter(new double[]{0,0});
		properties.setEndPoint(new double[]{1,1});
		this.model.translateObjects(properties);
		assertEquals(5, this.model.getTransformationGraph().getEdgeCount());
		//there should now be three anchors
		assertEquals(3, ((PowerDenotator)this.model.getComposition()).getFactorCount());
		//there should now be a total of six objects
		assertEquals(6, this.model.getObjects().getAllObjects().size());
		//second anchor should now be in third place
		assertEquals(new DenotatorPath(this.objects.SOUND_SCORE_FORM, new int[]{2}), objects.get(1).getTopDenotatorPathAt(null));
	}

	@Test
	@Disabled("old bug in domain ConstantMorphism made this work well...")
	void testModifyWallpaper() {
		//test modifying transformation so that motif displaced. other wallpaper tests in DenotatorManagerTests
		this.model.setOrAddComposition(this.objects.flatSoundScore);
		TreeSet<BigBangObject> objects = this.getBBObjectsFromModel(0, 2);
		this.model.addWallpaperDimension(objects, 0, 5);
		
		TransformationProperties properties = new TransformationProperties(objects, null, Arrays.asList(this.nodePaths), false, true);
		properties.setCenter(new double[]{0,0});
		properties.setEndPoint(new double[]{1,1});
		this.model.translateObjects(properties);
		assertEquals(3, this.model.getTransformationGraph().getEdgeCount());
		assertEquals(13, this.model.getObjects().getAllObjects().size());
		
		//modify translation
		properties = new TransformationProperties(objects, null, Arrays.asList(this.nodePaths), false, false);
		properties.setCenter(new double[]{0,0});
		properties.setEndPoint(new double[]{-1,-1});
		this.model.translateObjects(properties);
		assertEquals(3, this.model.getTransformationGraph().getEdgeCount());
		assertEquals(13, this.model.getObjects().getAllObjects().size());
	}

	@Test
	void testRepeatedAddAndTransformWithLimit() {
		//add an integer
		this.model.setOrAddComposition(this.objects.createInteger(3));
		assertEquals(1, this.model.getObjects().getAllObjects().size());
		assertEquals(3, ((SimpleDenotator)this.model.getComposition()).getInteger());
		BigBangObject addedObject = this.model.getObjects().getObjectsAt(null).iterator().next();
		
		//add with addEdit, should be replaced by new object
		int[][] paths = new int[][]{{}};
		double[][] values = new double[][]{{11}};
		this.model.addObjects(this.createNodePathAndValuesMapList(this.objects.INTEGER_FORM, paths, values),
				this.createPathsList(null, 1), false);
		assertEquals(1, this.model.getObjects().getAllObjects().size());
		assertEquals(11, ((SimpleDenotator)this.model.getComposition()).getInteger());
		//replaced object should be the same as the one added first!!
		assertSame(addedObject, this.model.getObjects().getObjectsAt(null).iterator().next());
		
		//add again, should be replaced by same object
		values = new double[][]{{12}};
		this.model.addObjects(this.createNodePathAndValuesMapList(this.objects.INTEGER_FORM, paths, values),
				this.createPathsList(null, 1), false);
		assertEquals(1, this.model.getObjects().getAllObjects().size());
		assertEquals(12, ((SimpleDenotator)this.model.getComposition()).getInteger());
		//replaced object should be the same as the one added first!!
		assertSame(addedObject, this.model.getObjects().getObjectsAt(null).iterator().next());
		
		//transform and see if replaced
		Set<BigBangObject> objects = this.getBBObjectsFromModel(0,1);
		List<TransformationPaths> transformationPaths = Arrays.asList(this.objects.createStandardTransformationPaths(this.objects.INTEGER_FORM, new int[][]{{0},null}));
		TransformationProperties properties = new TransformationProperties(objects, null, transformationPaths, false, true);
		properties.setCenter(new double[]{0,0});
		properties.setEndPoint(new double[]{1,0});
		this.model.scaleObjects(properties, new double[]{2,0});
		assertEquals(1, this.model.getObjects().getAllObjects().size());
		assertEquals(24, ((SimpleDenotator)this.model.getComposition()).getInteger());
		//replaced object should be the same as the one added first!!
		assertSame(addedObject, this.model.getObjects().getObjectsAt(null).iterator().next());
	}
	
	private ArrayList<Map<DenotatorPath,Double>> createNodePathAndValuesMapList(Form form, int[][] paths, double[][] values) {
		ArrayList<Map<DenotatorPath,Double>> list = new ArrayList<>();
		for (double[] value : values) {
			list.add(this.createNodePathAndValuesMap(form, paths, value));
		}
		return list;
	}
	
	private ArrayList<DenotatorPath> createPathsList(DenotatorPath path, int amount) {
		ArrayList<DenotatorPath> list = new ArrayList<>();
		for (int i = 0; i < amount; i++) {
			list.add(path);
		}
		return list;
	}
	
	private Map<DenotatorPath,Double> createNodePathAndValuesMap(Form form, int[][] paths, double[] values) {
		Map<DenotatorPath,Double> valuesMap = new TreeMap<>();
		for (int i = 0; i < paths.length; i++) {
			valuesMap.put(new DenotatorPath(form, paths[i]), values[i]);
		}
		//valuesMap.put(new DenotatorPath(this.objects.SOUND_NODE_FORM, new int[]{0,1}), pitch);
		return valuesMap;
	}
	
	private TreeSet<BigBangObject> getBBObjectsFromModel(int fromIndex, int toIndex) {
		List<BigBangObject> objectList = new ArrayList<>(this.model.getObjects().getAllObjects());
		return new TreeSet<>(objectList.subList(fromIndex, toIndex));
	}
	
	private CompositionState getCompStateAt(int index) {
		return this.model.getTransformationGraph().getCompositionStateAt(index);
	}

}
