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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.util.Iterator;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.rubato.base.RubatoException;
import org.vetronauta.latrunculus.core.math.module.rational.QElement;
import org.rubato.math.yoneda.Denotator;
import org.rubato.math.yoneda.PowerDenotator;
import org.rubato.rubettes.util.ScaleMap;
import org.vetronauta.latrunculus.server.xml.XMLReader;
import org.vetronauta.latrunculus.server.xml.XMLWriter;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;

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
	void testScaleRubette() throws RubatoException {
		this.rubette.getProperties();
		assertTrue(this.rubette.applyProperties());
		PowerDenotator scale = this.rubette.generateScale();
		assertTrue(scale.getFactorCount() == 75);
		this.rubette.setTempRootNote(59);
		this.rubette.applyProperties();
		scale = this.rubette.generateScale();
		assertTrue(scale.getFactorCount() == 74);
		
		//compare steps for the ionian scale
		double[] ionian = this.rubette.getScaleMap().get("ionian");
		Iterator<Denotator> steps = scale.getFactors().iterator();
		int[] path = new int[]{1,0};
		double previousPitch = ((QElement)steps.next().getElement(path)).getValue().doubleValue();
		double currentPitch;
		//starts at 1, due to root note 59...
		int currentStep = 1;
		while (steps.hasNext()) {
			currentPitch = ((QElement)steps.next().getElement(path)).getValue().doubleValue();
			assertTrue(currentPitch-previousPitch == ionian[currentStep%7]);
			previousPitch = currentPitch;
			currentStep++;
		}
		
		//test getInfo()
		assertTrue(this.rubette.getInfo().equals("ionian"));
	}

	@Test
	void testToAndFromXML() throws Exception {
		//make file, writer and reader
		File testFile = new File("./srTest");
		if (!testFile.exists()) {
			testFile.createNewFile();
		}
		XMLWriter writer = new XMLWriter(testFile);
		Reader bufferedReader = new BufferedReader(new FileReader(testFile));
		XMLReader reader = new XMLReader(bufferedReader);
		DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		
		//test a preset
		this.rubette.getProperties();
		this.rubette.setTempPreset(2);
		this.rubette.applyProperties();
		writer.open();
		this.rubette.toXML(writer);
		writer.close();
		Element element = builder.parse(new InputSource(bufferedReader)).getDocumentElement();
		reader.parse();
		this.rubette = (ScaleRubette)this.rubette.fromXML(reader, element);
		assertTrue(this.rubette.getInfo().equals("dorian"));
		
		testFile.createNewFile();
		writer = new XMLWriter(testFile);
		bufferedReader = new BufferedReader(new FileReader(testFile));
		
		//test 'custom'
		this.rubette.setTempPreset(0);
		this.rubette.applyProperties();
		writer.open();
		this.rubette.toXML(writer);
		writer.close();
		reader.parse();
		element = builder.parse(new InputSource(bufferedReader)).getDocumentElement();
		this.rubette = (ScaleRubette)this.rubette.fromXML(reader, element);
		assertTrue(this.rubette.getInfo().equals("custom"));
		
		testFile.delete();
	}

	@Test
	void testScaleMap() {
		ScaleMap map = new ScaleMap();
		assertTrue(map.size() == 13);
	}
	
}
