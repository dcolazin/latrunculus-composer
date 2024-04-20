package org.vetronauta.latrunculus.core.repository;

public interface LoadableDictionary extends Dictionary {

    void read();
    boolean hasError();

}
