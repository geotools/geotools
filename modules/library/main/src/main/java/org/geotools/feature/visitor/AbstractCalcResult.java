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

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Point;


/**
 * An abstract implementation for CalcResults. Each subclass should implement
 * its own getValue(), merge(), and constructor methods.
 * 
 * @author Cory Horner, Refractions
 * 
 * @since 2.2.M2
 *
 * @source $URL$
 */
public class AbstractCalcResult implements CalcResult {
    public boolean isCompatible(CalcResult targetResults) {
        return targetResults == CalcResult.NULL_RESULT;
    }

    public CalcResult merge(CalcResult resultsToAdd) {
    	if(resultsToAdd == CalcResult.NULL_RESULT) {
    		return this;
    	} else {
    		if (!isCompatible(resultsToAdd)) {
                throw new IllegalArgumentException(
                    "Parameter is not a compatible type");
            } else {
            	throw new IllegalArgumentException(
				"The CalcResults claim to be compatible, but the appropriate merge " +
				"method has not been implemented.");
            }
    	}
    }

    public Object getValue() {
        return null;
    }

    public int toInt() {
        Object value = getValue();
        if (value instanceof Number) {
        	Number number = (Number) value;
        	return number.intValue();
        } else {
        	return (int) 0;
        }
    }

    public double toDouble() {
        Object value = getValue();
        if (value instanceof Number) {
        	Number number = (Number) value;
        	return number.doubleValue();
        } else {
        	return (double) 0;
        }
    }

    public long toLong() {
        Object value = getValue();
        if (value instanceof Number) {
        	Number number = (Number) value;
        	return number.longValue();
        } else {
        	return (long) 0;
        }
    }

    public float toFloat() {
        Object value = getValue();
        if (value instanceof Number) {
        	Number number = (Number) value;
        	return number.floatValue();
        } else {
        	return (float) 0;
        }
    }

    public Geometry toGeometry() {
    	Object value = getValue();
    	if (value instanceof Geometry)
    		return (Geometry) getValue();
    	else
    		return null;
    }

    public Envelope toEnvelope() {
    	Object value = getValue();
    	if (value instanceof Envelope)
    		return (Envelope) value;
    	else
    		return null;
    }
    
    public Point toPoint() {
        Geometry geometry = toGeometry();
        return geometry.getCentroid();
    }

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
            Set set = new HashSet(Arrays.asList((Object[]) value));

            return set;
        }

        if (value instanceof Collection) {
            Set set = new HashSet((Collection) value);

            return set;
        }

        return null;
    }

    public List toList() {
		Object value = getValue();

		if (value == null) {
			return null;
		}

		if (value instanceof List) {
			List list = (List) value;

			return list;
		}

		if (value.getClass().isArray()) {
			return Arrays.asList((Object[]) value);
		}

		if (value instanceof HashSet) {
			Set set = (HashSet) value;
			// Object[] values = set.toArray();
			return Arrays.asList(set.toArray());
			// List list = new ArrayList();
			// for (int i = 0; i < values.length; i++)
			// list.add(values[i]);
			// return list;
		}

		if (value instanceof Collection) {
			return new ArrayList((Collection) value);
		}

		return null;
	}

    public Object[] toArray() {
        List list = toList();

        if (list == null) {
            return null;
        }

        return list.toArray();
    }

    public String[] toStringArray() {
        List list = toList();

        if (list == null) {
            return null;
        }

        String[] strings = new String[list.size()];

        return (String[]) list.toArray(strings);
    }

    public Map toMap() {
        return (Map) getValue();
    }

    public String toString() {
        return getValue().toString();
    }
}
