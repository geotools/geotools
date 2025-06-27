/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2014 - 2015, Open Source Geospatial Foundation (OSGeo)
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

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.measure.Unit;
import org.geotools.measure.Measure;
import org.geotools.measure.UnitFormat;
import org.geotools.util.Converter;
import org.geotools.util.ConverterFactory;
import org.geotools.util.factory.Hints;

/**
 * ConverterFactory which converts between the {@link Measure} and String.
 *
 * @author Andrea Aime - GeoSolutions
 * @since 12.0
 */
public class MeasureConverterFactory implements ConverterFactory {

    static final Pattern MEASURE_PATTERN = Pattern.compile("\\s*([-\\+]?[0-9]*\\.?[0-9]*(?:[eE][-\\+]?[0-9]+)?)(.*)?");

    public static final Converter CONVERTER = new Converter() {
        @Override
        public <T> T convert(Object source, Class<T> target) throws Exception {
            if (source == null) {
                return null;
            } else if (String.class.equals(target)) {
                Measure m = (Measure) source;
                DecimalFormat format = (DecimalFormat) NumberFormat.getNumberInstance(Locale.ENGLISH);
                if (m.doubleValue() < 1e-5) {
                    format.applyPattern("0.###E0");
                } else {
                    format.applyPattern("#0.##");
                }
                String v = format.format(m.doubleValue());
                if (m.getUnit() != null) {
                    return target.cast(v + UnitFormat.getInstance().format(m.getUnit()));
                } else {
                    return target.cast(v);
                }
            }

            if (!Measure.class.isAssignableFrom(target) || source == null) {
                return null;
            }

            String str = (String) source;
            if (str.trim().isEmpty()) {
                return null;
            }
            Matcher matcher = MEASURE_PATTERN.matcher(str);
            if (matcher.matches()) {
                double value = Double.parseDouble(matcher.group(1));
                Unit unit = null;
                if (matcher.groupCount() == 2) {
                    // this will throw an exception in case of failure
                    String group = matcher.group(2).trim();
                    if (!group.isEmpty()) {
                        unit = UnitFormat.getInstance().parse(group);
                    }
                }

                return target.cast(new Measure(value, unit));
            } else {
                throw new IllegalArgumentException("Invalid measure " + str);
            }
        }
    };

    @Override
    public Converter createConverter(Class source, Class target, Hints hints) {
        if (CharSequence.class.isAssignableFrom(source) && Measure.class.isAssignableFrom(target)) {
            return CONVERTER;
        } else if (String.class.isAssignableFrom(target) && Measure.class.isAssignableFrom(source)) {
            return CONVERTER;
        }

        // not a case we can handle
        return null;
    }
}
