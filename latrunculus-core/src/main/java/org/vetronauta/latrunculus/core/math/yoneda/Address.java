/*
 * Copyright (C) 2005 Gérard Milmeister
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

package org.vetronauta.latrunculus.core.math.yoneda;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.vetronauta.latrunculus.core.util.Pair;
import org.vetronauta.latrunculus.core.math.module.generic.Module;
import org.vetronauta.latrunculus.core.math.module.generic.NumberRing;
import org.vetronauta.latrunculus.core.math.module.generic.ProductRing;
import org.vetronauta.latrunculus.core.math.module.generic.Ring;
import org.vetronauta.latrunculus.core.math.module.generic.StringRing;
import org.vetronauta.latrunculus.core.math.module.impl.ZRing;
import org.vetronauta.latrunculus.core.math.module.impl.ZnRing;
import org.vetronauta.latrunculus.core.math.module.generic.PolynomialRing;
import org.vetronauta.latrunculus.core.math.yoneda.denotator.Denotator;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Utilities for address management.
 * 
 * @author Gérard Milmeister
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Address {

    private static final HashMap<Pair<Module<?,?>,Module<?,?>>,Ring> RING_ORDER = new HashMap<>();

    /**
     * Returns a common module for the given list of modules.
     * @return null if no common module could be found
     */
    public static Module getCommonModule(Module ... modules) {
        return getCommonModule(Arrays.asList(modules));
    }

    
    /**
     * Returns a common module for the given list of modules.
     * @return null if no common module could be found
     */
    public static Module getCommonModule(List<Module> modules) {
        LinkedList<Ring> rings = new LinkedList<>();
        int dim = 0;
        for (Module module : modules) {
            rings.add(module.getRing());
            // compute largest dimension
            if (module.getDimension() > dim) {
                dim = module.getDimension();
            }
        }
        // compute common ring
        Ring commonRing = getMinRing(rings);
        if (commonRing == null) {
            return ZRing.ring.getNullModule();
        }

        return commonRing.getFreeModule(dim);
    }
    
    
    /**
     * Returns a common module for the addresses of the given list of denotators.
     * @return null if no common module could be found
     */
    public static Module getCommonDenotatorModule(List<Denotator> denotators) {
        if (denotators.size() == 0) {
            return ZRing.nullModule;
        }
        LinkedList<Ring> rings = new LinkedList<>();
        int dim = 0;
        for (Denotator d : denotators) {
            Module module = d.getAddress();
            rings.add(module.getRing());
            // compute largest dimension
            dim = Math.max(module.getDimension(), dim);
        }
        // compute common ring
        Ring commonRing = getMinRing(rings);
        if (commonRing == null) {
            return ZRing.ring.getNullModule();
        }
        return commonRing.getFreeModule(dim);
    }
    
    /**
     * Returns the minimal ring among the given rings.
     * @return null if the minimal ring could not be found
     */
    public static Ring getMinRing(Collection<Ring> rings) {
        Pair<Module<?,?>,Module<?,?>> ringPair = new Pair<>();
        Ring minRing = null;
        for (Ring ring : rings) {
            if (minRing == null) {
                minRing = ring;
            }
            else if (!ring.equals(minRing)) {
                ringPair.first = minRing;
                ringPair.second = ring;
                minRing = getMinRing(ringPair);
                if (minRing == null) {
                    return ZRing.ring;
                }
            }
        }
        return minRing;
    }
    
    
    /**
     * Returns the minimal ring of the two rings in <code>ringPair</code>.
     * Each pair is cached for later lookup.
     */
    private static Ring getMinRing(Pair<Module<?,?>,Module<?,?>> ringPair) {
        Ring ring = RING_ORDER.get(ringPair);
        if (ring == null) {
            ring = getMinRing((Ring)ringPair.first, (Ring)ringPair.second);
            RING_ORDER.put(ringPair.copy(), ring);
        }
        return ring;
    }
    
    
    /**
     * Returns the minimal ring of the two given rings.
     */
    private static Ring getMinRing(Ring ring1, Ring ring2) {
        if (ring1 instanceof NumberRing && ring2 instanceof NumberRing) {
            if (ring1 instanceof ZnRing || ring2 instanceof ZnRing) {
                return ZRing.ring;
            }
            return (ring1.compareTo(ring2) < 0) ? ring1 : ring2;
        }
        if (ring1 instanceof NumberRing) {
            return getMinNumberRing(ring1, ring2);
        }
        if (ring2 instanceof NumberRing) {
            return getMinNumberRing(ring2, ring1);
        }
        if (ring1 instanceof ProductRing) {
            return getMinProductRing((ProductRing)ring1, ring2);
        }
        if (ring1 instanceof PolynomialRing) {
            return getMinPolynomialRing((PolynomialRing)ring1, ring2);
        }
        if (ring1 instanceof StringRing) {
            return getMinStringRing((StringRing)ring1, ring2);
        }
        return null;
    }
    
    
    /**
     * Returns the minimal ring of the two given number rings.
     */
    private static Ring getMinNumberRing(Ring ring1, Ring ring2) {
        if (ring2 instanceof PolynomialRing) {
            return getMinRing(ring1, ((PolynomialRing)ring2).getCoefficientRing());
        }
        else if (ring2 instanceof StringRing) {
            return getMinRing(ring1, ((StringRing)ring2).getFactorRing());
        }
        else if (ring2 instanceof ProductRing) {
            List<Ring> ringList = new LinkedList<Ring>();
            ringList.add(ring1);
            for (Ring ring : ((ProductRing)ring2).getFactors()) {
                ringList.add(ring);
            }
            return getMinRing(ringList);
        }
        else {
            return ZRing.ring;
        }
    }
    
    
    private static Ring getMinProductRing(ProductRing ring1, Ring ring2) {
        if (ring2 instanceof PolynomialRing) {
            return getMinRing(ring1, ((PolynomialRing)ring2).getCoefficientRing());
        }
        else if (ring2 instanceof StringRing) {
            return getMinRing(ring1, ((StringRing)ring2).getFactorRing());
        }
        else if (ring2 instanceof ProductRing) {
            ProductRing pRing = (ProductRing)ring2;
            if (ring1.getFactorCount() != pRing.getFactorCount()) {
                return ZRing.ring;
            }
            int len = ring1.getFactorCount();
            Ring[] factors = new Ring[len];
            Pair<Module<?,?>,Module<?,?>> ringPair = new Pair<>();
            for (int i = 0; i < len; i++) {
                ringPair.first = ring1.getFactor(i);
                ringPair.second = pRing.getFactor(i);
                Ring ring = getMinRing(ringPair);
                if (ring == null) {
                    return null;
                }

                factors[i] = ring;
            }
            return ProductRing.make(factors);
        }
        else {
            return ZRing.ring;
        }
    }

    
    private static Ring getMinPolynomialRing(PolynomialRing ring1, Ring ring2) {
        if (ring2 instanceof PolynomialRing) {
            Ring ring = getMinRing(ring1.getCoefficientRing(), ((PolynomialRing)ring2).getCoefficientRing());
            String ind1 = ring1.getIndeterminate();
            String ind2 = ((PolynomialRing)ring2).getIndeterminate();
            String ind = ind1.compareTo(ind2) < 0?ind1:ind2;
            return PolynomialRing.make(ring, ind);
        }
        else if (ring2 instanceof StringRing) {
            return getMinRing(ring1.getCoefficientRing(), ((StringRing)ring2).getFactorRing());
        }
        else if (ring2 instanceof ProductRing) {
            // TODO: not yet implemented
            throw new UnsupportedOperationException("Not implemented");
        }
        else {
            // TODO: not yet implemented
            throw new UnsupportedOperationException("Not implemented");
        }
    }

    
    private static Ring getMinStringRing(StringRing ring1, Ring ring2) {
        Pair<Module<?,?>,Module<?,?>> ringPair = new Pair<>();
        if (ring2 instanceof PolynomialRing) {
            ringPair.first = ring1.getFactorRing();
            ringPair.second = ((PolynomialRing)ring2).getCoefficientRing();
            return getMinRing(ringPair);
        }
        else if (ring2 instanceof StringRing) {
            ringPair.first = ring1.getFactorRing();
            ringPair.second = ((StringRing)ring2).getFactorRing();
            return getMinRing(ringPair);
        }
        else if (ring2 instanceof ProductRing) {
            ringPair.first = ring1.getFactorRing();
            ringPair.second = ring2;
            return getMinRing(ringPair);
        }
        else {
            // TODO: not yet implemented
            throw new UnsupportedOperationException("Not implemented");
        }
    }
    
}

