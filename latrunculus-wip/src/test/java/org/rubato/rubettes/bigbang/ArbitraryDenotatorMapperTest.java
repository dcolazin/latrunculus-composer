package org.rubato.rubettes.bigbang;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.vetronauta.latrunculus.core.exception.LatrunculusCheckedException;
import org.rubato.rubettes.bigbang.model.denotators.TransformationPaths;
import org.rubato.rubettes.util.ArbitraryDenotatorMapper;
import org.rubato.rubettes.util.DenotatorPath;
import org.vetronauta.latrunculus.core.math.element.generic.Vector;
import org.vetronauta.latrunculus.core.math.element.impl.Real;
import org.vetronauta.latrunculus.core.math.matrix.RMatrix;
import org.vetronauta.latrunculus.core.math.module.impl.RRing;
import org.vetronauta.latrunculus.core.math.morphism.ModuleMorphism;
import org.vetronauta.latrunculus.core.math.morphism.affine.AffineFreeMorphism;
import org.vetronauta.latrunculus.core.math.yoneda.denotator.Denotator;
import org.vetronauta.latrunculus.core.math.yoneda.denotator.LimitDenotator;
import org.vetronauta.latrunculus.core.math.yoneda.denotator.PowerDenotator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class ArbitraryDenotatorMapperTest {
	
	private TestObjects objects;
	private ModuleMorphism translation;

	@BeforeEach
	void setUp() throws Exception {
		this.objects = new TestObjects();
		RMatrix identity = new RMatrix(new double[][]{{1,0},{0,1}});
		List<Real> list = new ArrayList<>();
		list.add(new Real(-1));
		list.add(new Real(-2));
		this.translation = AffineFreeMorphism.make(RRing.ring, identity, new Vector<>(RRing.ring, list));
	}

	@Test
	void testMappingOfNodeDenotators() throws LatrunculusCheckedException {
		//create paths to map onset x pitch -> onset x pitch
		TransformationPaths paths = this.objects.createStandardTransformationPaths(
				this.objects.SOUND_NODE_FORM, new int[][]{{0,0},{0,1}});
		//init mapper
		ArbitraryDenotatorMapper mapper = new ArbitraryDenotatorMapper(this.translation, paths);
		
		LimitDenotator node = (LimitDenotator)this.objects.multiLevelSoundScore.get(new int[]{0});
		LimitDenotator mappedNode = (LimitDenotator)mapper.getMappedDenotator(node);
		//System.out.println("HEY " + (node == mappedNode));
		//check if transformed properly and satellites still there and unchanged
		LimitDenotator expectedNode = this.objects.createMultilevelNode(new double[][]{{-1,58,120,1,0},{1,3,-4,0,0},{1,-3,5,0,1}});
		this.objects.assertEqualNonPowerDenotators(mappedNode, expectedNode);
	}

	@Test
	void testMappingOfColimitDenotators() throws LatrunculusCheckedException {
		//create paths to map int x real -> int x real
		TransformationPaths paths = this.objects.createStandardTransformationPaths(
				this.objects.INTEGER_OR_REAL_FORM, new int[][]{{0},{1}});
		//init mapper
		ArbitraryDenotatorMapper mapper = new ArbitraryDenotatorMapper(this.translation, paths);
		
		//test transformation of coordinates directly
		Denotator currentColimit = this.objects.integerOrReals.get(new int[]{0});
		Denotator mappedColimit = mapper.getMappedDenotator(currentColimit);
		Denotator expectedColimit = this.objects.createIntegerOrReal(true, 3);
		this.objects.assertEqualNonPowerDenotators(expectedColimit, mappedColimit);
		currentColimit = this.objects.integerOrReals.get(new int[]{3});
		mappedColimit = mapper.getMappedDenotator(currentColimit);
		expectedColimit = this.objects.createIntegerOrReal(false, 1.5);
		this.objects.assertEqualNonPowerDenotators(expectedColimit, mappedColimit);
		
		//test transformation of powerset
		Denotator mappedPowerset = mapper.getMappedPowerDenotator(this.objects.integerOrReals);
		expectedColimit = this.objects.createIntegerOrReal(true, 3);
		this.objects.assertEqualNonPowerDenotators(expectedColimit, mappedPowerset.get(new int[]{0}));
		expectedColimit = this.objects.createIntegerOrReal(true, 4);
		this.objects.assertEqualNonPowerDenotators(expectedColimit, mappedPowerset.get(new int[]{1}));
		expectedColimit = this.objects.createIntegerOrReal(false, 0.5);
		this.objects.assertEqualNonPowerDenotators(expectedColimit, mappedPowerset.get(new int[]{2}));
		expectedColimit = this.objects.createIntegerOrReal(false, 1.5);
		this.objects.assertEqualNonPowerDenotators(expectedColimit, mappedPowerset.get(new int[]{3}));
	}

	@Test
	void testMappingOfColimitDenotators2() throws LatrunculusCheckedException {
		TransformationPaths paths = new TransformationPaths();
		//add Duration paths for both types of configurations: note and rest
		DenotatorPath noteDurationPath = new DenotatorPath(this.objects.GENERAL_NOTE_FORM, new int[]{0,3});
		DenotatorPath restDurationPath = new DenotatorPath(this.objects.GENERAL_NOTE_FORM, new int[]{1,1});
		List<DenotatorPath> durationPaths = Arrays.asList(noteDurationPath, restDurationPath);
		
		paths.setDomainPaths(0, durationPaths);
		paths.setCodomainPaths(0, durationPaths);
		//add Pitch paths only for notes
		DenotatorPath notePitchPath = new DenotatorPath(this.objects.GENERAL_NOTE_FORM, new int[]{0,1});
		List<DenotatorPath> pitchPaths = Arrays.asList(notePitchPath);
		paths.setDomainPaths(1, pitchPaths);
		paths.setCodomainPaths(1, pitchPaths);
		//init mapper
		ArbitraryDenotatorMapper mapper = new ArbitraryDenotatorMapper(this.translation, paths);
		
		//test transformation of coordinates directly
		double[][] rests = new double[][]{{3,1,0},{4,3,0}};
		Denotator mappedGeneralScore = mapper.getMappedPowerDenotator(this.objects.createGeneralScore(this.objects.ABSOLUTE, rests));
		
		Denotator expectedGeneralScore = this.objects.createGeneralScore(
				new double[][]{{0,58,120,0,0,0},{1,61,116,0,0,0},{2,58,121,0,1,0}}, new double[][]{{3,0},{4,2}});
		this.objects.assertEqualPowerDenotators((PowerDenotator)expectedGeneralScore, (PowerDenotator)mappedGeneralScore);
	}

}
