/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.vpf.util;

import java.util.HashMap;
import java.util.StringTokenizer;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;


/*
 * AreaData.java
 *
 * Created on July 4, 2004, 5:35 PM 
 *
 * @author  <a href="mailto:knuterik@onemap.org">Knut-Erik Johnsen</a>, Project OneMap
 * @source $URL$
 */
public class PointData extends HashMap {
    public Object put(Object key, Object value) {
        if (key instanceof String) {
            String key_s = (String) key;

            if (key_s.equals("coordinate")) {
                StringTokenizer st = new StringTokenizer((String) value, "()");
                Coordinate[] c = new Coordinate[st.countTokens()];
                int i = 0;

                while (st.hasMoreTokens()) {
                    StringTokenizer st2 = new StringTokenizer(st.nextToken(), 
                                                              ",");
                    c[i] = new Coordinate(Double.parseDouble(st2.nextToken()), 
                                          Double.parseDouble(st2.nextToken()));
                    i++;
                }

                // System.out.println( "Antall koordinater: " + c.length);
                GeometryFactory geofactory = new GeometryFactory();

                return super.put("coordinate", geofactory.createPoint(c[0]));
            }
        }

        return super.put(key, value);
    }
}
