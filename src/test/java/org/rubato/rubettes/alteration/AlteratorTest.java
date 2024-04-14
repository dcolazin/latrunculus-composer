package org.rubato.rubettes.alteration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.vetronauta.latrunculus.core.exception.RubatoException;
import org.rubato.rubettes.util.MacroNoteGenerator;
import org.rubato.rubettes.util.SimpleFormFinder;
import org.vetronauta.latrunculus.core.exception.MappingException;
import org.vetronauta.latrunculus.core.math.element.generic.Vector;
import org.vetronauta.latrunculus.core.math.element.impl.Rational;
import org.vetronauta.latrunculus.core.math.element.impl.Real;
import org.vetronauta.latrunculus.core.math.module.generic.VectorModule;
import org.vetronauta.latrunculus.core.math.module.impl.QRing;
import org.vetronauta.latrunculus.core.math.module.impl.RRing;
import org.vetronauta.latrunculus.core.math.morphism.ModuleMorphism;
import org.vetronauta.latrunculus.core.math.morphism.affine.AffineProjection;
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
		this.morphisms.add(new AffineProjection<>(RRing.ring, new Vector<>(RRing.ring, realList1), RRing.ring.getZero()));
		this.morphisms.add(new AffineProjection<>(RRing.ring, new Vector<>(RRing.ring, realList2), RRing.ring.getZero()));

		List<Rational> rationalList1 = new ArrayList<>();
		rationalList1.add(new Rational(3));
		rationalList1.add(new Rational(5));
		rationalList1.add(new Rational(7));
		List<Rational> rationalList2 = new ArrayList<>();
		rationalList2.add(new Rational(1));
		rationalList2.add(new Rational(2));
		rationalList2.add(new Rational(3));
		this.morphisms.add(new AffineProjection<>(QRing.ring, new Vector<>(QRing.ring, rationalList1), QRing.ring.getZero()));
		this.morphisms.add(new AffineProjection<>(QRing.ring, new Vector<>(QRing.ring, rationalList2), QRing.ring.getZero()));

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
		VectorModule<Real> r3 = new VectorModule<>(RRing.ring, 3);
		assertEquals(m050.map(r3.getUnitElement(0)), new Real(2));
		assertEquals(m050.map(r3.getUnitElement(1)), new Real(3.5));
		assertEquals(m050.map(r3.getUnitElement(2)), new Real(5));
		assertEquals(m100, m1);
		
		m0 = this.morphisms.get(2);
		m1 = this.morphisms.get(3);
		m000 = this.alterator.makeAlteredMorphism(m0, m1, 0);
		m050 = this.alterator.makeAlteredMorphism(m0, m1, 0.5);
		m100 = this.alterator.makeAlteredMorphism(m0, m1, 1);
		assertEquals(m000, m0);
		//assertTrue(m050.equals(new RFreeAffineMorphism(new RMatrix(new double[][]{{2,3.5,5}}), new double[]{0})));
		VectorModule<Rational> q3 = new VectorModule<>(QRing.ring, 3);
		assertEquals(m050.map(q3.getUnitElement(0)), new Rational(2));
		assertEquals(m050.map(q3.getUnitElement(1)), new Rational(3.5));
		assertEquals(m050.map(q3.getUnitElement(2)), new Rational(5));
		assertEquals(m100, m1);
	}
	
}
