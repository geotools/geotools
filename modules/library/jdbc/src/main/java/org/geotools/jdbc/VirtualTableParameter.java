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

import java.io.Serializable;


/**
 * A {@link VirtualTable} parameter. Has a name, a default value, and a {@link Validator} to protect
 * the parameters against sql injection attacks.
 * 
 * The class includes some default validators
 * 
 * @author Andrea Aime - OpenGeo
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/build/maven/javadoc/../../../modules/library/jdbc/src/main/java/org/geotools/jdbc/VirtualTableParameter.java $
 */
public class VirtualTableParameter implements Serializable {
    String name;

    String defaultValue;

    Validator validator;

    public VirtualTableParameter(String name, String defaultValue) {
        this(name, defaultValue, null);
    }
    
    public VirtualTableParameter(String name, String defaultValue, Validator validator) {
        this.name = name;
        this.defaultValue = defaultValue;
        this.validator = validator;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public Validator getValidator() {
        return validator;
    }

    public void setValidator(Validator validator) {
        this.validator = validator;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((defaultValue == null) ? 0 : defaultValue.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((validator == null) ? 0 : validator.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        VirtualTableParameter other = (VirtualTableParameter) obj;
        if (defaultValue == null) {
            if (other.defaultValue != null)
                return false;
        } else if (!defaultValue.equals(other.defaultValue))
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (validator == null) {
            if (other.validator != null)
                return false;
        } else if (!validator.equals(other.validator))
            return false;
        return true;
    }



    /**
     * Interface to be implemented by parameter value validators
     * 
     * @author aaime
     * 
     */
    public interface Validator extends Serializable {
        public void validate(String value) throws IllegalArgumentException;
    }
    
    
}
