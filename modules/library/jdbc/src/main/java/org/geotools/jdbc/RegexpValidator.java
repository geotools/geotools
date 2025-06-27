/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.jdbc;

import java.util.regex.Pattern;
import org.geotools.jdbc.VirtualTableParameter.Validator;

/**
 * A regular expression based validator
 *
 * @author Andrea Aime - OpenGeo
 */
public class RegexpValidator implements Validator {
    Pattern pattern;

    public RegexpValidator(Pattern pattern) {
        this.pattern = pattern;
    }

    public RegexpValidator(String pattern) {
        this.pattern = Pattern.compile(pattern);
    }

    @Override
    public void validate(String value) throws IllegalArgumentException {
        if (!pattern.matcher(value).matches()) {
            throw new IllegalArgumentException("Value " + value + " does not match " + pattern.pattern());
        }
    }

    public Pattern getPattern() {
        return pattern;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (pattern == null ? 0 : pattern.pattern().hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        RegexpValidator other = (RegexpValidator) obj;
        if (pattern == null) {
            if (other.pattern != null) return false;
        } else if (!pattern.pattern().equals(other.pattern.pattern())) return false;
        return true;
    }
}
