/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2015, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.postgis;

import org.geotools.factory.Hints;
import org.geotools.util.Converter;
import org.geotools.util.XMLDocumentConverterFactory;
import org.postgresql.util.PGobject;

public class PostgisXMLDocumentConverterFactory extends
        XMLDocumentConverterFactory {

    @Override
    public Converter createConverter(Class source, Class target, Hints hints) {
        if (source.equals(PGobject.class)) {
            return super.createConverter(String.class, target, hints);
        }
        return null;
    }
}
