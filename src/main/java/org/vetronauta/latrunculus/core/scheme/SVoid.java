package org.vetronauta.latrunculus.core.scheme;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SVoid extends SExpr {

    public static final SVoid SCHEME_VOID = new SVoid();

    @Override
    public SType type() {
        return SType.VOID;
    }

    public boolean eq_p(SExpr sexpr) { return this == sexpr; }
    public boolean eqv_p(SExpr sexpr) { return this == sexpr; }
    public boolean equal_p(SExpr sexpr) { return this == sexpr; }
    public boolean equals(Object obj) { return this == obj; }

}
