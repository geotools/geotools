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

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.geotools.factory.CommonFactoryFinder;

import org.opengis.filter.FilterFactory;
import org.opengis.filter.expression.Expression;
import org.opengis.style.ContrastMethod;
import org.opengis.style.StyleVisitor;

/**
 * @author iant
 *
 */
public class Exponential implements ContrastMethod {

    FilterFactory ff = CommonFactoryFinder.getFilterFactory2();

    private Map<String, Expression> params;

    private Expression algorithm;

    final static String NAME = "Exponential";

    final static List<String> PARAM_NAMES = Arrays.asList("normalizationFactor", "normalizationFactor");

    private static final Logger LOGGER = org.geotools.util.logging.Logging
            .getLogger("org.geotools.core");

    /**
     * 
     */
    public Exponential() {
        // TODO Auto-generated constructor stub
    }

    /**
     * 
     */
    public Exponential(FilterFactory f) {
        ff = f;
    }

    /**
     * 
     */
    public Exponential(Exponential e) {
        ff = e.getFilterFactory();
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
        if (params == null)
            params = new HashMap<String, Expression>();
        return params;
    }

    public void addParameter(String key, Expression value) {
        if (!PARAM_NAMES.contains(key)) {
            LOGGER.log(Level.WARNING, "Adding unexpected parameter {0} to {1} Contrast Enhancer",
                    new Object[] { key, NAME });
        }
        if (params == null)
            params = new HashMap<String, Expression>();
        params.put(key, value);
    }

    @Override
    public void accept(StyleVisitor visitor) {
        // TODO Auto-generated method stub

    }

    @Override
    public String name() {
        return NAME;
    }

    @Override
    public FilterFactory getFilterFactory() {
        return ff;
    }



    @Override
    public void setAlgorithm(Expression name) {
        algorithm = name;
        
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
        if (!(obj instanceof Exponential)) {
            return false;
        }
        Exponential other = (Exponential) obj;
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
