package org.rubato.rubettes.bigbang;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.rubato.rubettes.bigbang.model.BigBangModel;
import org.rubato.rubettes.bigbang.model.BigBangObject;
import org.rubato.rubettes.bigbang.model.BigBangObjects;
import org.rubato.rubettes.bigbang.model.DenotatorValueExtractor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class DenotatorValueExtractorTest {
	
	private BigBangModel model;
	private TestObjects objects;
	
	@BeforeEach
	void setUp() {
		this.objects = new TestObjects();
		this.model = new BigBangModel();
	}
	
	@Test
	void testExtractDisplayObjectsWithMultilevelSoundScore() {
		//TODO reabilitate
		//TODO there are problems of ClassCastException with TreeSet<AbstractOperation> as AbstractOperation is not Comparable
		/*
		this.model.setOrAddComposition(this.objects.multiLevelSoundScore);
		new DenotatorValueExtractor(this.model.getObjects(), this.model.getComposition());
		BigBangObjects nodes = this.model.getObjects();
		assertEquals(3, nodes.getAllObjects().size());
		BigBangObject lastNode = nodes.getAllObjects().last();
		assertEquals(2.0, lastNode.getNthValue(nodes.getCoordinateSystemValueNames().get(0), 0));
		assertEquals(60.0, lastNode.getNthValue(nodes.getCoordinateSystemValueNames().get(1), 0));
		assertEquals(121.0, lastNode.getNthValue(nodes.getCoordinateSystemValueNames().get(2), 0));
		assertEquals(1.0, lastNode.getNthValue(nodes.getCoordinateSystemValueNames().get(3), 0));
		assertEquals(1.0, lastNode.getNthValue(nodes.getCoordinateSystemValueNames().get(4), 0));
		assertEquals(2.0, lastNode.getNthValue(DenotatorValueExtractor.SATELLITE_LEVEL, 0));
		assertEquals(0.0, lastNode.getNthValue(DenotatorValueExtractor.SIBLING_NUMBER, 0));
	*/
	}

	@Test
	void testExtractDisplayObjectsWithQ3() {
		this.model.setOrAddComposition(this.objects.rationalTriples);
		new DenotatorValueExtractor(this.model.getObjects(), this.model.getComposition());
		BigBangObjects triples = this.model.getObjects();
		assertEquals(4, triples.getAllObjects().size());
		BigBangObject lastTriple = triples.getAllObjects().last();
		assertEquals(4.0, lastTriple.getNthValue(triples.getCoordinateSystemValueNames().get(0), 0));
		assertEquals(3.0, lastTriple.getNthValue(triples.getCoordinateSystemValueNames().get(1), 0));
		assertEquals(1.0, lastTriple.getNthValue(triples.getCoordinateSystemValueNames().get(2), 0));
	}

	@Test
	void testExtractDisplayObjectsWithProductRing() {
		this.model.setOrAddComposition(this.objects.realTriples);
		new DenotatorValueExtractor(this.model.getObjects(), this.model.getComposition());
		BigBangObjects triples = this.model.getObjects();
		assertEquals(3, triples.getAllObjects().size());
		BigBangObject lastTriple = triples.getAllObjects().last();
		assertEquals(4.0, lastTriple.getNthValue(triples.getCoordinateSystemValueNames().get(0), 0));
		assertEquals(3.0, lastTriple.getNthValue(triples.getCoordinateSystemValueNames().get(1), 0));
		assertEquals(1.0, lastTriple.getNthValue(triples.getCoordinateSystemValueNames().get(2), 0));
	}

	@Test
	void testExtractDisplayObjectsWithColimit() {
		this.model.setOrAddComposition(this.objects.integerOrReals);
		new DenotatorValueExtractor(this.model.getObjects(), this.model.getComposition());
		BigBangObjects integerOrReals = this.model.getObjects();
		assertEquals(4, integerOrReals.getAllObjects().size());
		assertEquals("Integer Z", integerOrReals.getCoordinateSystemValueNames().get(0));
		assertEquals("Real R", integerOrReals.getCoordinateSystemValueNames().get(1));
		
		//check first element
		BigBangObject first = integerOrReals.getAllObjects().first();
		assertEquals(4.0, first.getNthValue(integerOrReals.getCoordinateSystemValueNames().get(0), 0));
		assertNull(first.getNthValue(integerOrReals.getCoordinateSystemValueNames().get(1), 0));
		//colimit index
		assertEquals(0.0, first.getNthValue(integerOrReals.getCoordinateSystemValueNames().get(2), 0));
		
		//check last element
		BigBangObject last = integerOrReals.getAllObjects().last();
		assertNull(last.getNthValue(integerOrReals.getCoordinateSystemValueNames().get(0), 0));
		assertEquals(3.5, last.getNthValue(integerOrReals.getCoordinateSystemValueNames().get(1), 0));
		//colimit index
		assertEquals(1.0, last.getNthValue(integerOrReals.getCoordinateSystemValueNames().get(2), 0));
	}

}
