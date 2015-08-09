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

/**
 * @author ian
 *
 */
public abstract class AbstractContrastEnhancementMethod extends ContrastEnhancementMethod {

    /** ALGORITHM */
    public static final String ALGORITHM = "algorithm";

    protected FilterFactory filterFactory = CommonFactoryFinder.getFilterFactory2();

    protected String NAME = "None";

    /**
     * 
     */
    public AbstractContrastEnhancementMethod() {
        super();
    }

    public Expression getType() {
        return filterFactory.literal(name());
    }

    public Expression getAlgorithm() {
        return options.get(ALGORITHM);
    }

    public Map<String, Expression> getParameters() {
        HashMap<String, Expression> parameters = new HashMap<>(getOptions());
        if (parameters.containsKey(ALGORITHM)) {
            parameters.remove(ALGORITHM);
        }
        return parameters;
    }

    public String name() {
        return NAME;
    }

    public FilterFactory getFilterFactory() {
        return filterFactory;
    }

    public void addParameter(String key, Expression value) {
        addOption(key, value);
    }

    public void setAlgorithm(Expression name) {
        addOption(ALGORITHM, name);
    }

}