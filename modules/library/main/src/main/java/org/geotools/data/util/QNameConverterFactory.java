/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2014, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.util;

import javax.xml.namespace.QName;
import org.geotools.util.Converter;
import org.geotools.util.ConverterFactory;
import org.geotools.util.factory.Hints;

/**
 * ConverterFactory for handling qname conversions.
 *
 * @author Niels Charlier
 */
public class QNameConverterFactory implements ConverterFactory {

    @Override
    public Converter createConverter(Class<?> source, Class<?> target, Hints hints) {
        if (target.equals(String.class)) {
            // qname to string
            if (source.equals(QName.class)) {
                return new Converter() {

                    @Override
                    public <T> T convert(Object source, Class<T> target) throws Exception {
                        QName qname = (QName) source;
                        if (qname.getPrefix() == null || "".equals(qname.getPrefix())) {
                            return target.cast(qname.getLocalPart());
                        } else {
                            return target.cast(qname.getPrefix() + ":" + qname.getLocalPart());
                        }
                    }
                };
            }
        }

        return null;
    }
}
