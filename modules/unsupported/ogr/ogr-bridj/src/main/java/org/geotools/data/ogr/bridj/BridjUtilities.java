/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2019, Open Source Geospatial Foundation (OSGeo)
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */

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
