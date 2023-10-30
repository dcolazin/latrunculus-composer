package org.rubato.rubettes.alteration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.rubato.base.RubatoException;
import org.rubato.rubettes.util.MacroNoteGenerator;
import org.rubato.rubettes.util.SimpleFormFinder;
import org.vetronauta.latrunculus.core.math.arith.number.Rational;
import org.vetronauta.latrunculus.core.math.arith.number.Real;
import org.vetronauta.latrunculus.core.exception.MappingException;
import org.vetronauta.latrunculus.core.math.module.generic.ArithmeticElement;
import org.vetronauta.latrunculus.core.math.module.generic.ArithmeticMultiElement;
import org.vetronauta.latrunculus.core.math.module.morphism.ModuleMorphism;
import org.vetronauta.latrunculus.core.math.module.morphism.affine.ArithmeticAffineProjection;
import org.vetronauta.latrunculus.core.math.module.rational.QRing;
import org.vetronauta.latrunculus.core.math.module.real.RRing;
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
		List<Real> realList1 = new ArrayList<>();
		realList1.add(new Real(3));
		realList1.add(new Real(5));
		realList1.add(new Real(7));
		List<Real> realList2 = new ArrayList<>();
		realList2.add(new Real(1));
		realList2.add(new Real(2));
		realList2.add(new Real(3));
		this.morphisms.add(new ArithmeticAffineProjection<>(RRing.ring, new ArithmeticMultiElement<>(RRing.ring, ArithmeticElement.listOf(realList1)), RRing.ring.getZero()));
		this.morphisms.add(new ArithmeticAffineProjection<>(RRing.ring, new ArithmeticMultiElement<>(RRing.ring, ArithmeticElement.listOf(realList2)), RRing.ring.getZero()));

		List<Rational> rationalList1 = new ArrayList<>();
		rationalList1.add(new Rational(3));
		rationalList1.add(new Rational(5));
		rationalList1.add(new Rational(7));
		List<Rational> rationalList2 = new ArrayList<>();
		rationalList2.add(new Rational(1));
		rationalList2.add(new Rational(2));
		rationalList2.add(new Rational(3));
		this.morphisms.add(new ArithmeticAffineProjection<>(QRing.ring, new ArithmeticMultiElement<>(QRing.ring, ArithmeticElement.listOf(rationalList1)), QRing.ring.getZero()));
		this.morphisms.add(new ArithmeticAffineProjection<>(QRing.ring, new ArithmeticMultiElement<>(QRing.ring, ArithmeticElement.listOf(rationalList2)), QRing.ring.getZero()));

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
		assertEquals(m050.map(ArithmeticMultiElement.make(RRing.ring, new Real[]{new Real(1), new Real(0), new Real(0)})),
				new ArithmeticElement<>(new Real(2)));
		assertEquals(m050.map(ArithmeticMultiElement.make(RRing.ring, new Real[]{new Real(0), new Real(1), new Real(0)})),
				new ArithmeticElement<>(new Real(3.5)));
		assertEquals(m050.map(ArithmeticMultiElement.make(RRing.ring, new Real[]{new Real(0), new Real(0), new Real(1)})),
				new ArithmeticElement<>(new Real(5)));
		assertEquals(m100, m1);
		
		m0 = this.morphisms.get(2);
		m1 = this.morphisms.get(3);
		m000 = this.alterator.makeAlteredMorphism(m0, m1, 0);
		m050 = this.alterator.makeAlteredMorphism(m0, m1, 0.5);
		m100 = this.alterator.makeAlteredMorphism(m0, m1, 1);
		assertEquals(m000, m0);
		//assertTrue(m050.equals(new RFreeAffineMorphism(new RMatrix(new double[][]{{2,3.5,5}}), new double[]{0})));
		Rational zero = new Rational(0);
		Rational one = new Rational(1);
		assertEquals(m050.map(ArithmeticMultiElement.make(QRing.ring, new Rational[]{one, zero, zero})), new ArithmeticElement<>(new Rational(2)));
		assertEquals(m050.map(ArithmeticMultiElement.make(QRing.ring, new Rational[]{zero, one, zero})), new ArithmeticElement<>(new Rational(3.5)));
		assertEquals(m050.map(ArithmeticMultiElement.make(QRing.ring, new Rational[]{zero, zero, one})), new ArithmeticElement<>(new Rational(5)));
		assertEquals(m100, m1);
	}
	
}
