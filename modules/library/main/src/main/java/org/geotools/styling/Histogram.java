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
package org.geotools.styling;

import java.util.HashMap;
import java.util.Map;

import org.geotools.factory.CommonFactoryFinder;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.expression.Expression;
import org.opengis.style.ContrastMethod;
import org.opengis.style.StyleVisitor;

/**
 * @author iant
 *
 */
public class Histogram implements ContrastMethod {

    FilterFactory ff = CommonFactoryFinder.getFilterFactory2();

    private Map<String, Expression> params;

    private Expression algorithm;

    final static String NAME = "Histogram";

    public Histogram() {

    }

    public Histogram(ContrastMethod method) {
        if (!(method instanceof Histogram)) {
            throw new RuntimeException("tried to construct Histogram with " + method.getClass());
        }
        ff = method.getFilterFactory();

    }

    /**
     * @param filterFactory
     */
    public Histogram(FilterFactory filterFactory) {
        ff = filterFactory;
    }

    @Override
    public Expression getType() {
        return ff.literal(name());
    }

    @Override
    public Expression getAlgorithm() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Map<String, Expression> getParameters() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String name() {
        return NAME;
    }

    @Override
    public void accept(StyleVisitor visitor) {
        // TODO Auto-generated method stub

    }

    @Override
    public FilterFactory getFilterFactory() {
        return ff;
    }

    @Override
    public void addParameter(String key, Expression value) {
        if (this.params == null) {
            params = new HashMap<String, Expression>();
        }
        this.params.put(key, value);

    }

    @Override
    public void setAlgorithm(Expression name) {
        this.algorithm = name;

    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((algorithm == null) ? 0 : algorithm.hashCode());
        result = prime * result + ((ff == null) ? 0 : ff.hashCode());
        result = prime * result + ((params == null) ? 0 : params.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof Histogram)) {
            return false;
        }
        Histogram other = (Histogram) obj;
        if (algorithm == null) {
            if (other.algorithm != null) {
                return false;
            }
        } else if (!algorithm.equals(other.algorithm)) {
            return false;
        }
        if (ff == null) {
            if (other.ff != null) {
                return false;
            }
        } else if (!ff.equals(other.ff)) {
            return false;
        }
        if (params == null) {
            if (other.params != null) {
                return false;
            }
        } else if (!params.equals(other.params)) {
            return false;
        }
        return true;
    }

}
