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

    public static Pointer<Pointer<Byte>> pointerToCStrings(String[] strings) {
        Pointer<Pointer<Byte>> p = null;
        if (strings != null && strings.length > 0) {
            // The array of Strings must end in a null string
            String[] newStrings = new String[strings.length + 1];
            System.arraycopy(strings, 0, newStrings, 0, strings.length);
            p = Pointer.pointerToCStrings(newStrings);
        }
        return p;
    }
}
