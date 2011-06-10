/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2009-2011, Open Source Geospatial Foundation (OSGeo)
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

import java.util.Collection;

import org.geotools.factory.Hints;
import org.opengis.feature.Attribute;
import org.opengis.feature.ComplexAttribute;
import org.opengis.feature.Property;

/**
 * This converter retrieves the values out of attributes. 
 * @author Rini Angreani (CSIRO Earth Science and Resource Engineering) 
 *
 *
 *
 * @source $URL$
 */
public class ComplexAttributeConverterFactory implements ConverterFactory {

    public Converter createConverter(Class<?> source, Class<?> target, Hints hints) {
        if (ComplexAttribute.class.isAssignableFrom(source)) {
            return new Converter() {
                public Object convert(Object source, Class target) throws Exception {
                    while (source instanceof ComplexAttribute) {
                        if (!((ComplexAttribute) source).getType().getDescriptors().isEmpty()) {
                            // this is not the leaf type.. 
                            return null;
                        } else {
                            Collection<? extends Property> valueMap = ((ComplexAttribute) source)
                                    .getValue();
                            if (valueMap.isEmpty()) {
                                return null;
                            } else {
                                // there should only be one value
                                source = valueMap.iterator().next();
                            }
                        }
                    }
                    if (source instanceof Attribute) {
                        return ((Attribute) source).getValue();
                    }
                    return null;
                }
            };
        }
        if (Attribute.class.isAssignableFrom(source)) {
            return new Converter() {
                public Object convert(Object source, Class target) throws Exception {
                    return ((Attribute) source).getValue();
                }
            };
        }
        return null;
    }
}
