/*
 * Copyright (C) 2006 Gérard Milmeister
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

package org.vetronauta.latrunculus.core.math.module.morphism;

import org.rubato.util.Pair;
import org.vetronauta.latrunculus.core.math.arith.number.Rational;
import org.vetronauta.latrunculus.core.math.module.complex.CElement;
import org.vetronauta.latrunculus.core.math.module.complex.CRing;
import org.vetronauta.latrunculus.core.math.module.definition.Module;
import org.vetronauta.latrunculus.core.math.module.definition.ModuleElement;
import org.vetronauta.latrunculus.core.math.module.definition.Ring;
import org.vetronauta.latrunculus.core.math.module.definition.RingElement;
import org.vetronauta.latrunculus.core.math.module.definition.StringElement;
import org.vetronauta.latrunculus.core.math.module.definition.StringRing;
import org.vetronauta.latrunculus.core.math.module.integer.ZElement;
import org.vetronauta.latrunculus.core.math.module.integer.ZRing;
import org.vetronauta.latrunculus.core.math.module.modular.ZnElement;
import org.vetronauta.latrunculus.core.math.module.modular.ZnRing;
import org.vetronauta.latrunculus.core.math.module.rational.QElement;
import org.vetronauta.latrunculus.core.math.module.rational.QRing;
import org.vetronauta.latrunculus.core.math.module.real.RElement;
import org.vetronauta.latrunculus.core.math.module.real.RRing;

import java.util.HashMap;

/**
 * Morphism that casts elements from a ring to another ring.
 * The {@link #make} method is used to create a canonical morphism.
 * Only <i>proper</i> cast morphisms are created, not identities,
 * nor embeddings.
 * Therefore casting morphism should generally not be created directly
 * using this class, but through the {@link CanonicalMorphism}
 * class, this also takes care of free modules.
 * 
 * @author Gérard Milmeister
 */
public abstract class CastMorphism extends ModuleMorphism {

    /**
     * Creates a casting morphism from <code>domain</code>
     * to </code>codomain</code>.
     * 
     * @return null iff no such morphism could be created
     */
    public static ModuleMorphism make(Ring domain, Ring codomain) {
        // check if the required morphism is in the cache
        Pair<Module<?,?>,Module<?,?>> pair = Pair.makePair(domain, codomain);
        ModuleMorphism morphism = castingMorphisms.get(pair);
        if (morphism == null) {
            // if not, try to create it
            morphism = makeCastingMorphism(domain, codomain);
            if (morphism != null) {
                // put it into the cache
                castingMorphisms.put(pair, morphism);
            }
        }
        return morphism;
    }
    
    
    public final ModuleElement map(ModuleElement x)
            throws MappingException {
        ModuleElement res = null;
        
        if (getDomain().hasElement(x)) {
            res = mapValue(x);
        }
        
        if (res == null) {
            throw new MappingException("CastMorphism.map: ", x, this);
        }
        else {
            return res;
        }
    }

    /**
     * The low-level map method.
     * This must be implemented by subclasses.
     */
    public abstract ModuleElement mapValue(ModuleElement element);    

    
    /**
     * Creates a casting morphism from <code>domain</code>
     * to <code>codomain</code>.
     * 
     * @return null iff noch such morphism could be created
     */
    private static ModuleMorphism makeCastingMorphism(final Ring domain, final Ring codomain) {
        ModuleMorphism m = null;
        if (domain instanceof StringRing) {
            m = makeStringRingMorphism((StringRing)domain, codomain);
        }
        else if (domain == QRing.ring) {
            // Q -> ?
            if (codomain == ZRing.ring) {
                // Q -> Z
                m = new CastMorphism(domain, codomain) {
                    public ModuleElement mapValue(ModuleElement element) {
                        return new ZElement(((QElement)element).getValue().intValue());
                    }
                };
            }
            else if (codomain instanceof ZnRing) {
                // Q -> Zn
                final int modulus = ((ZnRing)codomain).getModulus();
                m = new CastMorphism(domain, codomain) {
                    public ModuleElement mapValue(ModuleElement element) {
                        return new ZnElement(((QElement)element).getValue().intValue(), modulus);
                    }
                };
            }
        }
        else if (domain == RRing.ring) {
            // R -> ?
            if (codomain == QRing.ring) {
                // R -> Q
                m = new CastMorphism(domain, codomain) {
                    public ModuleElement mapValue(ModuleElement element) {
                        return new QElement(new Rational(((RElement)element).getValue()));
                    }
                };
            }
            else if (codomain == ZRing.ring) {
                // R -> Z
                m = new CastMorphism(domain, codomain) {
                    public ModuleElement mapValue(ModuleElement element) {
                        return new ZElement((int)Math.round(((RElement)element).getValue()));
                    }
                };
            }
            else if (codomain instanceof ZnRing) {
                // R -> Zn
                final int modulus = ((ZnRing)codomain).getModulus();
                m = new CastMorphism(domain, codomain) {
                    public ModuleElement mapValue(ModuleElement element) {
                        return new ZnElement((int)Math.round(((RElement)element).getValue()), modulus);
                    }
                };
            }
        }
        else if (domain == CRing.ring) {
            // C -> ?
            if (codomain == RRing.ring) {
                // C -> R
                m = new CastMorphism(domain, codomain) {
                    public ModuleElement mapValue(ModuleElement element) {
                        return new RElement(((CElement)element).getValue().abs());
                    }
                };
            }
            else if (codomain == QRing.ring) {
                // C -> Q
                m = new CastMorphism(domain, codomain) {
                    public ModuleElement mapValue(ModuleElement element) {
                        return new QElement(new Rational(((CElement)element).getValue().abs()));
                    }
                };
            }
            else if (codomain == ZRing.ring) {
                // C -> Z
                m = new CastMorphism(domain, codomain) {
                    public ModuleElement mapValue(ModuleElement element) {
                        return new ZElement((int)Math.round(((CElement)element).getValue().abs()));
                    }
                };
            }
            else if (codomain instanceof ZnRing) {
                // C -> Zn
                final int modulus = ((ZnRing)codomain).getModulus();
                m = new CastMorphism(domain, codomain) {
                    public ModuleElement mapValue(ModuleElement element) {
                        return new ZnElement((int)Math.round(((CElement)element).getValue().abs()), modulus);
                    }
                };
            }
        }
        
        return m;
    }

    
    private static ModuleMorphism makeStringRingMorphism(final StringRing domain, final Ring codomain) {
        if (codomain instanceof StringRing) {
            return new CastMorphism(domain, codomain) {
                public ModuleElement mapValue(ModuleElement element) {
                    return codomain.cast(element);
                }
            };
        }
        else {
            final ModuleMorphism m = CanonicalMorphism.make(domain.getFactorRing(), codomain);
            if (m != null) {
                return new CastMorphism(domain, codomain) {
                    public ModuleElement mapValue(ModuleElement element) {
                        StringElement e = (StringElement)element;
                        ModuleElement s = domain.getFactorRing().getZero();
                        HashMap<String, RingElement> terms = e.getTerms();
                        try {
                            for (RingElement x : terms.values()) {
                                s.add(x);
                            }
                            return m.map(s);
                        }
                        catch (Exception ex) {                            
                            throw new RuntimeException("This should never happen!");
                        }
                    }
                };
            }
            else {
                return null;
            }
        }
    }


    /**
     * Casting morphisms are never module homomorphisms.
     */
    public boolean isModuleHomomorphism() {
        return false;
    }
    
    
    /**
     * Casting morphisms are never ring homomorphisms.
     */
    public boolean isRingHomomorphism() {
        return isRingMorphism();
    }

    
    public ModuleMorphism getRingMorphism() {
        return this;
    }
    
    
    public boolean equals(Object object) {
        if (object instanceof CastMorphism) {
            CastMorphism morphism = (CastMorphism)object;
            return getDomain().equals(morphism.getDomain()) &&
                   getCodomain().equals(morphism.getCodomain());
        }
        else {
            return false;
        }
    }

    
    public String toString() {
        return "CastMorphism["+getDomain()+","+getCodomain()+"]";
    }
    
    private static HashMap<Pair<Module<?,?>,Module<?,?>>,ModuleMorphism> castingMorphisms = new HashMap<>();

    public String getElementTypeName() {
        return "CastMorphism";
    }

    
    protected CastMorphism(Module domain, Module codomain) {
        super(domain, codomain);
    }
}
