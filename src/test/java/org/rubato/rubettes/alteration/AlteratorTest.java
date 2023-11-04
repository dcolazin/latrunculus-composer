package org.rubato.rubettes.alteration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.rubato.base.RubatoException;
import org.rubato.rubettes.util.MacroNoteGenerator;
import org.rubato.rubettes.util.SimpleFormFinder;
import org.vetronauta.latrunculus.core.exception.MappingException;
import org.vetronauta.latrunculus.core.math.arith.number.RationalWrapper;
import org.vetronauta.latrunculus.core.math.arith.number.RealWrapper;
import org.vetronauta.latrunculus.core.math.element.generic.Vector;
import org.vetronauta.latrunculus.core.math.module.generic.ArithmeticElement;
import org.vetronauta.latrunculus.core.math.module.generic.VectorModule;
import org.vetronauta.latrunculus.core.math.module.impl.QRing;
import org.vetronauta.latrunculus.core.math.module.impl.RRing;
import org.vetronauta.latrunculus.core.math.module.morphism.ModuleMorphism;
import org.vetronauta.latrunculus.core.math.module.morphism.affine.ArithmeticAffineProjection;
import org.vetronauta.latrunculus.core.math.yoneda.denotator.Denotator;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Defines tests for the AlterationRubette class.
 * 
 * @author Florian Thalmann
 */
class AlteratorTest {
	
	private List<ModuleMorphism> morphisms;
	private MacroNoteGenerator noteGenerator;
	private Alterator alterator;

	@BeforeEach
	void setUp() {
		this.morphisms = new ArrayList<>();
		List<RealWrapper> realWrapperList1 = new ArrayList<>();
		realWrapperList1.add(new RealWrapper(3));
		realWrapperList1.add(new RealWrapper(5));
		realWrapperList1.add(new RealWrapper(7));
		List<RealWrapper> realWrapperList2 = new ArrayList<>();
		realWrapperList2.add(new RealWrapper(1));
		realWrapperList2.add(new RealWrapper(2));
		realWrapperList2.add(new RealWrapper(3));
		this.morphisms.add(new ArithmeticAffineProjection<>(RRing.ring, new Vector<>(RRing.ring, ArithmeticElement.listOf(realWrapperList1)), RRing.ring.getZero()));
		this.morphisms.add(new ArithmeticAffineProjection<>(RRing.ring, new Vector<>(RRing.ring, ArithmeticElement.listOf(realWrapperList2)), RRing.ring.getZero()));

		List<RationalWrapper> rationalWrapperList1 = new ArrayList<>();
		rationalWrapperList1.add(new RationalWrapper(3));
		rationalWrapperList1.add(new RationalWrapper(5));
		rationalWrapperList1.add(new RationalWrapper(7));
		List<RationalWrapper> rationalWrapperList2 = new ArrayList<>();
		rationalWrapperList2.add(new RationalWrapper(1));
		rationalWrapperList2.add(new RationalWrapper(2));
		rationalWrapperList2.add(new RationalWrapper(3));
		this.morphisms.add(new ArithmeticAffineProjection<>(QRing.ring, new Vector<>(QRing.ring, ArithmeticElement.listOf(rationalWrapperList1)), QRing.ring.getZero()));
		this.morphisms.add(new ArithmeticAffineProjection<>(QRing.ring, new Vector<>(QRing.ring, ArithmeticElement.listOf(rationalWrapperList2)), QRing.ring.getZero()));

		this.noteGenerator = new MacroNoteGenerator();
		this.alterator = new Alterator();
	}
	
	@Test
	void testAlter() throws RubatoException {
		Denotator note1 = this.noteGenerator.createNoteDenotator(0, 50, 120, 1, 0);
		Denotator note2 = this.noteGenerator.createNoteDenotator(2, 55, 122, 1, 0);
		Denotator note3 = this.noteGenerator.createNoteDenotator(1, 52.5, 121, 1, 0);
		int[][] paths = new SimpleFormFinder(this.noteGenerator.getScoreForm()).getSimpleFormArrayPaths();
		Denotator altered = this.alterator.alter(note1, note2, 0.5, paths);

		assertEquals(altered, note3);
	}

	@Test
	void testMakeMorphism() throws MappingException {
		ModuleMorphism m0 = this.morphisms.get(0);
		ModuleMorphism m1 = this.morphisms.get(1);
		ModuleMorphism m000 = this.alterator.makeAlteredMorphism(m0, m1, 0);
		ModuleMorphism m050 = this.alterator.makeAlteredMorphism(m0, m1, 0.5);
		ModuleMorphism m100 = this.alterator.makeAlteredMorphism(m0, m1, 1);
		assertEquals(m000, m0);
		//assertTrue(m050.equals(new RFreeAffineMorphism(new RMatrix(new double[][]{{2,3.5,5}}), new double[]{0})));
		VectorModule<ArithmeticElement<RealWrapper>> r3 = new VectorModule<>(RRing.ring, 3);
		assertEquals(m050.map(r3.getUnitElement(0)), new ArithmeticElement<>(new RealWrapper(2)));
		assertEquals(m050.map(r3.getUnitElement(1)), new ArithmeticElement<>(new RealWrapper(3.5)));
		assertEquals(m050.map(r3.getUnitElement(2)), new ArithmeticElement<>(new RealWrapper(5)));
		assertEquals(m100, m1);
		
		m0 = this.morphisms.get(2);
		m1 = this.morphisms.get(3);
		m000 = this.alterator.makeAlteredMorphism(m0, m1, 0);
		m050 = this.alterator.makeAlteredMorphism(m0, m1, 0.5);
		m100 = this.alterator.makeAlteredMorphism(m0, m1, 1);
		assertEquals(m000, m0);
		//assertTrue(m050.equals(new RFreeAffineMorphism(new RMatrix(new double[][]{{2,3.5,5}}), new double[]{0})));
		VectorModule<ArithmeticElement<RationalWrapper>> q3 = new VectorModule<>(QRing.ring, 3);
		assertEquals(m050.map(q3.getUnitElement(0)), new ArithmeticElement<>(new RationalWrapper(2)));
		assertEquals(m050.map(q3.getUnitElement(0)), new ArithmeticElement<>(new RationalWrapper(3.5)));
		assertEquals(m050.map(q3.getUnitElement(0)), new ArithmeticElement<>(new RationalWrapper(5)));
		assertEquals(m100, m1);
	}
	
}
