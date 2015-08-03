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
public class Normalize implements ContrastMethod {

    FilterFactory filterFactory = CommonFactoryFinder.getFilterFactory2();
    private Map<String, Expression> params;
    private Expression algorithm;
    final static String NAME = "Normalize";
    
    public Normalize() {
        
    }
    
    /**
     * @param filterFactory 
     * 
     */
    
    public Normalize(FilterFactory filterFactory) {
        this.filterFactory = filterFactory;
    }
    

    /**
     * @param method
     */
    public Normalize(ContrastMethod method) {
        if(!(method instanceof Normalize)) {
            throw new RuntimeException("tried to construct Normalize with "+method.getClass());
        }
        filterFactory = method.getFilterFactory();
        params = method.getParameters();
        algorithm = method.getAlgorithm();
    }

    @Override
    public Expression getType() {
        return filterFactory.literal(name());
    }

    @Override
    public Expression getAlgorithm() {
        // TODO Auto-generated method stub
        return this.algorithm;
    }

    @Override
    public Map<String, Expression> getParameters() {
        if(this.params == null) {
            params = new HashMap<String,Expression>();
        }
            return params;
    }

    @Override
    public void accept(StyleVisitor visitor) {
        visitor.visit(this, null);

    }

    public void  accept(StyleVisitor visitor, Object extra) {
        visitor.visit(this, extra);
    }
    
    @Override
    public String name() {
        return NAME;
    }

    @Override
    public FilterFactory getFilterFactory() {
        return filterFactory;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((algorithm == null) ? 0 : algorithm.hashCode());
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
        if (!(obj instanceof Normalize)) {
            return false;
        }
        Normalize other = (Normalize) obj;
        if (algorithm == null) {
            if (other.algorithm != null) {
                return false;
            }
        } else if (!algorithm.equals(other.algorithm)) {
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

    /**
     * @param key 
     * @param value 
     * 
     */
    public void addParameter(String key, Expression value) {
        params = getParameters();
        params.put(key, value);
        
    }

    /**
     * @param algorithm - the algorithm to use for the enhancement
     */
    public void setAlgorithm(Expression algor) {
        algorithm = algor;
        
    }

    
}
