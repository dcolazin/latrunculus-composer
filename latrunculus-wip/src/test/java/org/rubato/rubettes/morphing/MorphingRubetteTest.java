/*
 * Copyright (C) 2007 Florian Thalmann
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of version 2 of the GNU General Public
 * License as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this program; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place - Suite 330,
 * Boston, MA 02111-1307, USA.
 *
 */

package org.rubato.rubettes.morphing;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.vetronauta.latrunculus.core.exception.LatrunculusCheckedException;
import org.rubato.rubettes.util.MacroNoteGenerator;
import org.vetronauta.latrunculus.core.math.element.impl.Rational;
import org.vetronauta.latrunculus.core.math.element.impl.Real;
import org.vetronauta.latrunculus.core.math.yoneda.denotator.Denotator;
import org.vetronauta.latrunculus.core.math.yoneda.denotator.PowerDenotator;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Defines tests for the MorphingRubette class.
 * 
 * @author Florian Thalmann
 */
class MorphingRubetteTest {
	
	private MacroNoteGenerator noteGenerator;
	private MorphingRubette rubette;
	private PowerDenotator score1, score2, score3;
	
	@BeforeEach
	protected void setUp() {
		this.noteGenerator = new MacroNoteGenerator();
		this.rubette = new MorphingRubette();
		this.score1 = this.noteGenerator.createSimpleMelody(1, 69, 74, 73, 82, 67, 71);
		this.score2 = this.noteGenerator.createSimpleMelody(1, 67, 67, 69, 73);
		this.score3 = this.noteGenerator.createSimpleMelody(1, 69, 74, 73, 82);
	}
	
	@Test
	void testGetTimeInfo() throws LatrunculusCheckedException {
		double[] timeInfo1 = this.rubette.getTimeInfo(score1);
		double[] timeInfo2 = this.rubette.getTimeInfo(score2);
		assertTrue(timeInfo1[0] == 0 && timeInfo1[1] == 5);
		assertTrue(timeInfo2[0] == 0 && timeInfo2[1] == 3);
	}

	@Test
	void testMakeTimeCorrection() throws LatrunculusCheckedException {
		this.rubette.makeTimeCorrection(score1, score2);
		double[] timeInfo1 = this.rubette.getTimeInfo(score1);
		double[] timeInfo2 = this.rubette.getTimeInfo(score2);
		//check onsets
		assertTrue(timeInfo1[0] == 0 && timeInfo1[1] == 4);
		assertTrue(timeInfo2[0] == 0 && timeInfo2[1] == 4);
		//check durations
		//System.out.println("l"+this.getDuration(score2.getFactor(0)));
		assertTrue(this.getDuration(score1.getFactor(0)) < 1);
		assertTrue(this.getDuration(score1.getFactor(4)) < 84.0/125);
		assertTrue(this.getDuration(score1.getFactor(5)) < Math.pow(4.0/5,2));
		assertTrue(this.getDuration(score2.getFactor(0)) > 1);
		assertTrue(this.getDuration(score2.getFactor(3)) < 1);
	}

	@Test
	void testMakeTimeCorrection2() throws LatrunculusCheckedException {
		this.rubette.makeTimeCorrection(score2, score1);
		double[] timeInfo1 = this.rubette.getTimeInfo(score1);
		double[] timeInfo2 = this.rubette.getTimeInfo(score2);
		//check onsets
		assertTrue(timeInfo1[0] == 0 && timeInfo1[1] == 4);
		assertTrue(timeInfo2[0] == 0 && timeInfo2[1] == 4);
		//check durations
		//System.out.println("l"+this.getDuration(score1.getFactor(0)));
		assertTrue(this.getDuration(score1.getFactor(0)) == 0.6400000000000001); //12/25
		assertTrue(this.getDuration(score1.getFactor(4)) < 1);
		assertTrue(this.getDuration(score1.getFactor(5)) > 1);
		assertTrue(this.getDuration(score2.getFactor(0)) > 1);
		assertTrue(this.getDuration(score2.getFactor(3)) > 4.0/3);
	}

	@Test
	void testGetMorph() throws LatrunculusCheckedException {
		this.rubette.setInput(score2,score3);
		this.rubette.makeTimeCorrection(score2, score3);
		PowerDenotator morph = this.rubette.getMorph();
		List<Denotator> factors = morph.getFactors();
		int size = factors.size();
		assertTrue(size <= 8);
		
		assertTrue(this.getPitch(factors.get(0)) == 67);
		assertTrue(this.getPitch(factors.get(size-1)) == 82);
		
		Set<Double> possiblePitches = new HashSet<Double>();
		possiblePitches.add(new Double(67));
		
		possiblePitches.add(new Double(82));
		
		for (int i = 0; i < factors.size(); i++) {
			//System.out.println(new Double(this.getPitch(factors.get(i))));
			//assertTrue(possiblePitches.contains(new Double(this.getPitch(factors.get(i)))));
		}
	}
	
	private double getPitch(Denotator denotator) throws LatrunculusCheckedException {
		return ((Rational)denotator.getElement(new int[]{1,0})).doubleValue();
	}
	
	private double getDuration(Denotator denotator) throws LatrunculusCheckedException {
		return ((Real)denotator.getElement(new int[]{3,0})).getValue();
	}

}
