/*
 * Copyright (C) 2006 Florian Thalmann
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

package org.rubato.rubettes.score;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.rubato.rubettes.alteration.AlterationRubette;
import org.vetronauta.latrunculus.core.exception.LatrunculusCheckedException;
import org.rubato.rubettes.util.ScaleMap;
import org.vetronauta.latrunculus.core.math.element.impl.Rational;
import org.vetronauta.latrunculus.core.math.yoneda.denotator.Denotator;
import org.vetronauta.latrunculus.core.math.yoneda.denotator.PowerDenotator;
import org.vetronauta.latrunculus.plugin.xml.reader.DefaultRubetteXmlReader;
import org.vetronauta.latrunculus.server.xml.XMLReader;
import org.vetronauta.latrunculus.server.xml.XMLWriter;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Defines tests for the ScaleRubette class.
 * 
 * @author Florian Thalmann
 */
class ScaleRubetteTest {
	
	private ScaleRubette rubette;

	@BeforeEach
	void setUp() {
		this.rubette = new ScaleRubette();
	}
	
	@Test
	void testScaleRubette() throws LatrunculusCheckedException {
		this.rubette.getProperties();
		assertTrue(this.rubette.applyProperties());
		PowerDenotator scale = this.rubette.generateScale();
		assertEquals(75, scale.getFactorCount());
		this.rubette.setTempRootNote(59);
		this.rubette.applyProperties();
		scale = this.rubette.generateScale();
		assertEquals(74, scale.getFactorCount());
		
		//compare steps for the ionian scale
		double[] ionian = this.rubette.getScaleMap().get("ionian");
		Iterator<Denotator> steps = scale.getFactors().iterator();
		int[] path = new int[]{1,0};
		double previousPitch = ((Rational)steps.next().getElement(path)).doubleValue();
		double currentPitch;
		//starts at 1, due to root note 59...
		int currentStep = 1;
		while (steps.hasNext()) {
			currentPitch = ((Rational)steps.next().getElement(path)).doubleValue();
			assertEquals(currentPitch - previousPitch, ionian[currentStep % 7]);
			previousPitch = currentPitch;
			currentStep++;
		}
		
		//test getInfo()
		assertEquals("ionian", this.rubette.getInfo());
	}

	@Test
	void testToAndFromXML() throws Exception {
		//make file, writer and reader
		File testFile = new File("./srTest");
		if (!testFile.exists()) {
			testFile.createNewFile();
		}
		try {
			XMLWriter writer = new XMLWriter(testFile);
			Reader bufferedReader = new BufferedReader(new FileReader(testFile));
			XMLReader reader = new XMLReader(bufferedReader);
			DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();

			//test a preset
			this.rubette.getProperties();
			this.rubette.setTempPreset(2);
			this.rubette.applyProperties();
			writer.open();
			writer.writeRubette(this.rubette);
			writer.close();
			Element element = builder.parse(new InputSource(bufferedReader)).getDocumentElement();
			reader.parse();
			this.rubette = (ScaleRubette) new DefaultRubetteXmlReader().fromXML(element, ScaleRubette.class, reader);
			assertEquals("dorian", this.rubette.getInfo());

			testFile.createNewFile();
			writer = new XMLWriter(testFile);
			bufferedReader = new BufferedReader(new FileReader(testFile));

			//test 'custom'
			this.rubette.setTempPreset(0);
			this.rubette.applyProperties();
			writer.open();
			writer.writeRubette(rubette);
			writer.close();
			reader.parse();
			element = builder.parse(new InputSource(bufferedReader)).getDocumentElement();
			this.rubette = (ScaleRubette) new DefaultRubetteXmlReader().fromXML(element, ScaleRubette.class, reader);
			assertEquals("custom", this.rubette.getInfo());
		} finally {
			testFile.delete();
		}
	}

	@Test
	void testScaleMap() {
		ScaleMap map = new ScaleMap();
		assertEquals(13, map.size());
	}
	
}
