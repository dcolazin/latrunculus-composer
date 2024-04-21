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

package org.rubato.rubettes.wallpaper;
//
//  WallpaperRubetteTest.java
//  WallpaperRubette
//
//  Created by Florian Thalmann on 5/29/06.
//  Copyright 2006 __MyCompanyName__. All rights reserved.
//

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.vetronauta.latrunculus.core.repository.Repository;
import org.vetronauta.latrunculus.core.exception.LatrunculusCheckedException;
import org.vetronauta.latrunculus.core.math.element.generic.Vector;
import org.vetronauta.latrunculus.core.math.element.impl.Rational;
import org.vetronauta.latrunculus.core.math.element.impl.Real;
import org.vetronauta.latrunculus.core.math.element.impl.ZInteger;
import org.vetronauta.latrunculus.core.math.matrix.RMatrix;
import org.vetronauta.latrunculus.core.math.module.generic.Module;
import org.vetronauta.latrunculus.core.math.module.impl.RRing;
import org.vetronauta.latrunculus.core.math.module.impl.ZRing;
import org.vetronauta.latrunculus.core.math.morphism.ModuleMorphism;
import org.vetronauta.latrunculus.core.math.morphism.affine.AffineFreeMorphism;
import org.vetronauta.latrunculus.core.math.morphism.affine.AffineRingMorphism;
import org.vetronauta.latrunculus.core.math.yoneda.denotator.Denotator;
import org.vetronauta.latrunculus.core.math.yoneda.denotator.LimitDenotator;
import org.vetronauta.latrunculus.core.math.yoneda.denotator.NameDenotator;
import org.vetronauta.latrunculus.core.math.yoneda.denotator.PowerDenotator;
import org.vetronauta.latrunculus.core.math.yoneda.denotator.SimpleDenotator;
import org.vetronauta.latrunculus.core.math.yoneda.form.LimitForm;
import org.vetronauta.latrunculus.core.math.yoneda.form.PowerForm;
import org.vetronauta.latrunculus.core.math.yoneda.form.SimpleForm;
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
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Defines tests for the WallpaperRubette class.
 * 
 * @author Florian Thalmann
 */
class WallpaperRubetteTest {

	private PowerDenotator denotator;
	private List<ModuleMorphism> morphisms;
	private WallpaperRubette rubette;
	
	private NameDenotator emptyName = NameDenotator.make("");
	private Repository systemRepository = Repository.systemRepository();
	private LimitForm noteForm = (LimitForm)systemRepository.getForm("Note");
	private PowerForm scoreForm = (PowerForm)systemRepository.getForm("Score");
	private SimpleForm loudnessForm = (SimpleForm)systemRepository.getForm("Loudness");
	private PowerForm realSetForm = (PowerForm)systemRepository.getForm("RealSet");
	private SimpleForm realForm = (SimpleForm)systemRepository.getForm("Real");

	@BeforeEach
	void setUp() {
		Denotator note = this.noteForm.createDefaultDenotator();
		List<Denotator> noteList = new ArrayList<>();
		noteList.add(note);
		try {
			this.denotator = new PowerDenotator(this.emptyName, this.scoreForm, noteList);
		} catch (LatrunculusCheckedException e) { e.printStackTrace(); }
		this.rubette = (WallpaperRubette)new WallpaperRubette().newInstance();
		this.rubette.setInputForm(this.scoreForm);
		this.morphisms = new ArrayList<>();
		this.morphisms.add(new AffineRingMorphism<>(RRing.ring, new Real((2)), new Real((1))));
		List<Real> list = new ArrayList<>();
		list.add(new Real((1)));
		list.add(new Real((2)));
		ModuleMorphism m = AffineFreeMorphism.make(RRing.ring, new RMatrix(new double[][]{{1,1},{1,1}}), new Vector<>(RRing.ring, list));
		this.morphisms.add(m);
	}
	
	@Test
	void testMapDenotatorReal() throws LatrunculusCheckedException {
		this.assertThisDenotatorAsDefault();
			
		this.rubette.addMorphism(this.morphisms.get(0), 0, 1, this.createSimplePaths(0, 0));
		this.denotator = this.rubette.mapDenotator(this.denotator, this.morphisms.get(0));
		assertEquals(1, ((Real) this.denotator.getElement(new int[]{0, 0, 0})).getValue());
		
		this.rubette.addMorphism(this.morphisms.get(1), 0, 1, this.createSimplePaths(0, 1, 0, 1));
		this.denotator = this.rubette.mapDenotator(this.denotator, this.morphisms.get(1));
		assertEquals(2, ((Real) this.denotator.getElement(new int[]{0, 0, 0})).getValue());
		assertEquals(((Rational) this.denotator.getElement(new int[]{0, 1, 0})), new Rational(3));
	}

	@Test
	void testMapDenotatorInteger() throws LatrunculusCheckedException {
		this.morphisms.add(new AffineRingMorphism<>(ZRing.ring, new ZInteger(2), new ZInteger(1)));
		SimpleDenotator newLoudness = new SimpleDenotator(emptyName, loudnessForm, new ZInteger(3));
		((LimitDenotator)this.denotator.getFactor(0)).setFactor(2, newLoudness);

		assertEquals(3, ((ZInteger) this.denotator.getElement(new int[]{0, 2, 0})).intValue());
		
		this.rubette.addMorphism(this.morphisms.get(2), 0, 1, this.createSimplePaths(2, 2));
		this.denotator = this.rubette.mapDenotator(this.denotator, this.morphisms.get(2));
		assertEquals(7, ((ZInteger) this.denotator.getElement(new int[]{0, 2, 0})).intValue());
	}

	@Test
	void testGetUnitedMappedDenotatorsWithOneMorphism() throws LatrunculusCheckedException {
		this.assertThisDenotatorAsDefault();
			
		this.rubette.addMorphism(this.morphisms.get(1), 1, 3, this.createSimplePaths(0, 1, 0, 1));
		this.denotator = this.rubette.getUnitedMappedDenotators(this.denotator);
		assertEquals(3, this.denotator.getFactorCount());
		this.assertDenotatorFactor(0, 1, 2);
		this.assertDenotatorFactor(1, 4, 5);
		this.assertDenotatorFactor(2, 10, 11);
	}

	@Test
	void testGetUnitedMappedDenotatorsWithTwoMorphisms() throws LatrunculusCheckedException {
		this.assertThisDenotatorAsDefault();
			
		this.rubette.addMorphism(this.morphisms.get(0), 0, 1, this.createSimplePaths(1, 1));
		this.rubette.addMorphism(this.morphisms.get(1), 0, 1, this.createSimplePaths(0, 1, 0, 1));
		this.denotator = this.rubette.getUnitedMappedDenotators(this.denotator);
		assertEquals(4, this.denotator.getFactorCount());
		this.assertDenotatorFactor(0, 0, 0);
		this.assertDenotatorFactor(1, 0, 1);
		this.assertDenotatorFactor(2, 1, 2);
		this.assertDenotatorFactor(3, 1, 5);
	}

	@Test
	@Disabled("deserialization will work, eventually")
	void testGetUnitedMappedDenotatorsWithToAndFromXML() throws Exception {
		this.assertThisDenotatorAsDefault();
			
		this.rubette.setInputForm(this.scoreForm);
		this.rubette.addMorphism(this.morphisms.get(1), 1, 3, this.createSimplePaths(0, 1, 0, 1));
		this.rubette.addMorphism(this.morphisms.get(0), 0, 1, this.createSimplePaths(1, 1));
		
		File testFile = new File("./wprTest");
		if (!testFile.exists()) {
			testFile.createNewFile();
		}

		try {
			Reader bufferedReader = new BufferedReader(new FileReader(testFile));
			//PrintStream stream = new PrintStream(new ByteArrayOutputStream());
			XMLWriter writer = new XMLWriter(testFile);
			XMLReader reader = new XMLReader(bufferedReader);
			writer.open();
			writer.writeRubette(this.rubette);
			writer.close();
			DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Element element = builder.parse(new InputSource(bufferedReader)).getDocumentElement();
			reader.parse();
			this.rubette = (WallpaperRubette) this.rubette.fromXML(reader, element);
			this.denotator = this.rubette.getUnitedMappedDenotators(this.denotator);
		} finally {
			testFile.delete();
		}

		assertTrue(this.rubette.getTempInputForm().equals(this.scoreForm));
		assertEquals(6, this.denotator.getFactorCount());
		this.assertDenotatorFactor(0, 1, 2);
		this.assertDenotatorFactor(1, 2, 3);
		this.assertDenotatorFactor(2, 4, 5);
		this.assertDenotatorFactor(3, 6, 7);
		this.assertDenotatorFactor(4, 10, 11);
		this.assertDenotatorFactor(5, 14, 15);
	}

	@Test
	void testGetUnitedMappedDenotatorsWithRange() throws LatrunculusCheckedException {
		this.assertThisDenotatorAsDefault();
			
		this.rubette.addMorphism(this.morphisms.get(0), 2, 3, this.createSimplePaths(1, 1));
		this.rubette.addMorphism(this.morphisms.get(1), 1, 4, this.createSimplePaths(0, 1, 0, 1));
		this.denotator = this.rubette.getUnitedMappedDenotators(this.denotator);
		assertEquals(8, this.denotator.getFactorCount());
		this.assertDenotatorFactor(0, 1, 11);
		this.assertDenotatorFactor(1, 1, 23);
		this.assertDenotatorFactor(2, 4, 23);
		this.assertDenotatorFactor(3, 4, 47);
		this.assertDenotatorFactor(4, 10, 47);
		this.assertDenotatorFactor(5, 10, 95);
		this.assertDenotatorFactor(6, 22, 95);
		this.assertDenotatorFactor(7, 22, 191);
	}
	
	/**
	 * Tests the Wallpaper rubette with a RealSet denotator.
	 * @throws LatrunculusCheckedException
	 */
	@Test
	void testGetUnitedMappedDenotatorsWithRealSet() throws LatrunculusCheckedException {
		this.assertThisDenotatorAsDefault();
		
		//the paths in the list must be empty, because the realSet's elements are Simples themselves
		List<List<Integer>> paths = this.createSimplePaths();
		paths.add(new ArrayList<Integer>());
		paths.add(new ArrayList<Integer>());
		this.rubette.addMorphism(this.morphisms.get(0), 1, 3, paths);
		List<Denotator> realList = new ArrayList<Denotator>();
		realList.add(this.realForm.createDefaultDenotator());
		this.denotator = new PowerDenotator(this.emptyName, this.realSetForm, realList);
		this.denotator = this.rubette.getUnitedMappedDenotators(this.denotator);
		//assertTrue(this.denotator.getFactorCount() == 3);
		
		SimpleDenotator d = (SimpleDenotator)this.realForm.createDefaultDenotator();
		ModuleMorphism m = d.getModuleMorphism();
		m = this.morphisms.get(0).compose(m);
		d = new SimpleDenotator(this.emptyName, this.realForm, m);
	}
	
	private void assertDenotatorFactor(int factorIndex, int onsetValue, int pitchValue) throws LatrunculusCheckedException {
		Denotator factor = this.denotator.getFactor(factorIndex);
		assertEquals(new Real(onsetValue), factor.getElement(new int[]{0, 0}));
		assertEquals(new Rational(pitchValue), (factor.getElement(new int[]{1, 0})));
	}
	
	private void assertThisDenotatorAsDefault() throws LatrunculusCheckedException {
		assertEquals(0, ((Real) this.denotator.getElement(new int[]{0, 0, 0})).getValue());
		assertEquals((this.denotator.getElement(new int[]{0, 1, 0})), new Rational(0));
		assertEquals(0, ((ZInteger) this.denotator.getElement(new int[]{0, 2, 0})).intValue());
		assertEquals(0, ((Real) this.denotator.getElement(new int[]{0, 3, 0})).getValue());
	}

	@Test
	void testJSelectMorphismElements() {
		JSelectSimpleForms selectElements = new JSelectSimpleForms(this.rubette, null);
		assertNull(selectElements.getElements());
		assertEquals(0, selectElements.getNumberOfFormBoxes());
		//test automatic elements
		selectElements.setMorphism(this.morphisms.get(1));
		List<List<Integer>> coordinatePaths = new ArrayList<>();
		coordinatePaths.add(this.createPath(0));
		coordinatePaths.add(this.createPath(1));
		coordinatePaths.add(this.createPath(0));
		coordinatePaths.add(this.createPath(1));
		assertEquals(4, selectElements.getNumberOfFormBoxes());
		assertEquals(selectElements.getElements(), coordinatePaths);
		//test set and get
		coordinatePaths = new ArrayList<List<Integer>>();
		coordinatePaths.add(this.createPath(2));
		coordinatePaths.add(this.createPath(1));
		coordinatePaths.add(this.createPath(2));
		coordinatePaths.add(this.createPath(3));
		selectElements.setElements(coordinatePaths);
		assertEquals(selectElements.getElements(), coordinatePaths);
		selectElements.dispose();
	}

	@Test
	void testJWallpaperView() {
		ModuleMorphism morphism = this.morphisms.get(1);
		Module domain = morphism.getDomain();
		int domainDim = domain.getDimension();
		List<List<Integer>> coordinates = this.createSimplePaths(0, 1, 0, 1);
		List<List<Integer>> coordinates2 = this.createSimplePaths(0, 0);
		this.rubette.addMorphism(this.morphisms.get(0), 1, 3, coordinates2);
		this.rubette.addMorphism(morphism, 0, 2, coordinates);
		JWallpaperView view = new JWallpaperView(this.rubette.getMorphismsTable());
		List<List<Integer>> indices = view.getIndices(false, domainDim, coordinates);
		assertEquals(indices, this.createSimplePaths(0, 1));
		this.rubette.getView();
	}
	
	private List<List<Integer>> createSimplePaths(int... paths) {
		List<List<Integer>> pathList = new ArrayList<>();
		for (int path : paths) {
			pathList.add(this.createPath(path));
		}
		return pathList;
	}
	
	private List<Integer> createPath(int path) {
		List<Integer> newPath = new ArrayList<>();
		newPath.add(path);
		return newPath;
	}

}
