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

    public static Pointer<Pointer<Byte>> pointerToCStrings(String[] strings) {
        Pointer<Pointer<Byte>> p = null; 
        if (strings != null && strings.length > 0) {
            p = Pointer.pointerToCStrings(strings);
        }
        return p;
    }
   
}
