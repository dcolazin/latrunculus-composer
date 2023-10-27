package org.rubato.rubettes.bigbang;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.rubato.base.RubatoException;
import org.vetronauta.latrunculus.core.math.yoneda.denotator.Denotator;
import org.vetronauta.latrunculus.core.math.yoneda.denotator.PowerDenotator;
import org.rubato.rubettes.util.MacroNoteGenerator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PowerDenotatorTest {
	
	private final double[][] RELATIVE = new double[][]{
			{0,60,120,1,0,0},{1,3,-4,0,0,0},{1,-3,5,0,1,0}};
	
	private MacroNoteGenerator generator;
	private PowerDenotator macroScore;

	@BeforeEach
	void setUp() {
		this.generator = new MacroNoteGenerator();
		this.macroScore = this.generator.createMultiLevelMacroScore(this.RELATIVE);
	}

	@Test
	void testCopy() throws RubatoException {
		PowerDenotator copy = this.macroScore.deepCopy();
		assertEquals(copy, this.macroScore);
		assertTrue(copy != this.macroScore);
		this.checkIfDeepCopied(copy, this.macroScore, new int[]{0,1,0});
		this.checkIfDeepCopied(copy, this.macroScore, new int[]{0,1,0,1,0});
	}
	
	private void checkIfDeepCopied(Denotator d1, Denotator d2, int[] path) throws RubatoException {
		Denotator insideD1 = d1.get(path);
		Denotator insideD2 = d2.get(path);
		assertEquals(insideD1, insideD2);
		assertTrue(insideD1 != insideD2);
	}

}
