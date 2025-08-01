/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2025, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.vectortiles.store;

import java.util.regex.Pattern;
import org.geotools.api.feature.IllegalAttributeException;
import org.geotools.api.feature.Property;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.filter.expression.DirectPropertyAccessorFactory;
import org.geotools.filter.expression.PropertyAccessor;
import org.geotools.filter.expression.PropertyAccessorFactory;
import org.geotools.util.factory.Hints;

/**
 * Like {@link DirectPropertyAccessorFactory} but works on {@link SimpleFeature} attribtues, not {@link Property}, so we
 * can handle attribute names like {@literal @name} from vector tiles
 */
@SuppressWarnings("rawtypes")
public class RelaxedSimpleFeaturePropertyAccessorFactory implements PropertyAccessorFactory {

    /** Single instance is fine - we are not stateful */
    public static final PropertyAccessor ATTRIBUTE_ACCESS = new SimpleFeaturePropertyAccessor();

    static Pattern idPattern = Pattern.compile("@(\\w+:)?id");

    @Override
    public PropertyAccessor createPropertyAccessor(Class type, String xpath, Class target, Hints hints) {
        if (!SimpleFeature.class.isAssignableFrom(type) && !SimpleFeatureType.class.isAssignableFrom(type)) {
            return null; // we only work with simple feature
        }

        if (xpath == null || xpath.isBlank() || idPattern.matcher(xpath).matches()) {
            return null;
        }

        if (xpath.startsWith("@") || stripPrefixIndex(xpath).startsWith("@")) {
            return ATTRIBUTE_ACCESS;
        }
        return null;
    }

    /**
     * We strip off namespace prefix, we need new feature model to do this property
     *
     * <ul>
     *   <li>BEFORE: foo:bar
     *   <li>AFTER: bar
     * </ul>
     *
     * @return xpath with any XML prefixes removed
     */
    static String stripPrefixIndex(String xpath) {
        int split = xpath.indexOf(":");
        if (split != -1) {
            xpath = xpath.substring(split + 1);
        }
        if (xpath.endsWith("[1]")) {
            xpath = xpath.substring(0, xpath.length() - 3);
        }
        return xpath;
    }

    static class SimpleFeaturePropertyAccessor implements PropertyAccessor {
        @Override
        public boolean canHandle(Object object, String xpath, Class target) {
            String stripped = stripPrefixIndex(xpath);

            if (object instanceof SimpleFeature f) {
                object = f.getType();
            }

            if (object instanceof SimpleFeatureType type) {
                return type.indexOf(xpath) >= 0 || type.indexOf(stripped) >= 0;
            }

            return false;
        }

        @Override
        @SuppressWarnings("unchecked") // target can be null, cannot use target.cast
        public <T> T get(Object object, String xpath, Class<T> target) throws IllegalArgumentException {

            if (object instanceof SimpleFeature f) {
                SimpleFeatureType type = f.getType();
                if (type.indexOf(xpath) >= 0) {
                    return (T) f.getAttribute(xpath);
                } else {
                    String stripped = stripPrefixIndex(xpath);
                    return (T) f.getAttribute(stripped);
                }
            }

            if (object instanceof SimpleFeatureType type) {
                if (type.indexOf(xpath) >= 0) {
                    return (T) type.getDescriptor(xpath);
                } else {
                    String stripped = stripPrefixIndex(xpath);
                    return (T) type.getDescriptor(stripped);
                }
            }

            return null;
        }

        @Override
        public void set(Object object, String xpath, Object value, Class target) throws IllegalAttributeException {
            xpath = stripPrefixIndex(xpath);

            if (object instanceof SimpleFeature f) {
                f.setAttribute(xpath, value);
            }

            if (object instanceof SimpleFeatureType) {
                throw new IllegalAttributeException("feature type is immutable");
            }
        }
    }
}
