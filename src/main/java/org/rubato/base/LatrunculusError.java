package org.rubato.base;

public class LatrunculusError extends Error {

    public LatrunculusError() {
        super();
    }

    public LatrunculusError(String message) {
        super(message);
    }

    public LatrunculusError(String messageFormat, Object... objects) {
        super(String.format(messageFormat, objects));
    }

    public LatrunculusError(Throwable th) {
        super(th);
    }

    public LatrunculusError(Throwable th, String message) {
        super(message, th);
    }

    public LatrunculusError(Throwable th, String messageFormat, Object... objects) {
        super(String.format(messageFormat, objects), th);
    }

}
