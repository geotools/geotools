/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2010, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.geojson.feature;

import java.io.IOException;

import org.geotools.geojson.HandlerBase;
import org.geotools.geojson.IContentHandler;
import org.geotools.referencing.CRS;
import org.json.simple.parser.ParseException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

public class CRSHandler extends HandlerBase implements IContentHandler<CoordinateReferenceSystem> {

    CoordinateReferenceSystem crs;
    int state = 0;
    
    public boolean startObjectEntry(String key) throws ParseException, IOException {
        if ("properties".equals(key)) {
            state = 1;
        }
        else if ("name".equals(key) && state == 1) {
            state = 2;
        }
        return true;
    }
    
    public boolean primitive(Object value) throws ParseException, IOException {
        if (state == 2) {
            try {
                crs = CRS.decode(value.toString());
            }
            catch(Exception e) {
                throw (IOException) new IOException("Error parsing " + value + " as crs id").initCause(e);
            }
            state = -1;
        }
        
        return true;
    }

    public CoordinateReferenceSystem getValue() {
        return crs;
    }

}
