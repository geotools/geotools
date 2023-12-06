/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2023, Open Source Geospatial Foundation (OSGeo)
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

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.geotools.api.referencing.FactoryException;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.util.Converter;
import org.geotools.util.ConverterFactory;
import org.geotools.util.factory.Hints;

public class ReferencedEnvelopeConverterFactory implements ConverterFactory {
    @Override
    public Converter createConverter(Class source, Class target, Hints hints) {

        if (ReferencedEnvelope.class.isAssignableFrom(target) && String.class.equals(source)) {

            return new Converter() {
                final CRSConverterFactory.CRSConverter crsConverter =
                        new CRSConverterFactory.CRSConverter();

                @Override
                public <T> T convert(Object source, Class<T> target) throws Exception {
                    Pattern pat =
                            Pattern.compile(
                                    "\\[([-+]?[0-9]*\\.?[0-9]+) : ([-+]?[0-9]*\\.?[0-9]+), ([-+]?[0-9]*\\.?[0-9]+) : ([-+]?[0-9]*\\.?[0-9]+)] \\{(.*)\\}",
                                    Pattern.MULTILINE | Pattern.DOTALL);

                    Matcher m = pat.matcher((String) source);

                    if (m.find()) {
                        double minX = Double.parseDouble(m.group(1));
                        double maxX = Double.parseDouble(m.group(2));
                        double minY = Double.parseDouble(m.group(3));
                        double maxY = Double.parseDouble(m.group(4));
                        try {
                            CoordinateReferenceSystem crs =
                                    crsConverter.convert(
                                            m.group(5), CoordinateReferenceSystem.class);
                            return target.cast(new ReferencedEnvelope(minX, maxX, minY, maxY, crs));
                        } catch (FactoryException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    throw new RuntimeException("Badly formed ReferencedEnvelope");
                }
            };
        } else if (ReferencedEnvelope.class.isAssignableFrom(source)
                && String.class.equals(target)) {
            return new Converter() {
                @Override
                public <T> T convert(Object source, Class<T> target) throws Exception {

                    ReferencedEnvelope env = (ReferencedEnvelope) source;
                    CRSConverterFactory.CRSConverter crsConverter =
                            new CRSConverterFactory.CRSConverter();
                    String crs =
                            crsConverter.convert(env.getCoordinateReferenceSystem(), String.class);

                    return target.cast(
                            "["
                                    + env.getMinX()
                                    + " : "
                                    + env.getMaxX()
                                    + ", "
                                    + env.getMinY()
                                    + " : "
                                    + env.getMaxY()
                                    + "] {"
                                    + crs
                                    + "}");
                }
            };
        }

        return null;
    }
}
