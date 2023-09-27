package org.rubato.rubettes.bigbang;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.rubato.base.RubatoException;
import org.rubato.math.yoneda.Denotator;
import org.rubato.rubettes.bigbang.model.BigBangModel;
import org.rubato.rubettes.bigbang.model.DenotatorValueExtractor;
import org.rubato.rubettes.bigbang.view.subview.DisplayObjects;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DisplayObjectsTest {
	
	private TestObjects objects;

	@BeforeEach
	void setUp() {
		this.objects = new TestObjects();
	}

	@Test
	void testCrucialMethods() throws RubatoException {
		double[][] rests = new double[][]{{3,1},{4,3}};
		DisplayObjects objects = this.createDisplayObjects(this.objects.createGeneralScore(this.objects.ABSOLUTE, rests));
		objects.setActiveColimitCoordinate(0, 0);
		assertEquals(0, objects.getActiveObjectValueIndex(0));
		assertEquals(1, objects.getActiveObjectValueIndex(1));
		assertEquals(2, objects.getActiveObjectValueIndex(2));
		assertEquals(3, objects.getActiveObjectValueIndex(3));
		assertEquals(4, objects.getActiveObjectValueIndex(4));
		
		objects.setActiveColimitCoordinate(0, 1);
		assertEquals(0, objects.getActiveObjectValueIndex(0));
		assertEquals(-1, objects.getActiveObjectValueIndex(1));
		assertEquals(-1, objects.getActiveObjectValueIndex(2));
		assertEquals(1, objects.getActiveObjectValueIndex(3));
		assertEquals(2, objects.getActiveObjectValueIndex(4));
	}

	@Test
	void testWithMultipleOccurrencesOfName() {
		DisplayObjects objects = this.createDisplayObjects(this.objects.createDyad(new double[]{60,63}));
		assertEquals(0, objects.getActiveObjectValueIndex(0));
		assertEquals(1, objects.getActiveObjectValueIndex(1));
	}
	
	private DisplayObjects createDisplayObjects(Denotator denotator) {
		BigBangModel model = new BigBangModel();
		model.setOrAddComposition(denotator);
		new DenotatorValueExtractor(model.getObjects(), model.getComposition());
		DisplayObjects objects = new DisplayObjects(model.getObjects());
		objects.addObjects(model.getObjects().getObjectsAt(null));
		return objects;
	}

}
