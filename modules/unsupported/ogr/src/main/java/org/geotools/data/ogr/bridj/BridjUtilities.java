package org.geotools.data.ogr.bridj;

import org.bridj.Pointer;

/**
 * 
 *
 * @source $URL$
 */
public class BridjUtilities {

    public static String getCString(Pointer<?> ptr) {
        if (ptr == null) {
            return null;
        } else {
            return ptr.getCString();
        }
    }
}
