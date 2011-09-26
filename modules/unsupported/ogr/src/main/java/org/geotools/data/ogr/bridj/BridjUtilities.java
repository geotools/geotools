package org.geotools.data.ogr.bridj;

import org.bridj.Pointer;

public class BridjUtilities {

    public static String getCString(Pointer<?> ptr) {
        if (ptr == null) {
            return null;
        } else {
            return ptr.getCString();
        }
    }
}
