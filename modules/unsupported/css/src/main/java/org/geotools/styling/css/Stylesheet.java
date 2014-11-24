/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2014, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.styling.css;

import java.util.List;

import org.geotools.styling.Rule;

/**
 * A CSS stylesheet, that is, a ordered list of {@link Rule}
 * 
 * @author Andrea Aime - GeoSolutions
 *
 */
public class Stylesheet {

    private List<CssRule> rules;

    public Stylesheet(List<CssRule> rules) {
        this.setRules(rules);
    }

    @Override
    public String toString() {
        return "Stylesheet [rules=" + getRules() + "]";
    }

    /**
     * The list of rules in the stylesheet
     * 
     * @return
     */
    List<CssRule> getRules() {
        return rules;
    }

    void setRules(List<CssRule> rules) {
        this.rules = rules;
    }

}
