/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2020, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.dggs.rhealpix;

import java.util.List;
import java.util.stream.Collectors;
import jep.JepException;
import jep.SharedInterpreter;

class RHealPixUtils {

    public static void setCellId(SharedInterpreter interpreter, String variableName, String id) throws JepException {
        interpreter.set(variableName, toInternalId(id));
        interpreter.exec(variableName + "= tuple(" + variableName + ")");
    }

    private static List<Object> toInternalId(String id) {
        return id.chars()
                .mapToObj(c -> c >= '0' && c <= '9' ? c - '0' : String.valueOf((char) c))
                .collect(Collectors.toList());
    }
}
