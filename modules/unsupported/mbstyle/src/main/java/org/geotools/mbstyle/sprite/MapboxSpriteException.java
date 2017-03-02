package org.geotools.mbstyle.sprite;

import org.json.simple.parser.ParseException;

public class MapboxSpriteException extends RuntimeException {

    /** serialVersionUID */
    private static final long serialVersionUID = 1371164543614500159L;

    public MapboxSpriteException(String msg) {
        super(msg);
    }
    
    public MapboxSpriteException(String msg, Throwable cause) {
        super(msg, cause);
    }

}
