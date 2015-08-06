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

import org.opengis.filter.expression.Expression;
import org.opengis.style.ContrastMethod;

/**
 * @author ian
 *
 */
public class ContrastEnhancementMethod {
    Map<String, Expression> options = new HashMap<>();
    ContrastMethod method = ContrastMethod.NONE;
    /**
     * @return the options
     */
    public Map<String, Expression> getOptions() {
        return options;
    }
    /**
     * @param options the options to set
     */
    public void setOptions(Map<String, Expression> options) {
        this.options = options;
    }
    /**
     * @return the method
     */
    public ContrastMethod getMethod() {
        return method;
    }
    /**
     * @param method the method to set
     */
    public void setMethod(ContrastMethod method) {
        this.method = method;
    }
    /**
     * @param key
     * @param value
     */
    public void addOption(String key, Expression value) {
        options.put(key, value);
        
    }
}
