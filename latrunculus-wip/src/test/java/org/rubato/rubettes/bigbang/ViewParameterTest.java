package org.rubato.rubettes.bigbang;

import org.junit.jupiter.api.Test;
import org.rubato.rubettes.bigbang.view.model.ViewParameter;

import static org.junit.jupiter.api.Assertions.assertTrue;


class ViewParameterTest {
	
	@Test
	void testGetLimitedValue() {
		ViewParameter relative = new ViewParameter("Test", false, 0, 0, 1, true);
		assertTrue(relative.getLimitedValue(2,0,5) == 2);
		assertTrue(relative.getLimitedValue(6,0,5) == 1);
		assertTrue(relative.getLimitedValue(-7,0,5) == 3);
		relative = new ViewParameter("Test", false, 0, 0, 1, false);
		assertTrue(relative.getLimitedValue(2,0,5) == 2);
		assertTrue(relative.getLimitedValue(6,0,5) == 5);
		assertTrue(relative.getLimitedValue(-2,0,5) == 0);
	}
}
