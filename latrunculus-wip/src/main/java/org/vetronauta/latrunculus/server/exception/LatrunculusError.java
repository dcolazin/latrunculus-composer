package org.vetronauta.latrunculus.server.exception;

public class LatrunculusError extends Error {

    public LatrunculusError(String message) {
        super(message);
    }

    public LatrunculusError(Throwable th, String message) {
        super(message, th);
    }

}
