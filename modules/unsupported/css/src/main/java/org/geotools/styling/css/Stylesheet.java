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
 */
public class Stylesheet {

    private List<CssRule> rules;

    private List<Directive> directives;

    public Stylesheet(List<CssRule> rules, List<Directive> directives) {
        this.rules = rules;
        this.directives = directives;
    }

    /** The list of rules in the stylesheet */
    List<CssRule> getRules() {
        return rules;
    }

    void setRules(List<CssRule> rules) {
        this.rules = rules;
    }

    /** The list of directives parsed from the style */
    public List<Directive> getDirectives() {
        return directives;
    }

    public void setDirectives(List<Directive> directives) {
        this.directives = directives;
    }

    public String getDirectiveValue(String name) {
        for (Directive d : directives) {
            if (name.equals(d.getName())) {
                return d.getValue();
            }
        }

        return null;
    }

    @Override
    public String toString() {
        return "Stylesheet [rules=" + rules + ", directives=" + directives + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((directives == null) ? 0 : directives.hashCode());
        result = prime * result + ((rules == null) ? 0 : rules.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        Stylesheet other = (Stylesheet) obj;
        if (directives == null) {
            if (other.directives != null) return false;
        } else if (!directives.equals(other.directives)) return false;
        if (rules == null) {
            if (other.rules != null) return false;
        } else if (!rules.equals(other.rules)) return false;
        return true;
    }
}
