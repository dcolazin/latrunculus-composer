package org.rubato.rubettes.util;

import java.util.List;

import org.vetronauta.latrunculus.core.math.yoneda.denotator.Denotator;
import org.vetronauta.latrunculus.core.math.yoneda.denotator.PowerDenotator;

public class DenotatorAnalyzer {
	
	//TODO: take that functionality out of ObjectGenerator!!!
	private ObjectGenerator generator = new ObjectGenerator();
	
	public double[] getMinAndMaxValue(PowerDenotator powerset, int valueIndex) {
		//TODO: how to deal with colimits??????????
		List<Denotator> factors = powerset.getFactors();
		List<DenotatorPath> formValuePaths = new DenotatorValueFinder(factors.get(0), false).getValuePaths();
		if (formValuePaths.size() > valueIndex) {
			DenotatorPath valuePath = formValuePaths.get(valueIndex);
			double currentMin = Double.MAX_VALUE;
			double currentMax = Double.MIN_VALUE;
			for (Denotator currentFactor : factors) {
				double currentValue = this.generator.getDoubleValue(currentFactor, valuePath);
				currentMin = Math.min(currentMin, currentValue);
				currentMax = Math.max(currentMax, currentValue);
			}
			return new double[]{currentMin, currentMax};
		}
		return null;
	}

}
