/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2007-2011, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.gce.imagemosaic.properties.numeric;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.data.util.NumericConverterFactory;
import org.geotools.gce.imagemosaic.properties.PropertiesCollectorSPI;
import org.geotools.gce.imagemosaic.properties.RegExPropertiesCollector;
import org.geotools.util.Converter;
import org.geotools.util.factory.GeoTools;
import org.geotools.util.logging.Logging;

abstract class NumericFileNameExtractor<N extends Number & Comparable<N>> extends RegExPropertiesCollector {

    static final NumericConverterFactory factory = new NumericConverterFactory();

    static class IntegerFileNameExtractor extends NumericFileNameExtractor<Integer> {

        public IntegerFileNameExtractor(PropertiesCollectorSPI spi, List<String> propertyNames, String regex) {
            super(spi, propertyNames, regex, Integer.class);
        }
    }

    static class ShortFileNameExtractor extends NumericFileNameExtractor<Short> {

        public ShortFileNameExtractor(PropertiesCollectorSPI spi, List<String> propertyNames, String regex) {
            super(spi, propertyNames, regex, Short.class);
        }
    }

    static class DoubleFileNameExtractor extends NumericFileNameExtractor<Double> {

        public DoubleFileNameExtractor(PropertiesCollectorSPI spi, List<String> propertyNames, String regex) {
            super(spi, propertyNames, regex, Double.class);
        }
    }

    static class FloatFileNameExtractor extends NumericFileNameExtractor<Float> {

        public FloatFileNameExtractor(PropertiesCollectorSPI spi, List<String> propertyNames, String regex) {
            super(spi, propertyNames, regex, Float.class);
        }
    }

    static class ByteFileNameExtractor extends NumericFileNameExtractor<Byte> {

        public ByteFileNameExtractor(PropertiesCollectorSPI spi, List<String> propertyNames, String regex) {
            super(spi, propertyNames, regex, Byte.class);
        }
    }

    static class LongFileNameExtractor extends NumericFileNameExtractor<Long> {

        public LongFileNameExtractor(PropertiesCollectorSPI spi, List<String> propertyNames, String regex) {
            super(spi, propertyNames, regex, Long.class);
        }
    }

    private static final Logger LOGGER = Logging.getLogger(NumericFileNameExtractor.class);

    private Class<? extends Number> targetClass;

    private Converter converter;

    public NumericFileNameExtractor(
            PropertiesCollectorSPI spi, List<String> propertyNames, String regex, final Class<N> targetClass) {
        super(spi, propertyNames, regex, false);

        this.targetClass = targetClass;
        this.converter = factory.createConverter(String.class, this.targetClass, GeoTools.getDefaultHints());
    }

    @Override
    public void setProperties(SimpleFeature feature) {

        // get all the matches and convert them in times
        final List<Number> values = new ArrayList<>();
        for (String match : getMatches()) {
            // try to convert to date
            try {
                values.add(converter.convert(match, targetClass));
            } catch (Exception e) {
                if (LOGGER.isLoggable(Level.INFO)) LOGGER.log(Level.INFO, e.getLocalizedMessage(), e);
            }
        }

        // set the properties, if we have some
        if (values.isEmpty()) {
            throw new IllegalArgumentException("No matches found for: " + this);
        }
        int index = 0;
        for (String propertyName : getPropertyNames()) {
            // set the property
            feature.setAttribute(propertyName, values.get(index++));

            // do we have more dates?
            if (index >= values.size()) return;
        }
    }

    @Override
    public String toString() {
        return "NumericFileNameExtractor{" + "targetClass="
                + targetClass + ", fullPath="
                + fullPath + ", pattern="
                + pattern + '}';
    }
}
