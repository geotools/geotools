/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2005-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.feature.visitor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Point;

/**
 * An abstract implementation for CalcResults. Each subclass should implement its own getValue(), merge(), and
 * constructor methods.
 *
 * @author Cory Horner, Refractions
 * @since 2.2.M2
 */
public class AbstractCalcResult implements CalcResult {
    @Override
    public boolean isCompatible(CalcResult targetResults) {
        return targetResults == CalcResult.NULL_RESULT;
    }

    @Override
    public CalcResult merge(CalcResult resultsToAdd) {
        if (resultsToAdd == CalcResult.NULL_RESULT) {
            return this;
        } else {
            if (!isCompatible(resultsToAdd)) {
                throw new IllegalArgumentException("Parameter is not a compatible type");
            } else {
                throw new IllegalArgumentException("The CalcResults claim to be compatible, but the appropriate merge "
                        + "method has not been implemented.");
            }
        }
    }

    @Override
    public Object getValue() {
        return null;
    }

    @Override
    public int toInt() {
        Object value = getValue();
        if (value instanceof Number) {
            Number number = (Number) value;
            return number.intValue();
        } else {
            return 0;
        }
    }

    @Override
    public double toDouble() {
        Object value = getValue();
        if (value instanceof Number) {
            Number number = (Number) value;
            return number.doubleValue();
        } else {
            return 0;
        }
    }

    @Override
    public long toLong() {
        Object value = getValue();
        if (value instanceof Number) {
            Number number = (Number) value;
            return number.longValue();
        } else {
            return 0;
        }
    }

    @Override
    public float toFloat() {
        Object value = getValue();
        if (value instanceof Number) {
            Number number = (Number) value;
            return number.floatValue();
        } else {
            return 0f;
        }
    }

    @Override
    public Geometry toGeometry() {
        Object value = getValue();
        if (value instanceof Geometry) return (Geometry) getValue();
        else return null;
    }

    @Override
    public Envelope toEnvelope() {
        Object value = getValue();
        if (value instanceof Envelope) return (Envelope) value;
        else return null;
    }

    @Override
    public Point toPoint() {
        Geometry geometry = toGeometry();
        return geometry.getCentroid();
    }

    @Override
    public Set toSet() {
        Object value = getValue();

        if (value == null) {
            return null;
        }

        if (value instanceof Set) {
            Set set = (Set) value;

            return set;
        }

        if (value.getClass().isArray()) {
            Object[] cast = (Object[]) value;
            Set set = new HashSet<>(Arrays.asList(cast));

            return set;
        }

        if (value instanceof Collection) {
            @SuppressWarnings("unchecked")
            Collection<Object> cast = (Collection<Object>) value;
            Set set = new HashSet<>(cast);

            return set;
        }

        return null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Object> toList() {
        Object value = getValue();

        if (value == null) {
            return null;
        }

        if (value instanceof List) {
            return (List<Object>) value;
        }

        if (value.getClass().isArray()) {
            return Arrays.asList((Object[]) value);
        }

        if (value instanceof Collection) {
            Collection<Object> cast = (Collection<Object>) value;
            return new ArrayList<>(cast);
        }

        return null;
    }

    @Override
    public Object[] toArray() {
        List<Object> list = toList();

        if (list == null) {
            return null;
        }

        return list.toArray();
    }

    public String[] toStringArray() {
        List<Object> list = toList();

        if (list == null) {
            return null;
        }

        return list.stream().map(o -> o != null ? String.valueOf(o) : null).toArray(String[]::new);
    }

    @Override
    public Map toMap() {
        return (Map) getValue();
    }

    @Override
    public String toString() {
        return getValue().toString();
    }
}
