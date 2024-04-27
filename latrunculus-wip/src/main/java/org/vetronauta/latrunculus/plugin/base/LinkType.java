package org.vetronauta.latrunculus.plugin.base;

public enum LinkType { //TODO this should be a client prop

    LINE, ZIGZAG, CURVE;

    public static LinkType of(int i) {
        if (i < 0 || i >= LinkType.values().length) {
            return null;
        }
        return LinkType.values()[i];
    }

}
