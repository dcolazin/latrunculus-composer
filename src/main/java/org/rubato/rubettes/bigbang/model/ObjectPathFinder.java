package org.rubato.rubettes.bigbang.model;

import java.util.Set;
import java.util.TreeSet;

import org.vetronauta.latrunculus.core.math.yoneda.denotator.ColimitDenotator;
import org.vetronauta.latrunculus.core.math.yoneda.denotator.Denotator;
import org.vetronauta.latrunculus.core.math.yoneda.denotator.FactorDenotator;
import org.vetronauta.latrunculus.core.math.yoneda.FormDenotatorTypeEnum;
import org.vetronauta.latrunculus.core.math.yoneda.denotator.LimitDenotator;
import org.rubato.rubettes.util.DenotatorPath;

public class ObjectPathFinder {
	
	private Set<DenotatorPath> foundObjectPaths = new TreeSet<DenotatorPath>(); 
	private boolean powersetOrListFound;
	
	public Set<DenotatorPath> findPaths(Denotator currentDenotator, DenotatorPath currentPath) {
		this.addObjectPathsToNewPaths(currentDenotator, currentPath);
		return this.foundObjectPaths;
	}
	
	public Set<DenotatorPath> findPaths(Denotator currentDenotator) {
		DenotatorPath topPath = new DenotatorPath(currentDenotator.getForm());
		this.addObjectPathsToNewPaths(currentDenotator, topPath);
		if (currentDenotator.getForm().getType() != FormDenotatorTypeEnum.POWER) {
			this.foundObjectPaths.add(topPath);
		}
		return this.foundObjectPaths;
	}
	
	public boolean powersetOrListFound() {
		return this.powersetOrListFound;
	}
	
	//recursive search method
	private void addObjectPathsToNewPaths(Denotator currentDenotator, DenotatorPath currentPath) {
		FormDenotatorTypeEnum denotatorType = currentDenotator.getType();
		if (currentDenotator.getType() == FormDenotatorTypeEnum.POWER || currentDenotator.getType() == FormDenotatorTypeEnum.LIST) {
			this.powersetOrListFound = true;
			FactorDenotator currentFactorDenotator = (FactorDenotator)currentDenotator;
			for (int i = 0; i < currentFactorDenotator.getFactorCount(); i++) {
				DenotatorPath currentChildPath = currentPath.getChildPath(i);
				this.foundObjectPaths.add(currentChildPath);
				this.addObjectPathsToNewPaths(currentFactorDenotator.getFactor(i), currentChildPath);
			}
		} else if (denotatorType == FormDenotatorTypeEnum.LIMIT) {
			LimitDenotator currentLimit = (LimitDenotator)currentDenotator;
			for (int i = 0; i < currentLimit.getFactorCount(); i++) {
				Denotator currentChild = currentLimit.getFactor(i);
				this.addObjectPathsToNewPaths(currentChild, currentPath.getChildPath(i));
			}
		} else if (denotatorType == FormDenotatorTypeEnum.COLIMIT) {
			ColimitDenotator currentColimit = (ColimitDenotator)currentDenotator;
			Denotator onlyChild = currentColimit.getFactor();
			int childIndex = currentColimit.getIndex();
			for (int i = 0; i < currentColimit.getForm().getFormCount(); i++) {
				if (i == childIndex) {
					DenotatorPath childPath = currentPath.getChildPath(childIndex);
					this.addObjectPathsToNewPaths(onlyChild, childPath);
				}
			}
		}
	}

}
