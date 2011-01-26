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

import org.geotools.data.vpf.io.RowField;
import org.geotools.data.vpf.io.TripletId;
import org.opengis.geometry.DirectPosition;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
/*
 * EdgeData.java
 *
 * Created on 6. april 2004, 14:54
 *
 * @author  <a href="mailto:knuterik@onemap.org">Knut-Erik Johnsen</a>, Project OneMap
 * @source $URL$
 */
public class EdgeData extends HashMap {
    public Object put(Object key, Object value) {
        if (key instanceof String) {
            GeometryFactory geofactory = new GeometryFactory();
            String key_s = (String) key;

            if (key_s.equals("coordinates")) {
                Coordinate[] c = null;
                if ( value instanceof RowField ) {
                    value = ((RowField)value).getValue();
                    if ( value instanceof DirectPosition[] ) {
                        DirectPosition[] coords = (DirectPosition[]) value;
                        c = new Coordinate[ coords.length ];
                        double[] c_pair = null;  
                        for( int i = 0; i < coords.length; i++ ) {
                            c_pair = coords[i].getCoordinates();
                            if ( coords[i].getDimension() == 2 ) {
                                c[i] = new Coordinate( c_pair[0], c_pair[1] );
                            } else if (coords[i].getDimension() == 3 ) {
                                c[i] = new Coordinate( c_pair[0], c_pair[1], c_pair[2] );
                            }                            
                        }
                    }                        
                }
                
                return super.put(key_s, geofactory.createLineString(c));
            } else if (key_s.equals("right_face") || 
                           key_s.equals("left_face") || 
                           key_s.equals("right_edge") || 
                           key_s.equals("left_edge")) {
                if (value != null) {
                    Object tmp = ((RowField) value).getValue();

                    if (tmp instanceof TripletId) {
                        return super.put(key_s, (TripletId) tmp );
                    } else if ( tmp instanceof Integer ) {
                        return super.put(key_s, ((Integer) tmp) );
                    } else {
                        System.out.println( "DYNGE I TRIPLETGENERERING!!!" );
                    }
                } else {
                    return super.put(key_s, null );
                }
            }
        }

        return super.put(key, value);
    }
}
