package org.geotools.mbstyle.sprite;

import org.json.simple.parser.ParseException;

public class MapboxSpriteException extends RuntimeException {

    public MapboxSpriteException(String msg) {
        super(msg);
    }
    
    public MapboxSpriteException(String msg, Throwable cause) {
        super(msg, cause);
    }

}
