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
package org.geotools.util;

import javax.xml.namespace.QName;

import org.geotools.factory.Hints;

/**
 * ConverterFactory for handling qname conversions.
 * 
 * @author Niels Charlier
 * 
 * 
 * 
 * @source $URL$
 */
public class QNameConverterFactory implements ConverterFactory {

    public Converter createConverter(Class source, Class target, Hints hints) {
        if (target.equals(String.class)) {
            // qname to string
            if (source.equals(QName.class)) {
                return new Converter() {

                    public Object convert(Object source, Class target) throws Exception {
                        QName qname = (QName) source;
                        if (qname.getPrefix() == null || "".equals(qname.getPrefix())) {
                            return qname.getLocalPart();
                        } else {
                            return qname.getPrefix() + ":" + qname.getLocalPart();
                        }
                    }

                };
            }

        }

        return null;
    }

}
