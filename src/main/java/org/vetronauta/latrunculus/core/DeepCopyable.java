package org.vetronauta.latrunculus.core;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Interface to enforce a Cloneable-like typed contract
 * @see java.lang.Cloneable
 * @author vetronauta
 */
public interface DeepCopyable<T> {

    T deepCopy();

    static <X extends DeepCopyable<X>> List<X> listOf(List<? extends X> list) {
        return list.stream().map(DeepCopyable::deepCopy).collect(Collectors.toList());
    }

}
