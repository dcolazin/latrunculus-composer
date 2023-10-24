package org.vetronauta.latrunculus.core;

public class Wrapper<E> {

    private E value;

    public E get() {
        return value;
    }

    public void set(E value) {
        this.value = value;
    }

}
