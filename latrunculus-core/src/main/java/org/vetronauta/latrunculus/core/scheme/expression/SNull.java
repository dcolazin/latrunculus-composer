package org.vetronauta.latrunculus.core.scheme.expression;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SNull extends SExpr {

    public static final SNull SCHEME_NULL = new SNull();

    @Override
    public SType type() {
        return SType.NULL;
    }

    public boolean eq_p(SExpr sexpr) { return this == sexpr; }
    public boolean eqv_p(SExpr sexpr) { return this == sexpr; }
    public boolean equal_p(SExpr sexpr) { return this == sexpr; }
    public boolean equals(Object obj) { return this == obj; }
    public int getLength() { return 0; }

}
