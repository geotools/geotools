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
import org.geotools.util.ConverterFactory;
import org.geotools.util.XMLDocumentConverterFactory;
import org.geotools.util.XMLDocumentConverterFactory.StringToDocumentConverter;
import org.geotools.util.XMLDocumentConverterFactory.StringToDocumentFragmentConverter;
import org.postgresql.util.PGobject;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;

public class PostgisXMLDocumentConverterFactory implements ConverterFactory {

    @Override
    public Converter createConverter(Class source, Class target, Hints hints) {
        if (source.equals(PGobject.class)) {
            if (target.equals(Document.class)) {
                return new StringToDocumentConverter() {
                    @Override
                    public <T> T convert(Object source, Class<T> target) throws Exception {
                        return super.convert(source.toString(), target);
                    }
                };
            } else if (target.equals(DocumentFragment.class)) {
                return new StringToDocumentFragmentConverter(){
                    @Override
                    public <T> T convert(Object source, Class<T> target) throws Exception {
                        return super.convert(source.toString(), target);
                    }
                };
            }
        }
        return null;
    }
}
