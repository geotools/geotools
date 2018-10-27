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

/**
 * Provide an abstract base class for ContrastMethodStrategies which hold the actual implementations
 * of the optional methods to carry out contrast enhancements of rasters when rendering.
 *
 * <p>Subclasses must provide a {@link ContrastMethod} in method that represents the type of
 * operation and a NAME that matches that method.
 *
 * <p>This class provides all the required methods and functionality. Subclasses should override
 * methods only if they wish to check for specific algorithms or sets of parameter names. They may
 * also provide suitable default values if necessary.
 *
 * @author Ian Turton
 */
/** @author ian */
public abstract class AbstractContrastMethodStrategy implements ContrastMethodStrategy {

    /** ALGORITHM */
    public static final String ALGORITHM = "algorithm";

    protected FilterFactory filterFactory = CommonFactoryFinder.getFilterFactory2();

    protected ContrastMethod method = ContrastMethod.NONE;

    private Map<String, Expression> options = new HashMap<>();

    /**
     * set the map of VendorOptions to control this method.
     *
     * @param options a map of Expressions keyed by name.
     */
    public void setOptions(Map<String, Expression> options) {
        this.options = options;
    }

    /**
     * Find out the algorithm used by this method.
     *
     * @return an Expression which evaluates to the algorithm name, may be null;
     */
    public Expression getAlgorithm() {
        return options.get(ALGORITHM);
    }

    /**
     * Fetch any parameters which control the method.
     *
     * <p><strong>Note this does not contain the algorithm value</strong>.
     *
     * @return a map of Expressions keyed by parameter name.
     */
    public Map<String, Expression> getParameters() {
        HashMap<String, Expression> parameters = new HashMap<>(getOptions());
        if (parameters.containsKey(ALGORITHM)) {
            parameters.remove(ALGORITHM);
        }
        return parameters;
    }

    /**
     * the name of the ContrastMethod Currently one of Normalize, Histogram, Exponential &
     * Logarithmic
     *
     * <p>More methods may be added in future releases.
     *
     * @return A string containing the name of the method.
     */
    public String name() {
        return method.name();
    }

    /**
     * Fetch the filter factory used by the method.
     *
     * @return the filter factory.
     */
    public FilterFactory getFilterFactory() {
        return filterFactory;
    }

    /**
     * A parameter to be used by this method. Subclasses can implement checks for valid parameter
     * names by overriding this method.
     *
     * @param key the name of the parameter
     * @param value the expression that is it's value.
     */
    public void addParameter(String key, Expression value) {
        addOption(key, value);
    }

    /**
     * The algorithm to be used by this method. Subclasses can implement checks for valid algorithms
     * by overriding this method.
     *
     * @param name the expression that evaluates to the algorithm name.
     */
    public void setAlgorithm(Expression name) {
        addOption(ALGORITHM, name);
    }

    /**
     * The map of VendorOptions to write into an SLD or CSS file.
     *
     * @return options a map containing the algorithm name and any parameters that have been set.
     */
    public Map<String, Expression> getOptions() {
        return options;
    }

    /** @return the method that these values relate to. */
    public ContrastMethod getMethod() {
        return method;
    }

    @Override
    public void addOption(String key, Expression value) {
        options.put(key, value);
    }

    /** @param method the method to set */
    public void setMethod(ContrastMethod method) {
        this.method = method;
    }
}
