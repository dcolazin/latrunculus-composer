package org.rubato.rubettes.alteration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.rubato.base.RubatoException;
import org.rubato.rubettes.util.MacroNoteGenerator;
import org.rubato.rubettes.util.SimpleFormFinder;
import org.vetronauta.latrunculus.core.math.arith.number.Rational;
import org.vetronauta.latrunculus.core.math.matrix.QMatrix;
import org.vetronauta.latrunculus.core.math.matrix.RMatrix;
import org.vetronauta.latrunculus.core.math.module.generic.ArithmeticElement;
import org.vetronauta.latrunculus.core.math.module.morphism.MappingException;
import org.vetronauta.latrunculus.core.math.module.morphism.ModuleMorphism;
import org.vetronauta.latrunculus.core.math.module.morphism.QFreeAffineMorphism;
import org.vetronauta.latrunculus.core.math.module.morphism.RFreeAffineMorphism;
import org.vetronauta.latrunculus.core.math.module.rational.QProperFreeElement;
import org.vetronauta.latrunculus.core.math.module.real.RElement;
import org.vetronauta.latrunculus.core.math.module.real.RProperFreeElement;
import org.vetronauta.latrunculus.core.math.yoneda.Denotator;

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
		this.morphisms.add(RFreeAffineMorphism.make(new RMatrix(new double[][]{{3,5,7}}), new double[]{0}));
		this.morphisms.add(RFreeAffineMorphism.make(new RMatrix(new double[][]{{1,2,3}}), new double[]{0}));
		QMatrix matrix1 = new QMatrix(new Rational[][]{{new Rational(3),new Rational(5),new Rational(7)}});
		QMatrix matrix2 = new QMatrix(new Rational[][]{{new Rational(1),new Rational(2),new Rational(3)}});
		this.morphisms.add(QFreeAffineMorphism.make(matrix1, new Rational[]{new Rational(0)}));
		this.morphisms.add(QFreeAffineMorphism.make(matrix2, new Rational[]{new Rational(0)}));
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
		assertEquals(m050.map(RProperFreeElement.make(new double[]{1, 0, 0})), new RElement(2));
		assertEquals(m050.map(RProperFreeElement.make(new double[]{0, 1, 0})), new RElement(3.5));
		assertEquals(m050.map(RProperFreeElement.make(new double[]{0, 0, 1})), new RElement(5));
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
		assertEquals(m050.map(QProperFreeElement.make(new Rational[]{one, zero, zero})), new ArithmeticElement<>(new Rational(2)));
		assertEquals(m050.map(QProperFreeElement.make(new Rational[]{zero, one, zero})), new ArithmeticElement<>(new Rational(3.5)));
		assertEquals(m050.map(QProperFreeElement.make(new Rational[]{zero, zero, one})), new ArithmeticElement<>(new Rational(5)));
		assertEquals(m100, m1);
	}
	
}
