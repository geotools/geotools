package org.geotools.graph.path;

@SuppressWarnings("serial")
class WrongPathException extends Exception {
    String message;

    public WrongPathException(String msj) {
        message = msj;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
