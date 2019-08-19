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

import java.util.Map;
import org.opengis.filter.expression.Expression;
import org.opengis.style.ContrastMethod;

/**
 * This interface defines the strategy that a ContrastEnhancement will use to execute the
 * ContrastMethod that it has defined. This allows uses to make use of VendorOptions in the SLD (&
 * CSS) to specify an algorithm to apply and named parameters to control how that algorithm
 * operates.
 *
 * @author Ian Turton.
 */
public interface ContrastMethodStrategy {
    /**
     * Add a VendorOption that controls how this strategy operates. All VendorOptions are optional
     * and the strategy will operate using default values if missing.
     *
     * @param key the name of the Vendor Option
     * @param value an expression that evaluates to the value of the option.
     */
    void addOption(String key, Expression value);

    /**
     * Return the Map of VendorOptions used by this strategy. This may be empty but should not be
     * null.
     *
     * @return the options a map of Expressions keyed by name.
     */
    Map<String, Expression> getOptions();

    /**
     * Set the options map - do not set it to null;
     *
     * @param options the options to set
     */
    void setOptions(Map<String, Expression> options);

    /**
     * Get the constant that defines the method that this strategy uses.
     *
     * @return the method
     */
    ContrastMethod getMethod();

    /**
     * Set the constant that defines the method that this strategy uses.
     *
     * @param method the method to set
     */
    void setMethod(ContrastMethod method);
}
